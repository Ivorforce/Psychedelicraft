/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.entities;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.ivorius.psychedelicraft.Psychedelicraft;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by lukas on 25.04.14.
 */
public class PSEntityList
{
    public static final int molotovCocktailID = 0;
    public static final int realityRiftID = 1;

    public static int villagerDealerProfessionID;

    public static void preInit(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        //----------------------------------------------------------Molotov Cocktail----------------------------------

        EntityRegistry.registerModEntity(EntityMolotovCocktail.class, "molotovCocktail", molotovCocktailID, mod, 64, 10, true);

        //----------------------------------------------------------Digital----------------------------------

        EntityRegistry.registerModEntity(EntityRealityRift.class, "realityRift", realityRiftID, mod, 80, 3, false);

        //----------------------------------------------------------Villager Dealer----------------------------------

        VillagerRegistry.instance().registerVillagerId(PSEntityList.villagerDealerProfessionID);

        VillagerRegistry.instance().registerVillageTradeHandler(PSEntityList.villagerDealerProfessionID, new VillagerTradeHandlerDrugDealer());
        VillagerRegistry.instance().registerVillageTradeHandler(0, new VillagerTradeHandlerFarmer());
    }

    public static void preInitEnd(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {

    }
}
