/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.toolkit;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lukas on 26.04.14.
 */
public class IvMultiNodeMatcherSimple implements IvMultiNodeMatcher
{
    public List<IvSingleNodeMatcher> singleNodeMatchers;

    public IvMultiNodeMatcherSimple(List<IvSingleNodeMatcher> singleNodeMatchers)
    {
        this.singleNodeMatchers = singleNodeMatchers;
    }

    public IvMultiNodeMatcherSimple(IvSingleNodeMatcher... singleNodeMatchers)
    {
        this(Arrays.asList(singleNodeMatchers));
    }

    @Override
    public boolean matchFromNodeInList(InsnList nodes, AbstractInsnNode node)
    {
        for (int i = 0; i < singleNodeMatchers.size(); i++)
        {
            if (!singleNodeMatchers.get(i).matchNode(node))
            {
                return false;
            }

            node = node.getNext();
        }

        return true;
    }
}
