/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

/**
 * Created by lukas on 03.11.14.
 */
public class RecipeActionRegistry
{
    private static BiMap<String, RecipeAction> actionRecipes = HashBiMap.create();

    public static void register(String id, RecipeAction recipe)
    {
        actionRecipes.put(id, recipe);
    }

    public static void addRecipe(String id, RecipeAction recipe)
    {
        register(id, recipe);
        GameRegistry.addRecipe(new RecipeActionRepresentation(recipe));
    }

    public static RecipeAction actionForID(String id)
    {
        return actionRecipes.get(id);
    }

    public static String idForAction(RecipeAction recipe)
    {
        return actionRecipes.inverse().get(recipe);
    }

    public static Set<RecipeAction> allRecipes()
    {
        return actionRecipes.values();
    }

    public static Set<String> allRecipeIDs()
    {
        return actionRecipes.keySet();
    }

    public static boolean finalizeCrafting(ItemStack result, InventoryCrafting inventoryCrafting, EntityPlayer player)
    {
        if (result.hasTagCompound() && result.getTagCompound().hasKey(RecipeActionRepresentation.ACTION_TAG_ID, Constants.NBT.TAG_STRING))
        {
            String actionID = result.getTagCompound().getString(RecipeActionRepresentation.ACTION_TAG_ID);

            RecipeAction recipe = RecipeActionRegistry.actionForID(actionID);
            Pair<ItemStack, List<ItemStack>> actionResult = recipe.craftingResult(inventoryCrafting);

            ItemStack actionResultPickup = actionResult.getLeft();
            List<ItemStack> actionResultPickups = actionResult.getRight();

            NBTTagCompound resultCompound = new NBTTagCompound();
            actionResultPickup.writeToNBT(resultCompound);
            result.readFromNBT(resultCompound);
            result.stackTagCompound = actionResultPickup.stackTagCompound; // Doesn't necessarily get overwritten

            for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++)
                inventoryCrafting.setInventorySlotContents(i, null);

            for (ItemStack resultPickup : actionResultPickups)
            {
                if (resultPickup != null && resultPickup.isItemStackDamageable() && resultPickup.getItemDamage() > resultPickup.getMaxDamage())
                {
                    MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, resultPickup));
                    continue;
                }

                if (!player.inventory.addItemStackToInventory(resultPickup))
                {
                    player.dropPlayerItemWithRandomChoice(resultPickup, false);
                }
            }

            return true;
        }

        return false;
    }
}
