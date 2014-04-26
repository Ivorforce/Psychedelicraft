/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.toolkit;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * Created by lukas on 26.04.14.
 */
public interface IvMultiNodeMatcher
{
    public boolean matchFromNodeInList(InsnList nodes, AbstractInsnNode node);
}
