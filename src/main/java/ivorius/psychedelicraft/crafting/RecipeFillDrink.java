/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lukas on 21.10.14.
 */
public class RecipeFillDrink implements IRecipe
{
    private final FluidStack recipeOutput;
    public final List<Object> recipeItems;

    public RecipeFillDrink(FluidStack recipeOutput, List<Object> items)
    {
        this.recipeOutput = recipeOutput;
        this.recipeItems = items;
    }

    public RecipeFillDrink(FluidStack recipeOutput, Object... items)
    {
        this.recipeOutput = recipeOutput;

        recipeItems = new ArrayList<>();
        for (Object in : items)
        {
            if (in instanceof ItemStack)
                recipeItems.add(((ItemStack) in).copy());
            else if (in instanceof Item)
                recipeItems.add(new ItemStack((Item) in));
            else if (in instanceof Block)
                recipeItems.add(new ItemStack((Block) in));
            else if (in instanceof String)
                recipeItems.add(OreDictionary.getOres((String) in));
            else
            {
                String ret = "Invalid shapeless ore recipe: ";
                for (Object tmp : items)
                {
                    ret += tmp + ", ";
                }
                ret += recipeOutput;
                throw new RuntimeException(ret);
            }
        }
    }

    public ItemStack getRecipeOutput()
    {
        return new ItemStack(PSItems.woodenMug);
    }

    public boolean matches(InventoryCrafting inventoryCrafting, World world)
    {
        ArrayList<Object> required = new ArrayList<>(this.recipeItems);

        ItemStack drinkHolder = getFirstDrinkHolder(inventoryCrafting, recipeOutput);
        if (drinkHolder == null)
            return false;
        required.add(drinkHolder);

        for (int x = 0; x < inventoryCrafting.getSizeInventory(); x++)
        {
            ItemStack slot = inventoryCrafting.getStackInSlot(x);

            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        match = OreDictionary.itemMatches((ItemStack)next, slot, false);
                    }
                    else if (next instanceof ArrayList)
                    {
                        Iterator<ItemStack> itr = ((ArrayList<ItemStack>)next).iterator();
                        while (itr.hasNext() && !match)
                        {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();    }

    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
    {
        ItemStack drinkHolder = getFirstDrinkHolder(inventoryCrafting, recipeOutput);

        ItemStack result;

        if (drinkHolder.getItem() == Items.bowl)
            result = new ItemStack(PSItems.woodenBowlDrug);
        else
        {
            result = drinkHolder.copy();
            result.stackSize = 1;
        }

        ((IFluidContainerItem) result.getItem()).fill(result, recipeOutput, true);
        return result;
    }

    public ItemStack getFirstDrinkHolder(InventoryCrafting inventoryCrafting, FluidStack fluidStack)
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                ItemStack itemstack = inventoryCrafting.getStackInRowAndColumn(j, i);

                if (itemstack != null && (itemstack.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem) itemstack.getItem()).fill(itemstack, fluidStack, false) >= fluidStack.amount))
                    return itemstack;
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
