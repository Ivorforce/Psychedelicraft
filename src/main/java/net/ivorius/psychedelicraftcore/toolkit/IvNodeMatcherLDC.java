/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.toolkit;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * Created by lukas on 26.04.14.
 */
public class IvNodeMatcherLDC implements IvSingleNodeMatcher
{
    public String cst;

    public IvNodeMatcherLDC(String cst)
    {
        this.cst = cst;
    }

    @Override
    public boolean matchNode(AbstractInsnNode node)
    {
        if (node.getOpcode() != Opcodes.LDC)
        {
            return false;
        }

        LdcInsnNode ldcInsnNode = (LdcInsnNode) node;

        if (cst != null && !cst.equals(ldcInsnNode.cst))
        {
            return false;
        }

        return true;
    }
}
