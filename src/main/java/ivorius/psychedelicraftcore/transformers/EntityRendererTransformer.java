/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore.transformers;

import ivorius.ivtoolkit.asm.*;
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
        registerExpectedMethod("setupCameraTransform", "func_78479_a", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.INT_TYPE));
        registerExpectedMethod("preRenderSky", "func_78471_a", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        switch (methodID)
        {
            case "updateCameraAndRender":
                AbstractInsnNode preNode = IvNodeFinder.findNode(new IvNodeMatcherLDC("level"), methodNode);
                AbstractInsnNode postNode = IvNodeFinder.findNode(new IvNodeMatcherFieldSRG(GETSTATIC, "field_148824_g" /* shadersSupported */, "net/minecraft/client/renderer/OpenGlHelper", Type.BOOLEAN_TYPE), methodNode);

                if (preNode == null)
                {
                    printSubMethodError(className, methodID, "pre");
                }
                else
                {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(FLOAD, 1));
                    list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "preWorldRender", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE), false));
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
                    list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "postWorldRender", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE), false));
                    methodNode.instructions.insertBefore(postNode, list);
                }

                return true;
            case "orientCamera":
            {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(FLOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "orientCamera", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE), false));
                methodNode.instructions.insert(methodNode.instructions.get(0), list);

                return true;
            }
            case "enableLightmap":
            {
                InsnList list = new InsnList();
                list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "enableLightmap", getMethodDescriptor(Type.VOID_TYPE), false));
                methodNode.instructions.insert(methodNode.instructions.get(0), list);

                return true;
            }
            case "disableLightmap":
            {
                InsnList list = new InsnList();
                list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "enableLightmap", getMethodDescriptor(Type.VOID_TYPE), false));
                methodNode.instructions.insert(methodNode.instructions.get(0), list);

                return true;
            }
            case "renderHand":
                AbstractInsnNode transformMatrixNode = IvNodeFinder.findNode(new IvNodeMatcherMethod(INVOKESTATIC, "glPushMatrix", "org/lwjgl/opengl/GL11", null), methodNode);
                AbstractInsnNode skipOverlayNode = IvNodeFinder.findNode(new IvNodeMatcherMethodSRG(INVOKEVIRTUAL, "func_78447_b" /* renderOverlays */, "net/minecraft/client/renderer/ItemRenderer", Type.VOID_TYPE, Type.FLOAT_TYPE), methodNode);

                if (transformMatrixNode == null)
                {
                    printSubMethodError(className, methodID, "renderHeldItem");
                }
                else
                {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(FLOAD, 1));
                    list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "renderHeldItem", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE), false));
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
                    preList.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "renderBlockOverlay", getMethodDescriptor(Type.BOOLEAN_TYPE, Type.FLOAT_TYPE), false));
                    preList.add(new JumpInsnNode(IFNE, skipRenderOverlayNode));
                    methodNode.instructions.insertBefore(skipOverlayNode.getPrevious().getPrevious().getPrevious(), preList);

                    InsnList postList = new InsnList();
                    methodNode.instructions.insert(skipOverlayNode, postList);
                }

                return true;
            case "renderWorld":
                AbstractInsnNode transformNode = IvNodeFinder.findNode(new IvNodeMatcherMethod(INVOKESPECIAL, "func_78476_b" /* renderHand */, "net/minecraft/client/renderer/EntityRenderer", Type.VOID_TYPE, Type.FLOAT_TYPE, Type.INT_TYPE), methodNode);

                if (transformNode != null)
                {
                    AbstractInsnNode glClearNode = transformNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious();

                    if (glClearNode.getOpcode() == INVOKESTATIC && ((MethodInsnNode) glClearNode).name.equals("glClear") && ((MethodInsnNode) glClearNode).owner.equals("org/lwjgl/opengl/GL11"))
                    {
                        LabelNode skipGLClearNode = new LabelNode();
                        methodNode.instructions.insert(glClearNode, skipGLClearNode);

                        InsnList preList = new InsnList();
                        preList.add(new VarInsnNode(FLOAD, 1));
                        preList.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "preRenderHand", getMethodDescriptor(Type.BOOLEAN_TYPE, Type.FLOAT_TYPE), false));
                        preList.add(new JumpInsnNode(IFNE, skipGLClearNode));
                        methodNode.instructions.insertBefore(glClearNode.getPrevious(), preList);

                        InsnList postList = new InsnList();
                        postList.add(new VarInsnNode(FLOAD, 1));
                        postList.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "postRenderHand", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE), false));
                        methodNode.instructions.insert(transformNode, postList);

                        return true;
                    }
                }
                break;
            case "setupFog":
                List<AbstractInsnNode> glFogiNodes = IvNodeFinder.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glFogi", "org/lwjgl/opengl/GL11", null), methodNode);

                for (AbstractInsnNode callListNode : glFogiNodes)
                {
                    InsnList listBefore = new InsnList();
                    listBefore.add(new InsnNode(DUP2));
                    listBefore.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLFogi", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE, Type.INT_TYPE), false));
                    methodNode.instructions.insertBefore(callListNode, listBefore);
                }

                return true;
            case "setupCameraTransform":
            {
                LabelNode realMethodStartNode = new LabelNode();

                InsnList list = new InsnList();
                list.add(new VarInsnNode(FLOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "setupCameraTransform", getMethodDescriptor(Type.BOOLEAN_TYPE, Type.FLOAT_TYPE), false));
                list.add(new JumpInsnNode(IFEQ, realMethodStartNode));
                list.add(new InsnNode(RETURN));
                list.add(realMethodStartNode);
                methodNode.instructions.insert(methodNode.instructions.get(0), list);

                return true;
            }
            case "renderWorldAdditions":
                List<AbstractInsnNode> valuepatchNodes = new ArrayList<>();
                valuepatchNodes.addAll(IvNodeFinder.findNodes(new IvNodeMatcherLDC("prepareterrain"), methodNode));
                valuepatchNodes.addAll(IvNodeFinder.findNodes(new IvNodeMatcherLDC("water"), methodNode));
                valuepatchNodes.addAll(IvNodeFinder.findNodes(new IvNodeMatcherLDC("entities"), methodNode));

                for (AbstractInsnNode node : valuepatchNodes)
                {
                    InsnList listBefore = new InsnList();
                    listBefore.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "fixGLState", getMethodDescriptor(Type.VOID_TYPE), false));
                    methodNode.instructions.insert(node, listBefore);
                }

                return valuepatchNodes.size() > 0;
            case "preRenderSky":
                AbstractInsnNode preRenderSkyNode = IvNodeFinder.findNode(new IvNodeMatcherMethodSRG(INVOKESTATIC, "func_78558_a", "net/minecraft/client/renderer/culling/ClippingHelperImpl", getMethodDescriptor("net/minecraft/client/renderer/culling/ClippingHelper")), methodNode);

                if (preRenderSkyNode != null)
                {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(FLOAD, 1));
                    list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "preRenderSky", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE), false));
                    methodNode.instructions.insert(preRenderSkyNode.getNext(), list);

                    return true;
                }
                break;
        }

        return false;
    }
}
