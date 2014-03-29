package net.ivorius.psychedelicraftcore;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by lukas on 12.03.14.
 */
public abstract class IvClassTransformerGeneral extends IvClassTransformer
{
    @Override
    public boolean transform(String className, ClassNode classNode, boolean obf)
    {
        boolean didChange = false;

        for (MethodNode m : classNode.methods)
        {
            if (transformMethod(className, m, obf))
            {
                didChange = true;
            }
        }

        return didChange;
    }

    public abstract boolean transformMethod(String className, MethodNode methodNode, boolean obf);
}
