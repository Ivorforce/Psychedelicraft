package net.ivorius.psychedelicraftcore.transformers;

import net.ivorius.psychedelicraftcore.toolkit.IvClassTransformerClass;
import net.ivorius.psychedelicraftcore.toolkit.IvASMHelper;
import net.ivorius.psychedelicraftcore.toolkit.IvNodeMatcherLDC;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by lukas on 21.02.14.
 */
public class RenderGlobalTransformer extends IvClassTransformerClass
{
    public RenderGlobalTransformer(Logger logger)
    {
        super(logger);

        registerExpectedMethod("renderEntities", "func_147589_a", getMethodDescriptor(Type.VOID_TYPE, "net/minecraft/entity/EntityLivingBase", "net/minecraft/client/renderer/culling/ICamera", Type.FLOAT_TYPE));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        if (methodID.equals("renderEntities"))
        {
            AbstractInsnNode entitiesNode = IvASMHelper.findNode(new IvNodeMatcherLDC("entities"), methodNode);
            entitiesNode = entitiesNode.getNext();

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
