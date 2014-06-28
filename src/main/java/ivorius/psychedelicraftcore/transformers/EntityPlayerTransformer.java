/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore.transformers;

import ivorius.psychedelicraftcore.ivtoolkit.IvClassTransformerClass;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by lukas on 21.02.14.
 */
public class EntityPlayerTransformer extends IvClassTransformerClass
{
    public EntityPlayerTransformer(Logger logger)
    {
        super(logger);
        registerExpectedMethod("wakeUpPlayer", "func_70999_a", getMethodDescriptor(Type.VOID_TYPE, Type.BOOLEAN_TYPE, Type.BOOLEAN_TYPE, Type.BOOLEAN_TYPE));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        if (methodID.equals("wakeUpPlayer"))
        {
            InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new VarInsnNode(ILOAD, 1));
            list.add(new VarInsnNode(ILOAD, 2));
            list.add(new VarInsnNode(ILOAD, 3));
            list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusCommon", "wakeUpPlayer", getMethodDescriptor(Type.VOID_TYPE, "net/minecraft/entity/player/EntityPlayer", Type.BOOLEAN_TYPE, Type.BOOLEAN_TYPE, Type.BOOLEAN_TYPE)));
            methodNode.instructions.insert(methodNode.instructions.get(0), list);

            return true;
        }

        return false;
    }
}
