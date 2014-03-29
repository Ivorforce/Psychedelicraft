package net.ivorius.psychedelicraft.entities;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.ivorius.psychedelicraft.Psychedelicraft;
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
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemCannabisLeaf, 0.7f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemCannabisSeeds, 0.5f, 1, 5);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemDriedCannabisBuds, 0.9f, 1, 2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemDriedCannabisLeaves, 0.8f, 1, 2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemHashMuffin, 0.7f, 1, 2);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemMagicMushroomsBrown, 0.5f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemMagicMushroomsRed, 0.5f, 2, 8);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemCocaLeaf, 0.5f, 1, 4);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemCocaSeeds, 0.5f, 2, 4);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemDriedCocaLeaves, 0.5f, 2, 20);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemSyringe, 0.5f, 2, 4);

        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemDriedPeyote, 0.5f, 3, 10);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemPeyoteJoint, 0.5f, 1, 3);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Item.getItemFromBlock(Psychedelicraft.blockPeyote), 0.5f, 1, 5);
    }
}
