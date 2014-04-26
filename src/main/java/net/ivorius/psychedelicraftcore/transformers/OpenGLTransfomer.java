package net.ivorius.psychedelicraftcore.transformers;

import net.ivorius.psychedelicraftcore.ivToolkit.IvClassTransformerGeneral;
import net.ivorius.psychedelicraftcore.ivToolkit.IvASMHelper;
import net.ivorius.psychedelicraftcore.ivToolkit.IvNodeMatcherMethodSRG;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by lukas on 12.03.14.
 */
public class OpenGLTransfomer extends IvClassTransformerGeneral
{
    public OpenGLTransfomer(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean transformMethod(String className, MethodNode methodNode, boolean obf)
    {
        int transformed = 0;

        transformed += proxyGLSwitchMethods(methodNode);

        return transformed > 0;
    }

    public static int proxyGLSwitchMethods(MethodNode methodNode)
    {
        int caught = 0;

        List<AbstractInsnNode> glEnableNodes = IvASMHelper.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glEnable", "org/lwjgl/opengl/GL11", null), methodNode);
        List<AbstractInsnNode> glDisableNodes = IvASMHelper.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glDisable", "org/lwjgl/opengl/GL11", null), methodNode);

        for (AbstractInsnNode callListNode : glEnableNodes)
        {
            InsnList listBefore = new InsnList();
            listBefore.add(new InsnNode(DUP));
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLEnable", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        for (AbstractInsnNode callListNode : glDisableNodes)
        {
            InsnList listBefore = new InsnList();
            listBefore.add(new InsnNode(DUP));
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLDisable", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        return caught;
    }

    public static int proxyGLMatrixMethods(MethodNode methodNode)
    {
        int caught = 0;

        List<AbstractInsnNode> glTranslatefNodes = IvASMHelper.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glTranslatef", "org/lwjgl/opengl/GL11", null), methodNode);
        List<AbstractInsnNode> glRotatefNodes = IvASMHelper.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glRotatef", "org/lwjgl/opengl/GL11", null), methodNode);
        List<AbstractInsnNode> glScalefNodes = IvASMHelper.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glScalef", "org/lwjgl/opengl/GL11", null), methodNode);

        for (AbstractInsnNode callListNode : glTranslatefNodes)
        {
            InsnList listBefore = new InsnList();
            insertDUP3(listBefore);
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLTranslatef", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        for (AbstractInsnNode callListNode : glRotatefNodes)
        {
            InsnList listBefore = new InsnList();
            insertDUP4(listBefore);
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLRotatef", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        for (AbstractInsnNode callListNode : glScalefNodes)
        {
            InsnList listBefore = new InsnList();
            insertDUP3(listBefore);
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLScalef", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        return caught;
    }

    public static void insertDUP3(InsnList list)
    {
        list.add(new InsnNode(DUP2_X1));    // 1 2 3 1 2
        list.add(new InsnNode(POP2));       // 3 1 2
        list.add(new InsnNode(DUP_X2));     // 3 1 2 3
        list.add(new InsnNode(DUP_X2));     // 3 1 2 3 3
        list.add(new InsnNode(POP));        // 1 2 3 3
        list.add(new InsnNode(DUP2_X1));    // 1 2 3 1 2 3
    }

    public static void insertDUP4(InsnList list)
    {
        list.add(new InsnNode(DUP2_X2));    // 1 2 3 4 1 2
        list.add(new InsnNode(POP2));       // 3 4 1 2
        list.add(new InsnNode(DUP2_X2));    // 3 4 1 2 3 4
        list.add(new InsnNode(DUP2_X2));    // 3 4 1 2 3 4 3 4
        list.add(new InsnNode(POP2));       // 1 2 3 4 3 4
        list.add(new InsnNode(DUP2_X2));    // 1 2 3 4 1 2 3 4
    }
}
