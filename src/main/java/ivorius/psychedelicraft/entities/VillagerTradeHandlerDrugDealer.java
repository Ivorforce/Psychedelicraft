/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import cpw.mods.fml.common.registry.VillagerRegistry;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

/**
 * Created by lukas on 23.02.14.
 */
public class VillagerTradeHandlerDrugDealer implements VillagerRegistry.IVillageTradeHandler
{
    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
    {
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.cannabisLeaf, 0.7f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.cannabisSeeds, 0.5f, 1, 5);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.driedCannabisBuds, 0.9f, 1, 2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.driedCannabisLeaves, 0.8f, 1, 2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.hashMuffin, 0.7f, 1, 2);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.magicMushroomsBrown, 0.5f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.magicMushroomsRed, 0.5f, 2, 8);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.cocaLeaf, 0.5f, 1, 4);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.cocaSeeds, 0.5f, 2, 4);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.driedCocaLeaves, 0.5f, 2, 20);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.syringe, 0.5f, 2, 4);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.driedPeyote, 0.5f, 3, 10);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.peyoteJoint, 0.5f, 1, 3);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Item.getItemFromBlock(PSBlocks.peyote), 0.5f, 1, 5);
    }
}
