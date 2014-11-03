/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by lukas on 03.11.14.
 */
public interface RecipeAction
{
    ItemStack visualCraftingResult(InventoryCrafting inventoryCrafting);

    Pair<ItemStack, List<ItemStack>> craftingResult(InventoryCrafting inventoryCrafting);

    int getRecipeSize();

    ItemStack getRecipeOutput();
}
