/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.worldgen;

import cpw.mods.fml.common.registry.GameRegistry;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.items.PSItems;
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

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.cannabisPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.04f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.tobaccoPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.04f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.coffea),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.05f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, true, PSBlocks.cocaPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.river, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.beach, 0.4f, 5)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenPeyote(false),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.desert, 0.01f, 4),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.extremeHills, 0.02f, 4),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.jungleHills, 0.01f, 4)), 10);

        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.wineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.glassChalice, 0, 1, 4, 5));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.woodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.juniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(PSItems.driedTobacco, 0, 1, 16, 5));

        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.wineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.woodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.juniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.driedTobacco, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.pipe, 0, 1, 1, 3));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(PSItems.cigarette, 0, 1, 8, 5));

        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.wineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.juniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.woodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.cigarette, 0, 1, 16, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.cigar, 0, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.joint, 0, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.coldCoffee, 0, 1, 4, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.syringe, PSItems.syringeCocaineDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.syringe, PSItems.syringeCaffeineDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.woodenBowlDrug, PSItems.woodenBowlPeyoteDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(PSItems.hashMuffin, 0, 1, 8, 1));
    }
}
