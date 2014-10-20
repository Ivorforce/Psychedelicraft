/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.worldgen;

import ivorius.psychedelicraft.PSConfig;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.items.DrinkInformation;
import ivorius.psychedelicraft.items.ItemSyringe;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;

import static cpw.mods.fml.common.registry.GameRegistry.registerWorldGenerator;
import static ivorius.psychedelicraft.items.PSItems.*;
import static ivorius.psychedelicraft.worldgen.GeneratorGeneric.EntryDefault;
import static net.minecraftforge.common.ChestGenHooks.*;

/**
 * Created by lukas on 25.04.14.
 */
public class PSWorldGen
{
    public static void initWorldGen()
    {
        if (PSConfig.genJuniper)
            registerWorldGenerator(new GeneratorGeneric(new WorldGenJuniperTrees(false),
                    new EntryDefault(BiomeGenBase.extremeHills, 0.1f),
                    new EntryDefault(BiomeGenBase.taiga, 0.1f),
                    new EntryDefault(BiomeGenBase.icePlains, 0.05f),
                    new EntryDefault(BiomeGenBase.coldTaiga, 0.05f),
                    new EntryDefault(BiomeGenBase.coldTaigaHills, 0.05f),
                    new EntryDefault(BiomeGenBase.taigaHills, 0.1f),
                    new EntryDefault(BiomeGenBase.iceMountains, 0.05f)), 10);

        if (PSConfig.genCannabis)
            registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.cannabisPlant),
                    new EntryDefault(BiomeGenBase.plains, 0.04f),
                    new EntryDefault(BiomeGenBase.forest, 0.04f),
                    new EntryDefault(BiomeGenBase.savanna, 0.04f)), 10);

        if (PSConfig.genTobacco)
            registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.tobaccoPlant),
                    new EntryDefault(BiomeGenBase.plains, 0.04f),
                    new EntryDefault(BiomeGenBase.forest, 0.04f),
                    new EntryDefault(BiomeGenBase.savanna, 0.04f)), 10);

        if (PSConfig.genCoffea)
            registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, PSBlocks.coffea),
                    new EntryDefault(BiomeGenBase.plains, 0.05f),
                    new EntryDefault(BiomeGenBase.forest, 0.05f),
                    new EntryDefault(BiomeGenBase.savanna, 0.05f)), 10);

        if (PSConfig.genCoca)
            registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, true, PSBlocks.cocaPlant),
                    new EntryDefault(BiomeGenBase.plains, 0.4f, 5),
                    new EntryDefault(BiomeGenBase.forest, 0.4f, 5),
                    new EntryDefault(BiomeGenBase.river, 0.4f, 5),
                    new EntryDefault(BiomeGenBase.beach, 0.4f, 5)), 10);

        if (PSConfig.genPeyote)
            registerWorldGenerator(new GeneratorGeneric(new WorldGenPeyote(false),
                    new EntryDefault(BiomeGenBase.desert, 0.01f, 4),
                    new EntryDefault(BiomeGenBase.extremeHills, 0.02f, 4),
                    new EntryDefault(BiomeGenBase.jungleHills, 0.01f, 4)), 10);

        if (PSConfig.dungeonChests)
        {
            addItem(DUNGEON_CHEST, new WeightedRandomChestContent(wineGrapes, 0, 1, 8, 10));
            addItem(DUNGEON_CHEST, new WeightedRandomChestContent(glassChalice, 0, 1, 4, 5));
            addItem(DUNGEON_CHEST, new WeightedRandomChestContent(woodenMug, 0, 1, 16, 5));
            addItem(DUNGEON_CHEST, new WeightedRandomChestContent(juniperBerries, 0, 1, 8, 10));
            addItem(DUNGEON_CHEST, new WeightedRandomChestContent(driedTobacco, 0, 1, 16, 5));

            addItem(MINESHAFT_CORRIDOR, new WeightedRandomChestContent(wineGrapes, 0, 1, 8, 10));
            addItem(MINESHAFT_CORRIDOR, new WeightedRandomChestContent(woodenMug, 0, 1, 16, 5));
            addItem(MINESHAFT_CORRIDOR, new WeightedRandomChestContent(juniperBerries, 0, 1, 8, 10));
            addItem(MINESHAFT_CORRIDOR, new WeightedRandomChestContent(driedTobacco, 0, 1, 16, 5));
            addItem(MINESHAFT_CORRIDOR, new WeightedRandomChestContent(pipe, 0, 1, 1, 3));
            addItem(MINESHAFT_CORRIDOR, new WeightedRandomChestContent(cigarette, 0, 1, 8, 5));
        }

        if (PSConfig.villageChests)
        {
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(wineGrapes, 0, 1, 8, 10));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(juniperBerries, 0, 1, 8, 10));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(woodenMug, 0, 1, 16, 5));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(cigarette, 0, 1, 16, 1));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(cigar, 0, 1, 1, 1));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(joint, 0, 1, 1, 1));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(woodenMug.createDrinkStack(1, new DrinkInformation("coldCoffee", 1)), 1, 4, 1));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(woodenBowlDrug.createDrinkStack(1, new DrinkInformation("peyote", 1)), 1, 1, 1));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(syringe, ItemSyringe.damageCocaine, 1, 1, 1));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(syringe, ItemSyringe.damageCaffeine, 1, 1, 1));
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(hashMuffin, 0, 1, 8, 1));
        }
    }
}
