/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import ivorius.psychedelicraft.blocks.TileEntityMashTub;
import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import ivorius.psychedelicraft.entities.drugs.DrugInfluenceHarmonium;
import ivorius.psychedelicraft.items.ItemBong;
import ivorius.psychedelicraft.items.ItemSmokingPipe;
import net.minecraft.block.BlockWood;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;

import static ivorius.psychedelicraft.blocks.PSBlocks.*;
import static ivorius.psychedelicraft.crafting.OreDictionaryConstants.*;
import static ivorius.psychedelicraft.fluids.PSFluids.*;
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
        addRecipe(new ItemStack(syringe), "I", "#", 'I', DC_IRON_INGOT, '#', DC_BLOCK_GLASS_CLEAR);
        addRecipe(new ItemStack(pipe), "  I", " S ", "WS ", 'I', DC_IRON_INGOT, 'S', DC_STICK_WOOD, 'W', DC_PLANK_WOOD);
        addRecipe(new ItemStack(bong, 1, 3), " P ", "G G", "GGG", 'P', DC_GLASS_PANE_CLEAR, 'G', DC_BLOCK_GLASS_CLEAR);

        for (int type = 0; type < BlockWood.field_150096_a.length; type++)
        {
            addRecipe(new ItemStack(itemBarrel, 1, type), " I ", "# #", "S#S", '#', new ItemStack(planks, 1, type), 'I', DC_IRON_INGOT, 'S', DC_STICK_WOOD);
        }

        addRecipe(new ItemStack(itemMashTub), "# #", "I I", "###", 'I', DC_IRON_INGOT, '#', DC_PLANK_WOOD);

        Object flaskIngotItem = foreignItem(DC_COPPER_INGOT, DC_IRON_INGOT);
        addRecipe(new ItemStack(itemFlask), " # ", "#G#", "###", 'G', DC_BLOCK_GLASS_CLEAR, '#', flaskIngotItem);
        addRecipe(new ItemStack(itemDistillery), "##", "D ", 'D', itemFlask, '#', flaskIngotItem);

        ItemStack wineMash = new ItemStack(itemMashTub);
        itemMashTub.fill(wineMash, new FluidStack(wine, TileEntityMashTub.MASH_TUB_CAPACITY), true);
        addShapelessRecipe(wineMash, new ItemStack(itemMashTub), DC_GRAPE, DC_GRAPE, DC_GRAPE, DC_GRAPE, DC_GRAPE, DC_GRAPE, DC_GRAPE, DC_GRAPE);

        ItemStack wortMash = new ItemStack(itemMashTub);
        itemMashTub.fill(wortMash, new FluidStack(beer, TileEntityMashTub.MASH_TUB_CAPACITY), true);
        addShapelessRecipe(wortMash, new ItemStack(itemMashTub), water_bucket, DC_CROP_WHEAT, DC_CROP_WHEAT, DC_CROP_WHEAT, DC_CROP_WHEAT, DC_CROP_WHEAT, DC_CROP_WHEAT, DC_CROP_WHEAT);

        ItemStack vodkaMash = new ItemStack(itemMashTub);
        itemMashTub.fill(vodkaMash, new FluidStack(vodka, TileEntityMashTub.MASH_TUB_CAPACITY), true);
        addShapelessRecipe(vodkaMash, new ItemStack(itemMashTub), water_bucket, DC_POTATO, DC_POTATO, DC_POTATO, DC_POTATO, DC_POTATO, DC_POTATO, DC_POTATO);

        ItemStack jeneverMash = new ItemStack(itemMashTub);
        itemMashTub.fill(jeneverMash, new FluidStack(jenever, TileEntityMashTub.MASH_TUB_CAPACITY), true);
        addShapelessRecipe(jeneverMash, new ItemStack(itemMashTub), DC_JUNIPER_BERRIES, DC_JUNIPER_BERRIES, DC_JUNIPER_BERRIES, DC_JUNIPER_BERRIES, sugar, DC_GRAPE, DC_GRAPE, DC_CROP_WHEAT);

        addRecipe(new ItemStack(glassChalice, 4), "# #", " # ", " # ", '#', DC_BLOCK_GLASS_CLEAR);
        addRecipe(new ItemStack(wineGrapeLattice), "###", "###", "O#O", '#', DC_STICK_WOOD, 'O', DC_PLANK_WOOD);

        addRecipe(new ItemStack(woodenMug, 8), "# #", "# #", "###", '#', DC_PLANK_WOOD);

//        for (int color = 0; color < ItemDye.field_150922_c.length; color++)
//            addRecipe(new ItemStack(molotovCocktail, 4, color), "P", "#", '#', new ItemStack(stained_glass, 1, 15 - color), 'P', paper);

        for (int color = 0; color < ItemDye.field_150922_c.length; color++)
        {
            addRecipe(new ItemStack(bottle, 8, color), " # ", "# #", "###", '#', new ItemStack(stained_glass, 1, 15 - color));
            addRecipe(new ItemStack(molotovCocktail, 1, color), "P", "G", 'P', paper, 'G', new ItemStack(bottle, 1, color));
        }

        addRecipe(new ItemStack(dryingTable), "###", "#R#", '#', DC_PLANK_WOOD, 'R', DC_REDSTONE_DUST);

        DryingRegistry.addDryingResult(DC_LEAF_CANNABIS, new ItemStack(driedCannabisLeaves, 3));
        DryingRegistry.addDryingResult(DC_BUD_CANNABIS, new ItemStack(driedCannabisBuds, 3));
        addRecipe(new ItemStack(hashMuffin), "LLL", "#X#", "LLL", 'X', new ItemStack(dye, 1, 3), '#', DC_CROP_WHEAT, 'L', DC_DRIED_CANNABIS_LEAVES);
        addRecipe(new ItemStack(joint), "P", "C", "P", 'P', paper, 'C', driedCannabisBuds);
        pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(driedCannabisBuds), new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.25f)}));
        bong.addConsumable(new ItemBong.ItemBongConsumable(new ItemStack(driedCannabisBuds), new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.2f)})); //TODO: Play around with the bongs benefits

        DryingRegistry.addDryingResult(brown_mushroom, new ItemStack(magicMushroomsBrown, 3));
        DryingRegistry.addDryingResult(red_mushroom, new ItemStack(magicMushroomsRed, 3));

        addRecipe(new ItemStack(cigarette, 4), "P", "T", "P", 'P', paper, 'T', DC_DRIED_TOBACCO);
        addRecipe(new ItemStack(cigar), "TTT", "TTT", "PPP", 'P', paper, 'T', DC_DRIED_TOBACCO);
        DryingRegistry.addDryingResult(DC_DRIED_TOBACCO, new ItemStack(driedTobacco, 3));
        pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(driedTobacco), new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.8f)}));
        bong.addConsumable(new ItemBong.ItemBongConsumable(new ItemStack(driedTobacco), new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.6f)})); //TODO: Play around with the bongs benefits

        DryingRegistry.addDryingResult(DC_LEAF_COCA, new ItemStack(driedCocaLeaves, 3));

        addRecipe(new ItemStack(planks, 4, 1), "#", '#', psycheLog);

        GameRegistry.addSmelting(coffeaCherries, new ItemStack(coffeeBeans), 0.2f);

        DryingRegistry.addDryingResult(DC_CROP_PEYOTE, new ItemStack(driedPeyote, 3));
        addRecipe(new ItemStack(peyoteJoint), "P", "D", "P", 'P', paper, 'D', driedPeyote);

        for (int i = 0; i < 16; i++)
        {
            pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(harmonium, 1, 15 - i), new DrugInfluence[]{new DrugInfluenceHarmonium("Harmonium", 0, 0.04, 0.01, 0.65f, EntitySheep.fleeceColorTable[i])}, EntitySheep.fleeceColorTable[i]));

            if (PSConfig.enableHarmonium)
                addShapelessRecipe(new ItemStack(harmonium, 1, i), new ItemStack(dye, 1, i), DC_GLOWSTONE_DUST, DC_DRIED_TOBACCO);
        }

        if (PSConfig.enableRiftJars)
            addRecipe(new ItemStack(riftJar), "O-O", "GO ", "OIO", 'O', DC_BLOCK_GLASS_CLEAR, '-', DC_PLANK_WOOD, 'G', DC_GOLD_INGOT, 'I', DC_IRON_INGOT);

        RecipeActionRegistry.addRecipe("ps_pourDrink", new RecipePourDrink());

        GameRegistry.addRecipe(new RecipeFillDrink(new FluidStack(peyoteJuice, 100), water_bucket, DC_PEYOTE_DRIED, DC_PEYOTE_DRIED));
        GameRegistry.addRecipe(new RecipeFillDrink(new FluidStack(coffee, 100), water_bucket, DC_COFFEE_BEANS, DC_COFFEE_BEANS));
        GameRegistry.addRecipe(new RecipeFillDrink(new FluidStack(cocaTea, 100), water_bucket, DC_LEAF_COCA, DC_LEAF_COCA));
        GameRegistry.addRecipe(new RecipeFillDrink(new FluidStack(cannabisTea, 100), water_bucket, DC_LEAF_CANNABIS, DC_LEAF_CANNABIS));
        GameRegistry.addRecipe(new RecipeFillDrink(new FluidStack(cocaineFluid, 10), water_bucket, DC_LEAF_COCA_DRIED, DC_LEAF_COCA_DRIED));
        GameRegistry.addRecipe(new RecipeFillDrink(new FluidStack(caffeineFluid, 10), DC_COFFEE_BEANS, DC_COFFEE_BEANS));

        //TODO Add when Forge fixes smelting with NBT
//                GameRegistry.addSmelting(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "coldCoffee"), new ItemStack(emptyContainer, 1, 3), 0.2f);
    }

    private static Object foreignItem(String oredictID, Object replacement)
    {
        List<ItemStack> foreignItems = OreDictionary.getOres(oredictID);
        return foreignItems.size() == 0 ? replacement : oredictID;
    }

    private static void addRecipe(ItemStack output, Object... params)
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(output, params));
    }

    private static void addShapelessRecipe(ItemStack output, Object... params)
    {
        GameRegistry.addRecipe(new ShapelessOreRecipe(output, params));
    }
}
