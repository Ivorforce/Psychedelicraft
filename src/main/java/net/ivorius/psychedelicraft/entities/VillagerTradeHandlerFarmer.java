/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.entities;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.ivorius.psychedelicraft.items.PSItems;
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
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemWineGrapes, 0.5f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemWoodenMug, 0.5f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemDriedTobacco, 0.3f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemCigarette, 0.8f, -6, -2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemCigar, 0.3f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemTobaccoSeeds, 0.3f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemCoffeeBeans, 0.8f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemCoffeaCherries, 0.6f, 1, 1);
    }
}
