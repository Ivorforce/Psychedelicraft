/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.mods;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Created by lukas on 02.11.14.
 */
public class ModRepresentation
{
    public static String id(Block block)
    {
        return Block.blockRegistry.getNameForObject(block);
    }

    public static String id(Item item)
    {
        return Item.itemRegistry.getNameForObject(item);
    }
}
