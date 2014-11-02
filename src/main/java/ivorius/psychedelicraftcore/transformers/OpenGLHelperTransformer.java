/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore.transformers;

import ivorius.ivtoolkit.asm.IvClassTransformerClass;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * Created by lukas on 21.02.14.
 */
public class OpenGLHelperTransformer extends IvClassTransformerClass
{
    public OpenGLHelperTransformer(Logger logger)
    {
        super(logger);

        registerExpectedMethod("glBlendFunc", "func_148821_a", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE));
        registerExpectedMethod("setActiveTexture", "func_77473_a", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        switch (methodID)
        {
            case "glBlendFunc":
            {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ILOAD, 0));
                list.add(new VarInsnNode(ILOAD, 1));
                list.add(new VarInsnNode(ILOAD, 2));
                list.add(new VarInsnNode(ILOAD, 3));
                list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLBlendFunc", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE), false));
                methodNode.instructions.insert(methodNode.instructions.get(0), list);

                return true;
            }
            case "setActiveTexture":
            {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ILOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLActiveTexture", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE), false));
                methodNode.instructions.insert(methodNode.instructions.get(0), list);

                return true;
            }
        }

        return false;
    }
}
