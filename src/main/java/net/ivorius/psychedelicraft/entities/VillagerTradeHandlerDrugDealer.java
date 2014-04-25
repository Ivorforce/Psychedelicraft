package net.ivorius.psychedelicraft.entities;

import java.util.Random;

import net.ivorius.psychedelicraft.blocks.PSBlocks;
import net.ivorius.psychedelicraft.items.PSItems;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry;

/**
 * Created by lukas on 23.02.14.
 */
public class VillagerTradeHandlerDrugDealer implements VillagerRegistry.IVillageTradeHandler
{
    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
    {
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemCannabisLeaf, 0.7f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemCannabisSeeds, 0.5f, 1, 5);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemDriedCannabisBuds, 0.9f, 1, 2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemDriedCannabisLeaves, 0.8f, 1, 2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemHashMuffin, 0.7f, 1, 2);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemMagicMushroomsBrown, 0.5f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemMagicMushroomsRed, 0.5f, 2, 8);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemCocaLeaf, 0.5f, 1, 4);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemCocaSeeds, 0.5f, 2, 4);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemDriedCocaLeaves, 0.5f, 2, 20);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemSyringe, 0.5f, 2, 4);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemDriedPeyote, 0.5f, 3, 10);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, PSItems.itemPeyoteJoint, 0.5f, 1, 3);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Item.getItemFromBlock(PSBlocks.blockPeyote), 0.5f, 1, 5);
    }
}
