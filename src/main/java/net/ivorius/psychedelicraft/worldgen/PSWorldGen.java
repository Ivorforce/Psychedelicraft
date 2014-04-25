/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.worldgen;

import cpw.mods.fml.common.registry.GameRegistry;
import net.ivorius.psychedelicraft.blocks.PSBlocks;
import net.ivorius.psychedelicraft.items.PSItems;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ChestGenHooks;

/**
 * Created by lukas on 25.04.14.
 */
public class PSWorldGen
{
    public static void initWorldGen()
    {
        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenJuniperTrees(false),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.extremeHills, 0.1f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.taiga, 0.1f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.icePlains, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.coldTaiga, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.coldTaigaHills, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.taigaHills, 0.1f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.iceMountains, 0.05f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.blockCannabisPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.04f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.blockTobaccoPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.04f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.blockCoffea),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.05f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, true, PSBlocks.blockCocaPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.river, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.beach, 0.4f, 5)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenPeyote(false),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.desert, 0.01f, 4),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.extremeHills, 0.02f, 4),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.jungleHills, 0.01f, 4)), 10);

        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.itemWineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.itemGlassChalice, 0, 1, 4, 5));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.itemWoodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.itemJuniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.itemDriedTobacco, 0, 1, 16, 5));

        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.itemWineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.itemWoodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.itemJuniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.itemDriedTobacco, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.itemPipe, 0, 1, 1, 3));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.itemCigarette, 0, 1, 8, 5));

        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemWineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemJuniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemWoodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemCigarette, 0, 1, 16, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemCigar, 0, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemJoint, 0, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemColdCoffee, 0, 1, 4, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemSyringe, PSItems.itemSyringeCocaineDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemSyringe, PSItems.itemSyringeCaffeineDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemWoodenBowlDrug, PSItems.itemWoodenBowlPeyoteDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.itemHashMuffin, 0, 1, 8, 1));
    }
}
