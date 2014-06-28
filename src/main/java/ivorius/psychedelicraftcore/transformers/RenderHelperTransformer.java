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

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * Created by lukas on 21.02.14.
 */
public class RenderHelperTransformer extends IvClassTransformerClass
{
    public RenderHelperTransformer(Logger logger)
    {
        super(logger);

        registerExpectedMethod("enableStandardItemLighting", "func_74519_b", getMethodDescriptor(Type.VOID_TYPE));
        registerExpectedMethod("disableStandardItemLighting", "func_74518_a", getMethodDescriptor(Type.VOID_TYPE));
    }

    @Override
    public boolean transformMethod(String className, String methodID, MethodNode methodNode, boolean obf)
    {
        if (methodID.equals("disableStandardItemLighting"))
        {
            InsnList list = new InsnList();
            list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "disableStandardItemLighting", getMethodDescriptor(Type.VOID_TYPE)));
            methodNode.instructions.insert(methodNode.instructions.get(0), list);

            return true;
        }
        if (methodID.equals("enableStandardItemLighting"))
        {
            InsnList list = new InsnList();
            list.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "enableStandardItemLighting", getMethodDescriptor(Type.VOID_TYPE)));
            methodNode.instructions.insert(methodNode.instructions.get(0), list);

            return true;
        }

        return false;
    }
}
