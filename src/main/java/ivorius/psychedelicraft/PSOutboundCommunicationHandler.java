/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import cpw.mods.fml.common.Loader;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.items.PSItems;
import ivorius.psychedelicraft.mods.MineFactoryReloaded;

/**
 * Created by lukas on 24.09.14.
 */
public class PSOutboundCommunicationHandler
{
    public static void init()
    {
        if (Loader.isModLoaded(MineFactoryReloaded.MOD_ID))
        {
            MineFactoryReloaded.registerPlantableCrop(PSBlocks.cannabisPlant, PSItems.cannabisSeeds, null);
            MineFactoryReloaded.registerPlantableCrop(PSBlocks.cocaPlant, PSItems.cocaSeeds, null);
            MineFactoryReloaded.registerPlantableCrop(PSBlocks.coffea, PSItems.coffeaCherries, null);
            MineFactoryReloaded.registerPlantableCrop(PSBlocks.tobaccoPlant, PSItems.tobaccoSeeds, null);
            MineFactoryReloaded.registerPlantableSapling(PSBlocks.psycheSapling, null);

            MineFactoryReloaded.registerHarvestableCrop(PSBlocks.cannabisPlant, 15);
            MineFactoryReloaded.registerHarvestableCrop(PSBlocks.cocaPlant, 11);
            MineFactoryReloaded.registerHarvestableCrop(PSBlocks.coffea, 14);
            MineFactoryReloaded.registerHarvestableCrop(PSBlocks.tobaccoPlant, 14);
            MineFactoryReloaded.registerHarvestableTreeLeaves(PSBlocks.psycheLeaves);
            MineFactoryReloaded.registerHarvestableLog(PSBlocks.psycheLog);

            MineFactoryReloaded.registerFertilizableCrop(PSBlocks.cannabisPlant, 15, null);
            MineFactoryReloaded.registerFertilizableCrop(PSBlocks.cocaPlant, 11, null);
            MineFactoryReloaded.registerFertilizableCrop(PSBlocks.coffea, 14, null);
            MineFactoryReloaded.registerFertilizableCrop(PSBlocks.tobaccoPlant, 14, null);
            MineFactoryReloaded.registerFertilizableStandard(PSBlocks.psycheSapling, null);
        }
    }
}

