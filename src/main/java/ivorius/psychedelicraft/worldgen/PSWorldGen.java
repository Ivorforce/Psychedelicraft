/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.worldgen;

import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.fluids.PSFluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.FluidStack;

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

            ItemStack coffeeStack = new ItemStack(woodenMug);
            woodenMug.fill(coffeeStack, new FluidStack(PSFluids.coffee, 1), true);
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(coffeeStack, 1, 4, 1));

            ItemStack peyoteStack = new ItemStack(woodenBowlDrug);
            woodenBowlDrug.fill(coffeeStack, new FluidStack(PSFluids.peyoteJuice, 1), true);
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(peyoteStack, 1, 1, 1));

            ItemStack cocaineStack = new ItemStack(syringe);
            syringe.fill(coffeeStack, new FluidStack(PSFluids.cocaineFluid, 1), true);
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(cocaineStack, 1, 1, 1));

            ItemStack caffeineStack = new ItemStack(syringe);
            syringe.fill(coffeeStack, new FluidStack(PSFluids.caffeineFluid, 1), true);
            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(caffeineStack, 1, 1, 1));

            addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(hashMuffin, 0, 1, 8, 1));
        }
    }
}
