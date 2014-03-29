package net.ivorius.psychedelicraft.entities;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.ivorius.psychedelicraft.Psychedelicraft;
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
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemWineGrapes, 0.5f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemWoodenMug, 0.5f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemDriedTobacco, 0.3f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemCigarette, 0.8f, -6, -2);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemCigar, 0.3f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemTobaccoSeeds, 0.3f, 2, 8);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemCoffeeBeans, 0.8f, 1, 1);
        VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Psychedelicraft.itemCoffeaCherries, 0.6f, 1, 1);
    }
}
