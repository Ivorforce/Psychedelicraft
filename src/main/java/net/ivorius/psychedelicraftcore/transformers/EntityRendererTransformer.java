/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.transformers;

import net.ivorius.psychedelicraftcore.ivToolkit.*;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by lukas on 21.02.14.
 */
public class EntityRendererTransformer extends IvClassTransformerClass
{
    public EntityRendererTransformer(Logger logger)
    {
        super(logger);

        registerExpectedMethod("updateCameraAndRender", "func_78480_b", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE));
        registerExpectedMethod("orientCamera", "func_78467_g", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE));
        registerExpectedMethod("renderHand", "func_78476_b", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.INT_TYPE));
        registerExpectedMethod("enableLightmap", "func_78463_b", getMethodDescriptor(Type.VOID_TYPE, Double.TYPE));
        registerExpectedMethod("disableLightmap", "func_78483_a", getMethodDescriptor(Type.VOID_TYPE, Double.TYPE));
        registerExpectedMethod("renderWorld", "func_78471_a", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE));
        registerExpectedMethod("renderWorldAdditions", "func_78471_a", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE));
        registerExpectedMethod("setupFog", "func_78468_a", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE, Type.FLOAT_TYPE));
        registerExpectedMethod("getFOVModifier", "func_78481_a", getMethodDescriptor(Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.BOOLEAN_TYPE));
        registerExpectedMethod("setupCameraTransform", "func_78479_a", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.INT_TYPE));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        if (methodID.equals("updateCameraAndRender"))
        {
            AbstractInsnNode preNode = IvASMHelper.findNode(new IvNodeMatcherLDC("level"), methodNode);
            AbstractInsnNode postNode = IvASMHelper.findNode(new IvNodeMatcherFieldSRG(GETSTATIC, "field_148824_g" /* shadersSupported */, "net/minecraft/client/renderer/OpenGlHelper", Type.BOOLEAN_TYPE), methodNode);

            if (preNode == null)
            {
                printSubMethodError(className, methodID, "pre");
            }
            else
            {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(FLOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "preWorldRender", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE)));
                methodNode.instructions.insert(preNode, list);
            }

            if (postNode == null)
            {
                printSubMethodError(className, methodID, "post");
            }
            else
            {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(FLOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "postWorldRender", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE)));
                methodNode.instructions.insertBefore(postNode, list);
            }

            return true;
        }
        else if (methodID.equals("orientCamera"))
        {
            InsnList list = new InsnList();
            list.add(new VarInsnNode(FLOAD, 1));
            list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "orientCamera", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE)));
            methodNode.instructions.insert(methodNode.instructions.get(0), list);

            return true;
        }
        else if (methodID.equals("enableLightmap"))
        {
            InsnList list = new InsnList();
            list.add(new InsnNode(ICONST_1));
            list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraft/client/rendering/DrugShaderHelper", "setLightmapEnabled", getMethodDescriptor(Type.VOID_TYPE, Type.BOOLEAN_TYPE)));
            methodNode.instructions.insert(methodNode.instructions.get(0), list);

            return true;
        }
        else if (methodID.equals("disableLightmap"))
        {
            InsnList list = new InsnList();
            list.add(new InsnNode(ICONST_0));
            list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraft/client/rendering/DrugShaderHelper", "setLightmapEnabled", getMethodDescriptor(Type.VOID_TYPE, Type.BOOLEAN_TYPE)));
            methodNode.instructions.insert(methodNode.instructions.get(0), list);

            return true;
        }
        else if (methodID.equals("renderHand"))
        {
            AbstractInsnNode transformMatrixNode = IvASMHelper.findNode(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glPushMatrix", "org/lwjgl/opengl/GL11", null), methodNode);
            AbstractInsnNode skipOverlayNode = IvASMHelper.findNode(new IvNodeMatcherMethodSRG(INVOKEVIRTUAL, "func_78447_b" /* renderOverlays */, "net/minecraft/client/renderer/ItemRenderer", Type.VOID_TYPE, Type.FLOAT_TYPE), methodNode);

            if (transformMatrixNode == null)
            {
                printSubMethodError(className, methodID, "renderHeldItem");
            }
            else
            {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(FLOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "renderHeldItem", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE)));
                methodNode.instructions.insert(transformMatrixNode, list);
            }

            if (skipOverlayNode == null)
            {
                printSubMethodError(className, methodID, "renderBlockOverlay");
            }
            else
            {
                LabelNode skipRenderOverlayNode = new LabelNode();
                methodNode.instructions.insert(skipOverlayNode, skipRenderOverlayNode);

                InsnList preList = new InsnList();
                preList.add(new VarInsnNode(FLOAD, 1));
                preList.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "renderBlockOverlay", getMethodDescriptor(Type.BOOLEAN_TYPE, Type.FLOAT_TYPE)));
                preList.add(new JumpInsnNode(IFNE, skipRenderOverlayNode));
                methodNode.instructions.insertBefore(skipOverlayNode.getPrevious().getPrevious().getPrevious(), preList);

                InsnList postList = new InsnList();
                methodNode.instructions.insert(skipOverlayNode, postList);
            }

            return true;
        }
        else if (methodID.equals("renderWorld"))
        {
            AbstractInsnNode transformNode = IvASMHelper.findNode(new IvNodeMatcherMethodSRG(INVOKESPECIAL, "func_78476_b" /* renderHand */, "net/minecraft/client/renderer/EntityRenderer", Type.VOID_TYPE, Type.FLOAT_TYPE, Type.INT_TYPE), methodNode);

            if (transformNode != null)
            {
                AbstractInsnNode glClearNode = transformNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious();

                if (glClearNode.getOpcode() == INVOKESTATIC && ((MethodInsnNode) glClearNode).name.equals("glClear") && ((MethodInsnNode) glClearNode).owner.equals("org/lwjgl/opengl/GL11"))
                {
                    LabelNode skipGLClearNode = new LabelNode();
                    methodNode.instructions.insert(glClearNode, skipGLClearNode);

                    InsnList preList = new InsnList();
                    preList.add(new VarInsnNode(FLOAD, 1));
                    preList.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "preRenderHand", getMethodDescriptor(Type.BOOLEAN_TYPE, Type.FLOAT_TYPE)));
                    preList.add(new JumpInsnNode(IFNE, skipGLClearNode));
                    methodNode.instructions.insertBefore(glClearNode.getPrevious(), preList);

                    InsnList postList = new InsnList();
                    postList.add(new VarInsnNode(FLOAD, 1));
                    postList.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "postRenderHand", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE)));
                    methodNode.instructions.insert(transformNode, postList);

                    return true;
                }
            }
        }
        else if (methodID.equals("setupFog"))
        {
            List<AbstractInsnNode> glFogiNodes = IvASMHelper.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glFogi", "org/lwjgl/opengl/GL11", null), methodNode);

            for (AbstractInsnNode callListNode : glFogiNodes)
            {
                InsnList listBefore = new InsnList();
                listBefore.add(new InsnNode(DUP2));
                listBefore.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLFogi", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE, Type.INT_TYPE)));
                methodNode.instructions.insertBefore(callListNode, listBefore);
            }

            return true;
        }
        else if (methodID.equals("getFOVModifier"))
        {
            List<AbstractInsnNode> returnNodes = IvASMHelper.findNodes(new IvNodeMatcherSimple(FRETURN), methodNode);

            for (AbstractInsnNode callListNode : returnNodes)
            {
                InsnList listBefore = new InsnList();
                listBefore.add(new InsnNode(DUP));
                listBefore.add(new VarInsnNode(ILOAD, 2));
                listBefore.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "updateFOVValue", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.BOOLEAN_TYPE)));
                methodNode.instructions.insertBefore(callListNode, listBefore);
            }

            return returnNodes.size() > 0;
        }
        else if (methodID.equals("setupCameraTransform"))
        {
            LabelNode realMethodStartNode = new LabelNode();

            InsnList list = new InsnList();
            list.add(new VarInsnNode(FLOAD, 1));
            list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "setupCameraTransform", getMethodDescriptor(Type.BOOLEAN_TYPE, Type.FLOAT_TYPE)));
            list.add(new JumpInsnNode(IFEQ, realMethodStartNode));
            list.add(new InsnNode(RETURN));
            list.add(realMethodStartNode);
            methodNode.instructions.insert(methodNode.instructions.get(0), list);

            return true;
        }
        else if (methodID.equals("renderWorldAdditions"))
        {
            List<AbstractInsnNode> valuepatchNodes = new ArrayList<AbstractInsnNode>();
            valuepatchNodes.addAll(IvASMHelper.findNodes(new IvNodeMatcherLDC("prepareterrain"), methodNode));
            valuepatchNodes.addAll(IvASMHelper.findNodes(new IvNodeMatcherLDC("water"), methodNode));
            valuepatchNodes.addAll(IvASMHelper.findNodes(new IvNodeMatcherLDC("entities"), methodNode));

            for (AbstractInsnNode node : valuepatchNodes)
            {
                InsnList listBefore = new InsnList();
                listBefore.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "fixGLState", getMethodDescriptor(Type.VOID_TYPE)));
                methodNode.instructions.insert(node, listBefore);
            }

            return valuepatchNodes.size() > 0;
        }

        return false;
    }
}
