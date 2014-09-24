/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by lukas on 24.09.14.
 */
public class PSOutboundCommuncationHandler
{
    public static void init()
    {
        if (Loader.isModLoaded("MineFactoryReloaded"))
        {
            registerMineFactoryCrop(PSBlocks.cannabisPlant, PSItems.cannabisSeeds, null);
            registerMineFactoryCrop(PSBlocks.cocaPlant, PSItems.cocaSeeds, null);
            registerMineFactoryCrop(PSBlocks.coffea, PSItems.coffeaCherries, null);
            registerMineFactoryCrop(PSBlocks.tobaccoPlant, PSItems.tobaccoSeeds, null);
        }
    }

    private static void registerMineFactoryCrop(Block crop, Item seed, Integer seedDamage)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("crop", id(crop));
        tag.setString("seed", id(seed));
        if (seedDamage != null) // accepted metadata of the 'seed' item (optional); defaults to OreDictionary.WILDCARD_VALUE
            tag.setInteger("meta", seedDamage);
        FMLInterModComms.sendMessage("MineFactoryReloaded", "registerPlantable_Crop", tag);
    }

    private static String id(Block block)
    {
        return Block.blockRegistry.getNameForObject(block);
    }

    private static String id(Item item)
    {
        return Item.itemRegistry.getNameForObject(item);
    }
}

