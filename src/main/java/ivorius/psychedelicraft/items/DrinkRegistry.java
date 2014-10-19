/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

/**
 * Created by lukas on 14.05.14.
 */
public class DrinkRegistry
{
    private static List<ItemDrinkHolder> registeredDrinkHolders = new ArrayList<>();
    private static BiMap<String, IDrink> registeredDrinks = HashBiMap.create();
    private static BiMap<Item, Map<String, String>> registeredDrinkSpecialIcons = HashBiMap.create();

    public static IDrink getDrink(String id)
    {
        return registeredDrinks.get(id);
    }

    public static String getDrinkID(IDrink drink)
    {
        return registeredDrinks.inverse().get(drink);
    }

    public static void registerDrink(String id, IDrink drink)
    {
        registeredDrinks.put(id, drink);
    }

    public static void registerSpecialIcon(String drinkID, Item item, String iconName)
    {
        if (!registeredDrinkSpecialIcons.containsKey(item))
        {
            registeredDrinkSpecialIcons.put(item, new HashMap<String, String>());
        }

        registeredDrinkSpecialIcons.get(item).put(drinkID, iconName);
    }

    public static void registerDrinkHolder(ItemDrinkHolder item)
    {
        registeredDrinkHolders.add(item);
    }

    public static Collection<String> getAllDrinkIDs()
    {
        return registeredDrinks.keySet();
    }

    public static Collection<IDrink> getAllDrinks()
    {
        return registeredDrinks.inverse().keySet();
    }

    public static Collection<String> getSpecialIcons(Item item)
    {
        Map<String, String> icons = registeredDrinkSpecialIcons.get(item);
        return icons != null ? icons.values() : Collections.<String>emptyList();
    }

    public static String getDrinkSpecialIcon(String drinkID, Item item)
    {
        Map<String, String> iconsForItem = registeredDrinkSpecialIcons.get(item);

        if (iconsForItem != null)
            return iconsForItem.get(drinkID);

        return null;
    }

    public static String getDrinkTranslationKey(IDrink drink, NBTTagCompound drinkInfo)
    {
        String specialKey = drink.getSpecialTranslationKey(drinkInfo);
        return "psDrink." + getDrinkID(drink) + (specialKey != null ? ("." + specialKey) : "");
    }

    public static List<ItemDrinkHolder> getAllDrinkHolders()
    {
        return registeredDrinkHolders;
    }
}
