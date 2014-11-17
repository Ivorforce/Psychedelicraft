/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore.transformers;

import ivorius.ivtoolkit.asm.IvClassTransformerClass;
import ivorius.ivtoolkit.asm.IvNodeFinder;
import ivorius.ivtoolkit.asm.IvNodeMatcherLDC;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

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
        switch (methodID)
        {
            case "renderEntities":
                AbstractInsnNode entitiesNode = IvNodeFinder.findNode(new IvNodeMatcherLDC("entities"), methodNode);
                entitiesNode = entitiesNode.getNext();

                if (entitiesNode != null)
                {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(FLOAD, 3));
                    list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "renderEntities", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE), false));
                    methodNode.instructions.insert(entitiesNode, list);

                    return true;
                }
                break;
        }

        return false;
    }
}
