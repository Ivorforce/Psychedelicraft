/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import cpw.mods.fml.common.registry.VillagerRegistry;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

/**
 * Created by lukas on 23.02.14.
 */
public class VillagerTradeHandlerFarmer implements VillagerRegistry.IVillageTradeHandler
{
    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
    {
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.wineGrapes, 0.5f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.hopCones, 0.6f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.hopSeeds, 0.4f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.woodenMug, 0.5f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.driedTobacco, 0.3f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.cigarette, 0.8f, -6, -2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.cigar, 0.3f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.tobaccoSeeds, 0.3f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.coffeeBeans, 0.8f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.coffeaCherries, 0.6f, 1, 1);
    }
}
