/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.toolkit;

import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Created by lukas on 26.04.14.
 */
public class IvNodeMatcherSimple implements IvSingleNodeMatcher
{
    public int opCode;

    public IvNodeMatcherSimple(int opCode)
    {
        this.opCode = opCode;
    }

    @Override
    public boolean matchNode(AbstractInsnNode node)
    {
        return node.getOpcode() == opCode;
    }
}
