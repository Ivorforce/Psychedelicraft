/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.toolkit;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukas on 26.04.14.
 */
public class IvASMHelper
{
    public static AbstractInsnNode findNode(IvSingleNodeMatcher matcher, MethodNode methodNode)
    {
        return findNode(matcher, methodNode.instructions);
    }

    public static AbstractInsnNode findNode(IvSingleNodeMatcher matcher, InsnList insnList)
    {
        for (int i = 0; i < insnList.size(); i++)
        {
            AbstractInsnNode node = insnList.get(i);

            if (matcher.matchNode(node))
            {
                return node;
            }
        }

        return null;
    }

    public static AbstractInsnNode findNodeList(IvMultiNodeMatcher matcher, MethodNode methodNode)
    {
        return findNodeList(matcher, methodNode.instructions);
    }

    public static AbstractInsnNode findNodeList(IvMultiNodeMatcher matcher, InsnList insnList)
    {
        for (int i = 0; i < insnList.size(); i++)
        {
            AbstractInsnNode node = insnList.get(i);

            if (matcher.matchFromNodeInList(insnList, node))
            {
                return node;
            }
        }

        return null;
    }

    public static List<AbstractInsnNode> findNodes(IvSingleNodeMatcher matcher, MethodNode methodNode)
    {
        return findNodes(matcher, methodNode.instructions);
    }

    public static List<AbstractInsnNode> findNodes(IvSingleNodeMatcher matcher, InsnList insnList)
    {
        List<AbstractInsnNode> nodes = new ArrayList<AbstractInsnNode>();

        for (int i = 0; i < insnList.size(); i++)
        {
            AbstractInsnNode node = insnList.get(i);

            if (matcher.matchNode(node))
            {
                nodes.add(node);
            }
        }

        return nodes;
    }

    public static List<AbstractInsnNode> findNodeLists(IvMultiNodeMatcher matcher, MethodNode methodNode)
    {
        return findNodeLists(matcher, methodNode.instructions);
    }

    public static List<AbstractInsnNode> findNodeLists(IvMultiNodeMatcher matcher, InsnList insnList)
    {
        List<AbstractInsnNode> nodes = new ArrayList<AbstractInsnNode>();

        for (int i = 0; i < insnList.size(); i++)
        {
            AbstractInsnNode node = insnList.get(i);

            if (matcher.matchFromNodeInList(insnList, node))
            {
                nodes.add(node);
            }
        }

        return nodes;
    }
}
