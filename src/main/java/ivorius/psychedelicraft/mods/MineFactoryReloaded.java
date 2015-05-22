/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.mods;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by lukas on 02.11.14.
 */
public class MineFactoryReloaded extends ModRepresentation
{
    public static final String MOD_ID = "MineFactoryReloaded";

    public static void registerPlantableCrop(Block crop, Item seed, Integer seedDamage)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("crop", id(crop));
        tag.setString("seed", id(seed));
        if (seedDamage != null) // accepted metadata of the 'seed' item (optional); defaults to OreDictionary.WILDCARD_VALUE
            tag.setInteger("meta", seedDamage);
        FMLInterModComms.sendMessage(MOD_ID, "registerPlantable_Crop", tag);
    }

    public static void registerPlantableSapling(Block sapling, Item seed)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("sapling", id(sapling));
        if (seed != null)
            tag.setString("seed", id(seed));
        FMLInterModComms.sendMessage(MOD_ID, "registerPlantable_Sapling", tag);
    }

    public static void registerHarvestableCrop(Block crop, int minMetadata)
    {
        FMLInterModComms.sendMessage(MOD_ID, "registerHarvestable_Crop", new ItemStack(crop, minMetadata));
    }

    public static void registerHarvestableTreeLeaves(Block block)
    {
        FMLInterModComms.sendMessage(MOD_ID, "registerHarvestable_Leaves", id(block));
    }

    public static void registerHarvestableLog(Block block)
    {
        FMLInterModComms.sendMessage(MOD_ID, "registerHarvestable_Log", id(block));
    }

    public static void registerFertilizableCrop(Block crop, int targetMeta, FertilizerType fertilizationType)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("plant", id(crop));
        tag.setInteger("meta", targetMeta);
        if (fertilizationType != null)
            tag.setInteger("type", fertilizationType.ordinal());
        FMLInterModComms.sendMessage(MOD_ID, "registerFertilizable_Crop", tag);
    }

    public static void registerFertilizableStandard(Block plant, FertilizerType fertilizationType)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("plant", id(plant));
        if (fertilizationType != null)
            tag.setInteger("type", fertilizationType.ordinal());
        FMLInterModComms.sendMessage(MOD_ID, "registerFertilizable_Standard", tag);
    }

    /**
     * Determines what kind of action a given fertilizer can perform. Your
     * IFactoryFertilizable instances should check this before performing any action
     * to maintain future compatibility.
     *
     * See https://github.com/skyboy/MineFactoryReloaded/blob/master/src/powercrystals/minefactoryreloaded/api/FertilizerType.java
     *
     * @author PowerCrystals
     */
    public enum FertilizerType
    {
        /**
         * The fertilizer will fertilize nothing.
         */
        None,
        /**
         * The fertilizer will fertilize grass.
         */
        Grass,
        /**
         * The fertilizer will grow a plant.
         */
        GrowPlant,
        /**
         * The fertilizer will grow magical crops.
         */
        GrowMagicalCrop,
    }
}
