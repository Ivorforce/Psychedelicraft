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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lukas on 19.10.14.
 */
public class RecipeTransferDrink implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inventory, World world)
    {
        return getEqualDrinkHolders(inventory) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory)
    {
        Pair<ItemStack, DrinkInformation> pair = getEqualDrinkHolders(inventory);

        return pair != null ? ((ItemDrinkHolder) pair.getLeft().getItem()).createDrinkStack(1, pair.getRight()) : null;
    }

    public static Pair<ItemStack, DrinkInformation> getEqualDrinkHolders(InventoryCrafting inventory)
    {
        ItemStack empty = null;
        DrinkInformation full = null;

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                ItemStack itemstack = inventory.getStackInRowAndColumn(j, i);

                if (itemstack != null)
                {
                    if (itemstack.getItem() instanceof ItemDrinkHolder)
                    {
                        DrinkInformation drinkInfo = ((ItemDrinkHolder) itemstack.getItem()).getDrinkInfo(itemstack);
                        if (drinkInfo != null && full == null)
                            full = drinkInfo;
                        else if (drinkInfo == null && empty == null)
                            empty = itemstack;
                        else
                            return null;
                    }
                    else
                        return null;
                }
            }
        }

        return empty != null && full != null ? new ImmutablePair<>(empty, full) : null;
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
