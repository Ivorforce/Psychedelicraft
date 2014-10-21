/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import ivorius.psychedelicraft.items.DrinkInformation;
import ivorius.psychedelicraft.items.ItemDrinkHolder;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lukas on 21.10.14.
 */
public class RecipeFillDrink implements IRecipe
{
    /**
     * Is the ItemStack that you get when craft the recipe.
     */
    private final DrinkInformation recipeOutput;
    /**
     * Is a List of ItemStack that composes the recipe.
     */
    public final List<ItemStack> recipeItems;

    public RecipeFillDrink(DrinkInformation recipeOutput, List<ItemStack> items)
    {
        this.recipeOutput = recipeOutput;
        this.recipeItems = items;
    }

    public RecipeFillDrink(DrinkInformation recipeOutput, Object... items)
    {
        this(recipeOutput, stacks(items));
    }

    public static List<ItemStack> stacks(Object... params)
    {
        List<ItemStack> stacks = new ArrayList<>();
        for (Object param : params)
        {
            if (param instanceof Item)
                stacks.add(new ItemStack((Item) param, 1, OreDictionary.WILDCARD_VALUE));
            else if (param instanceof Block)
                stacks.add(new ItemStack((Block) param, 1, OreDictionary.WILDCARD_VALUE));
            else if (param instanceof ItemStack)
                stacks.add((ItemStack) param);
            else
                throw new IllegalArgumentException();
        }
        return stacks;
    }

    public ItemStack getRecipeOutput()
    {
        return new ItemStack(PSItems.woodenMug);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inventoryCrafting, World p_77569_2_)
    {
        ArrayList<ItemStack> arraylist = new ArrayList<>(this.recipeItems);

        ItemStack drinkHolder = getFirstDrinkHolder(inventoryCrafting, recipeOutput.getFillings());
        if (drinkHolder == null)
            return false;
        arraylist.add(drinkHolder);

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                ItemStack itemstack = inventoryCrafting.getStackInRowAndColumn(j, i);

                if (itemstack != null)
                {
                    boolean flag = false;
                    Iterator iterator = arraylist.iterator();

                    while (iterator.hasNext())
                    {
                        ItemStack itemstack1 = (ItemStack) iterator.next();

                        if (itemstack.getItem() == itemstack1.getItem() && (itemstack1.getItemDamage() == 32767 || itemstack.getItemDamage() == itemstack1.getItemDamage()))
                        {
                            flag = true;
                            arraylist.remove(itemstack1);
                            break;
                        }
                    }

                    if (!flag)
                    {
                        return false;
                    }
                }
            }
        }

        return arraylist.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
    {
        ItemStack drinkHolder = getFirstDrinkHolder(inventoryCrafting, recipeOutput.getFillings());

        ItemStack result;

        if (drinkHolder.getItem() == Items.bowl)
            result = new ItemStack(PSItems.woodenBowlDrug);
        else
        {
            result = drinkHolder.copy();
            result.stackSize = 1;
        }

        ((ItemDrinkHolder) result.getItem()).setDrinkInfo(result, recipeOutput.clone());
        return result;
    }

    public ItemStack getFirstDrinkHolder(InventoryCrafting inventoryCrafting, int neededFillings)
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                ItemStack itemstack = inventoryCrafting.getStackInRowAndColumn(j, i);

                if (itemstack != null && (itemstack.getItem() instanceof ItemDrinkHolder && ((ItemDrinkHolder) itemstack.getItem()).getMaxDrinkFilling() >= neededFillings))
                    return itemstack;
                if (itemstack != null && itemstack.getItem() == Items.bowl && neededFillings <= PSItems.woodenBowlDrug.getMaxDrinkFilling())
                    return itemstack; // Hacky but eh
            }
        }

        return null;
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return this.recipeItems.size() + 1;
    }
}
