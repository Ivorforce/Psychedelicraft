/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import ivorius.psychedelicraft.items.DrinkInformation;
import ivorius.psychedelicraft.items.ItemDrinkHolder;
import ivorius.psychedelicraft.items.ItemMolotovCocktail;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lukas on 19.10.14.
 */
public class RecipeMolotovCocktail implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inventory, World world)
    {
        List<ItemStack> arraylist = new ArrayList<>();
        arraylist.add(new ItemStack(Items.paper));
        arraylist.add(new ItemStack(Blocks.glass));
        boolean hadDrink = false;

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                ItemStack itemstack = inventory.getStackInRowAndColumn(j, i);

                if (itemstack != null)
                {
                    if (itemstack.getItem() instanceof ItemDrinkHolder && ItemDrinkHolder.getDrinkInfo(itemstack) != null)
                    {
                        if (hadDrink)
                            return false;

                        hadDrink = true;
                    }
                    else
                    {
                        boolean flag = false;
                        Iterator iterator = arraylist.iterator();

                        while (iterator.hasNext())
                        {
                            ItemStack itemstack1 = (ItemStack)iterator.next();

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
        }

        return arraylist.isEmpty() && hadDrink;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory)
    {
        DrinkInformation drinkInfo = getFirstDrinkInfo(inventory);

        if (drinkInfo != null)
            return ItemMolotovCocktail.createMolotovStack(PSItems.molotovCocktail, 1, drinkInfo);

        return null;
    }

    public static DrinkInformation getFirstDrinkInfo(InventoryCrafting inventory)
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                ItemStack itemstack = inventory.getStackInRowAndColumn(j, i);
                if (itemstack != null && itemstack.getItem() instanceof ItemDrinkHolder)
                {
                    DrinkInformation drinkInfo = ItemDrinkHolder.getDrinkInfo(itemstack);
                    if (drinkInfo != null)
                        return drinkInfo;
                }
            }
        }

        return null;
    }

    @Override
    public int getRecipeSize()
    {
        return 3;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return new ItemStack(PSItems.molotovCocktail);
    }
}
