/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore.transformers;

import ivorius.ivtoolkit.asm.IvClassTransformerClass;
import ivorius.ivtoolkit.asm.IvNodeFinder;
import ivorius.ivtoolkit.asm.IvNodeMatcherSimple;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by lukas on 21.02.14.
 */
public class SoundManagerTransformer extends IvClassTransformerClass
{
    public SoundManagerTransformer(Logger logger)
    {
        super(logger);

        registerExpectedMethod("getNormalizedVolume", "func_148594_a", getMethodDescriptor(Type.FLOAT_TYPE, "net/minecraft/client/audio/ISound", "net/minecraft/client/audio/SoundPoolEntry", "net/minecraft/client/audio/SoundCategory"));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        if (methodID.equals("getNormalizedVolume"))
        {
            AbstractInsnNode returnNode = IvNodeFinder.findNode(new IvNodeMatcherSimple(FRETURN), methodNode);

            if (returnNode != null)
            {
                InsnList list = new InsnList();
//                list.add(new InsnNode(DUP)); // Already on top of stack :>
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new VarInsnNode(ALOAD, 3));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "getSoundVolume", getMethodDescriptor(Type.FLOAT_TYPE, Type.FLOAT_TYPE, "net/minecraft/client/audio/ISound", "net/minecraft/client/audio/SoundPoolEntry", "net/minecraft/client/audio/SoundCategory", "net/minecraft/client/audio/SoundManager"), false));
                methodNode.instructions.insertBefore(returnNode, list);

                return true;
            }
        }

        return false;
    }
}
