/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import ivorius.psychedelicraft.items.DrinkInformation;
import ivorius.psychedelicraft.items.ItemDrinkHolder;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
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

        ItemStack left = pair.getLeft().copy();
        ((ItemDrinkHolder) pair.getLeft().getItem()).setDrinkInfo(left, pair.getRight());
        left.stackSize = 1;

        return left;
    }

    public static Pair<ItemStack, DrinkInformation> getEqualDrinkHolders(InventoryCrafting inventory)
    {
        ItemStack dst = null;
        DrinkInformation src;
        int srcRow;

        for (srcRow = 0; srcRow < 2; ++srcRow)
        {
            List<ItemStack> row = itemStacksInRow(inventory, srcRow);
            if (row.size() == 0)
                continue;
            else if (row.size() == 1)
            {
                dst = row.get(0);

                if (dst.getItem() == Items.bowl || dst.getItem() instanceof ItemDrinkHolder)
                    break;
            }

            return null;
        }

        if (dst == null)
            return null;

        src = ((ItemDrinkHolder) dst.getItem()).getDrinkInfo(dst).clone();

        for (int y = srcRow + 1; y < 3; ++y)
        {
            for (int x = 0; x < 3; ++x)
            {
                ItemStack itemstack = inventory.getStackInRowAndColumn(x, y);

                if (itemstack != null)
                {
                    ItemDrinkHolder drinkHolder = itemstack.getItem() instanceof ItemDrinkHolder ? (ItemDrinkHolder) itemstack.getItem() : null;
                    if (itemstack.getItem() == Items.bowl)
                        drinkHolder = PSItems.woodenBowlDrug;

                    if (drinkHolder != null)
                    {
                        DrinkInformation drinkInfo = drinkHolder.getDrinkInfo(itemstack);

                        if (drinkInfo != null)
                        {
                            if (src == null)
                                src = drinkInfo.clone();
                            else if (drinkInfo.equalsDrinkType(src))
                                src.incrementFillings(drinkInfo.getFillings());
                            else
                                return null;
                        }
                    }
                    else
                        return null;
                }
            }
        }

        if (src != null && ItemDrinkHolder.maxFillingsTransferred(src, dst) > 0)
            return new ImmutablePair<>(dst, src);

        return null;
    }

    public static List<ItemStack> itemStacksInRow(InventoryCrafting inventory, int y)
    {
        List<ItemStack> list = new ArrayList<>();
        for (int x = 0; x < 3; ++x)
        {
            ItemStack itemstack = inventory.getStackInRowAndColumn(x, y);
            if (itemstack != null)
                list.add(itemstack);
        }
        return list;
    }

    @Override
    public int getRecipeSize()
    {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return new ItemStack(PSItems.molotovCocktail);
    }
}
