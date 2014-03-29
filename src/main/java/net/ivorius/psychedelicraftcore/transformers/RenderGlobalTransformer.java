package net.ivorius.psychedelicraftcore.transformers;

import net.ivorius.psychedelicraftcore.IvClassTransformerClass;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by lukas on 21.02.14.
 */
public class RenderGlobalTransformer extends IvClassTransformerClass
{
    public RenderGlobalTransformer()
    {
        registerExpectedMethod("renderEntities", "func_147589_a", getMethodDescriptor(Type.VOID_TYPE, "net/minecraft/entity/EntityLivingBase", "net/minecraft/client/renderer/culling/ICamera", Type.FLOAT_TYPE));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        if (methodID.equals("renderEntities"))
        {
            AbstractInsnNode currentNode;
            AbstractInsnNode entitiesNode = null;

            Iterator<AbstractInsnNode> methodNodeIterator = methodNode.instructions.iterator();

            while (methodNodeIterator.hasNext())
            {
                currentNode = methodNodeIterator.next();

                if (currentNode.getOpcode() == LDC && ((LdcInsnNode) currentNode).cst.equals("entities"))
                {
                    entitiesNode = currentNode.getNext();
                }
            }

            if (entitiesNode != null)
            {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(FLOAD, 3));
                list.add(new MethodInsnNode(INVOKESTATIC, "net/ivorius/psychedelicraftcore/PsycheCoreBusClient", "renderEntities", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE)));
                methodNode.instructions.insert(entitiesNode, list);

                return true;
            }
        }

        return false;
    }
}
