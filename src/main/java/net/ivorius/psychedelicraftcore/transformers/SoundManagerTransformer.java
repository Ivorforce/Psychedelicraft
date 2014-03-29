package net.ivorius.psychedelicraftcore.transformers;

import net.ivorius.psychedelicraftcore.IvClassTransformerClass;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by lukas on 21.02.14.
 */
public class SoundManagerTransformer extends IvClassTransformerClass
{
    public SoundManagerTransformer()
    {
        registerExpectedMethod("getNormalizedVolume", "func_148594_a", getMethodDescriptor(Type.FLOAT_TYPE, "net/minecraft/client/audio/ISound", "net/minecraft/client/audio/SoundPoolEntry", "net/minecraft/client/audio/SoundCategory"));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        if (methodID.equals("getNormalizedVolume"))
        {
            AbstractInsnNode currentNode;
            AbstractInsnNode returnNode = null;

            Iterator<AbstractInsnNode> methodNodeIterator = methodNode.instructions.iterator();

            while (methodNodeIterator.hasNext())
            {
                currentNode = methodNodeIterator.next();

                if (currentNode.getOpcode() == FRETURN)
                {
                    returnNode = currentNode;
                }
            }

            if (returnNode != null)
            {
                InsnList list = new InsnList();
//            list.add(new InsnNode(DUP)); // Already on top of stack :>
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new VarInsnNode(ALOAD, 3));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "getSoundVolume", getMethodDescriptor(Type.FLOAT_TYPE, Type.FLOAT_TYPE, "net/minecraft/client/audio/ISound", "net/minecraft/client/audio/SoundPoolEntry", "net/minecraft/client/audio/SoundCategory", "net/minecraft/client/audio/SoundManager")));
                methodNode.instructions.insertBefore(returnNode, list);

                return true;
            }
        }

        return false;
    }
}
