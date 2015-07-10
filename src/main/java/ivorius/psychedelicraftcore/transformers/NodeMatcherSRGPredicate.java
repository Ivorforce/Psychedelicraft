package ivorius.psychedelicraftcore.transformers;

import com.google.common.base.Predicate;
import ivorius.ivtoolkit.asm.IvClassTransformer;
import ivorius.ivtoolkit.asm.IvSingleNodeMatcher;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * Created by lukas on 10.07.15.
 */
public class NodeMatcherSRGPredicate implements IvSingleNodeMatcher
{
    public Predicate<Integer> opCode;
    public Predicate<String> srgMethodName;
    public Predicate<String> owner;
    public Predicate<String> desc;

    public NodeMatcherSRGPredicate(Predicate<Integer> opCode, Predicate<String> srgMethodName, Predicate<String> owner, Predicate<String> desc)
    {
        this.opCode = opCode;
        this.srgMethodName = srgMethodName;
        this.owner = owner;
        this.desc = desc;
    }

    @Override
    public boolean matchNode(AbstractInsnNode node)
    {
        if (opCode != null && !opCode.apply(node.getOpcode()))
        {
            return false;
        }

        MethodInsnNode methodInsnNode = (MethodInsnNode) node;

        if (srgMethodName != null && !srgMethodName.apply(IvClassTransformer.getSrgName(methodInsnNode)))
        {
            return false;
        }

        if (owner != null && !owner.apply(IvClassTransformer.getSrgClassName(methodInsnNode.owner)))
        {
            return false;
        }

        if (desc != null && !desc.apply(IvClassTransformer.getSRGDescriptor(methodInsnNode.desc)))
        {
            return false;
        }

        return true;
    }
}
