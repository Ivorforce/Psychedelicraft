/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.ivToolkit;

import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Created by lukas on 26.04.14.
 */
public interface IvSingleNodeMatcher
{
    public boolean matchNode(AbstractInsnNode node);
}
