/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.ivToolkit;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

/**
 * Created by lukas on 26.04.14.
 */
public class IvNodeMatcherFieldSRG implements IvSingleNodeMatcher
{
    public int opCode;
    public String srgFieldName;
    public String owner;
    public Type type;

    public IvNodeMatcherFieldSRG(int opCode, String srgFieldName, String owner, Type type)
    {
        this.opCode = opCode;
        this.srgFieldName = srgFieldName;
        this.owner = owner;
        this.type = type;
    }

    @Override
    public boolean matchNode(AbstractInsnNode node)
    {
        if (node.getOpcode() != opCode)
        {
            return false;
        }

        FieldInsnNode fieldInsnNode = (FieldInsnNode) node;

        if (srgFieldName != null && !srgFieldName.equals(IvClassTransformer.getSrgName(fieldInsnNode)))
        {
            return false;
        }

        if (owner != null && !owner.equals(IvClassTransformer.getSrgClassName(fieldInsnNode.owner)))
        {
            return false;
        }

        if (type != null && !type.equals(Type.getType(fieldInsnNode.desc)))
        {
            return false;
        }

        return true;
    }
}
