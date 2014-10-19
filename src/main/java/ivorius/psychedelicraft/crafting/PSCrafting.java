/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.blocks.TileEntityDryingTable;
import ivorius.psychedelicraft.entities.DrugInfluence;
import ivorius.psychedelicraft.entities.DrugInfluenceHarmonium;
import ivorius.psychedelicraft.items.*;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static ivorius.psychedelicraft.blocks.PSBlocks.*;
import static ivorius.psychedelicraft.items.PSItems.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

/**
 * Created by lukas on 18.10.14.
 */
public class PSCrafting
{
    public static void initialize()
    {
        GameRegistry.addRecipe(new ItemStack(syringe), "I", "#", 'I', iron_ingot, '#', glass);
        GameRegistry.addRecipe(new ItemStack(pipe), "  I", " S ", "WS ", 'I', iron_ingot, 'S', stick, 'W', planks);
        GameRegistry.addRecipe(new ItemStack(bong, 1, 3), " P ", "G G", "GGG", 'P', glass_pane, 'G', glass);

        GameRegistry.addRecipe(new ItemStack(glassChalice, 4), "# #", " # ", " # ", '#', glass);
        GameRegistry.addRecipe(new ItemStack(barrel, 1, BARREL_ITEM_DAMAGE_WINE), "###", "###", "W#W", '#', wineGrapes, 'W', planks);
        GameRegistry.addRecipe(new ItemStack(wineGrapeLattice), "###", "###", "O#O", '#', stick, 'O', planks);

        for (int i = 0; i < 16; i++)
        {
            GameRegistry.addRecipe(new ItemStack(molotovCocktail, 1, i), "#", "W", '#', paper, 'W', new ItemStack(glassChalice, 1, i + 1));
        }

        GameRegistry.addRecipe(new ItemStack(woodenMug, 8), "# #", "# #", "###", '#', planks);
        GameRegistry.addRecipe(new ItemStack(barrel, 1, BARREL_ITEM_DAMAGE_BEER), "###", "###", "WUW", '#', Items.wheat, 'U', water_bucket, 'W', planks);

        GameRegistry.addRecipe(new ItemStack(barrel, 1, BARREL_ITEM_DAMAGE_VODKA), "###", "###", "WUW", '#', Items.potato, 'U', water_bucket, 'W', planks);

        GameRegistry.addRecipe(new ItemStack(dryingTable), "###", "#R#", '#', planks, 'R', redstone);

        TileEntityDryingTable.addDryingResult(cannabisLeaf, new ItemStack(driedCannabisLeaves, 3));
        TileEntityDryingTable.addDryingResult(cannabisBuds, new ItemStack(driedCannabisBuds, 3));
        GameRegistry.addRecipe(new ItemStack(hashMuffin), "LLL", "#X#", "LLL", 'X', new ItemStack(dye, 1, 3), '#', Items.wheat, 'L', driedCannabisLeaves);
        GameRegistry.addRecipe(new ItemStack(joint), "P", "C", "P", 'P', paper, 'C', driedCannabisBuds);
        pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(driedCannabisBuds), new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.25f)}));
        bong.addConsumable(new ItemBong.ItemBongConsumable(new ItemStack(driedCannabisBuds), new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.2f)})); //TODO: Play around with the bongs benefits

        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(brown_mushroom), new ItemStack(magicMushroomsBrown, 3));
        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(red_mushroom), new ItemStack(magicMushroomsRed, 3));

        GameRegistry.addRecipe(new ItemStack(cigarette, 4), "P", "T", "P", 'P', paper, 'T', driedTobacco);
        GameRegistry.addRecipe(new ItemStack(cigar), "TTT", "TTT", "PPP", 'P', paper, 'T', driedTobacco);
        TileEntityDryingTable.addDryingResult(tobaccoLeaf, new ItemStack(driedTobacco, 3));
        pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(driedTobacco), new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.8f)}));
        bong.addConsumable(new ItemBong.ItemBongConsumable(new ItemStack(driedTobacco), new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.6f)})); //TODO: Play around with the bongs benefits

        addShapelessRecipe(new ItemStack(syringe, 1, ItemSyringe.damageCocaine), water_bucket, new ItemStack(syringe, 1, 0), driedCocaLeaves);
        TileEntityDryingTable.addDryingResult(cocaLeaf, new ItemStack(driedCocaLeaves, 3));

        GameRegistry.addRecipe(new ItemStack(planks, 4, 1), "#", '#', psycheLog);
        GameRegistry.addRecipe(new ItemStack(barrel, 1, BARREL_ITEM_DAMAGE_JENEVER), "JSJ", "AGA", "WUW", 'J', juniperBerries, 'S', sugar, 'A', wineGrapes, 'G', Items.wheat, 'U', water_bucket, 'W', planks);

        GameRegistry.addSmelting(coffeaCherries, new ItemStack(coffeeBeans), 0.2f);

        addShapelessRecipe(new ItemStack(syringe, 1, ItemSyringe.damageCaffeine), water_bucket, new ItemStack(syringe, 1, 0), coffeeBeans, coffeeBeans);

        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(peyote), new ItemStack(driedPeyote, 3));
        GameRegistry.addRecipe(new ItemStack(peyoteJoint), "P", "D", "P", 'P', paper, 'D', driedPeyote);

        for (int i = 0; i < 16; i++)
        {
            pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(harmonium, 1, 15 - i), new DrugInfluence[]{new DrugInfluenceHarmonium("Harmonium", 0, 0.04, 0.01, 0.65f, EntitySheep.fleeceColorTable[i])}, EntitySheep.fleeceColorTable[i]));
            addShapelessRecipe(new ItemStack(harmonium, 1, i), new ItemStack(dye, 1, i), glowstone_dust, driedTobacco);
        }

        GameRegistry.addRecipe(new ItemStack(riftJar), "O-O", "GO ", "OIO", 'O', glass, '-', planks, 'G', gold_ingot, 'I', iron_ingot);

        for (ItemDrinkHolder itemDrinkHolder : DrinkRegistry.getAllDrinkHolders())
        {
            Item emptyContainer = itemDrinkHolder == woodenBowlDrug ? bowl : itemDrinkHolder; // Hacky but eh

            addShapelessRecipe(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "peyote"), driedPeyote, driedPeyote, new ItemStack(emptyContainer));
            addShapelessRecipe(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "coldCoffee"), water_bucket, new ItemStack(emptyContainer), coffeeBeans, coffeeBeans);
            addShapelessRecipe(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "cocaTea"), water_bucket, new ItemStack(emptyContainer), cocaLeaf, cocaLeaf);
            addShapelessRecipe(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "cannabisTea"), water_bucket, new ItemStack(emptyContainer), cannabisLeaf, cannabisLeaf);
            //TODO Add when Forge fixes smelting with NBT
//        GameRegistry.addSmelting(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "coldCoffee"), new ItemStack(emptyContainer, 1, 3), 0.2f);
        }
    }

    private static void addShapelessRecipe(ItemStack output, Object... params)
    {
        for (int i = 0; i < params.length; i++)
        {
            if (params[i] instanceof Item)
                params[i] = new ItemStack((Item) params[i], 1, OreDictionary.WILDCARD_VALUE);
            else if (params[i] instanceof Block)
                params[i] = new ItemStack((Block) params[i], 1, OreDictionary.WILDCARD_VALUE);
        }

        GameRegistry.addShapelessRecipe(output, params);
    }
}
