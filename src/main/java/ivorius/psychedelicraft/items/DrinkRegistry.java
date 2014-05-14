/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public class DrinkRegistry
{
    private static Hashtable<String, IDrink> registeredDrinks = new Hashtable<String, IDrink>();
    private static Hashtable<Item, Hashtable<String, String>> registeredDrinkSpecialIcons = new Hashtable<Item, Hashtable<String, String>>();

    public static void registerDrink(String id, IDrink drink)
    {
        registeredDrinks.put(id, drink);
    }

    public static void registerSpecialIcon(String drinkID, Item item, String iconName)
    {
        if (!registeredDrinkSpecialIcons.containsKey(item))
            registeredDrinkSpecialIcons.put(item, new Hashtable<String, String>());

        registeredDrinkSpecialIcons.get(item).put(drinkID, iconName);
    }

    public Collection<String> getAllDrinkIDs()
    {
        return registeredDrinks.keySet();
    }

    public static String getDrinkIDFromStack(ItemStack stack)
    {
        if (!stack.hasTagCompound())
            return null;

        return stack.getTagCompound().getString("drinkID");
    }

    public static IDrink getDrinkFromStack(ItemStack stack)
    {
        String drinkID = getDrinkIDFromStack(stack);

        if (drinkID != null)
            return registeredDrinks.get(drinkID);

        return null;
    }

    public static String getDrinkTranslationKey(ItemStack stack)
    {
        String drinkKey = getDrinkIDFromStack(stack);

        if (drinkKey != null)
        {
            IDrink drink = registeredDrinks.get(drinkKey);

            if (drink != null)
            {
                String specialKey = drink.getSpecialTranslationKey(stack);

                return "psDrink." + drinkKey + (specialKey != null ? ("." + specialKey) : "");
            }
        }

        return null;
    }

    public static String getDrinkSpecialIcon(ItemStack stack)
    {
        String drinkID = getDrinkIDFromStack(stack);

        if (drinkID != null)
        {
            Hashtable<String, String> iconsForItem = registeredDrinkSpecialIcons.get(stack.getItem());

            if (iconsForItem != null)
                return iconsForItem.get(drinkID);
        }

        return null;
    }

    public static Collection<String> getSpecialIcons(Item item)
    {
        return registeredDrinkSpecialIcons.get(item).values();
    }

    public static Collection<String> getAllDrinks()
    {
        return registeredDrinks.keySet();
    }

    public static ItemStack createDrinkStack(Item item, int stackSize, String drinkID)
    {
        ItemStack stack = new ItemStack(item, stackSize);
        stack.setTagInfo("drinkID", new NBTTagString(drinkID));
        return stack;
    }
}
