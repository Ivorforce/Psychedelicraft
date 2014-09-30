/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public class DrinkWine implements IDrink
{
    public static ItemStack createWineStack(Item item, int stackSize, int wineStrength)
    {
        ItemStack stack = DrinkRegistry.createDrinkStack(item, stackSize, "wine");
        stack.setTagInfo("wineStrength", new NBTTagInt(wineStrength));
        return stack;
    }

    @Override
    public List<DrugInfluence> getDrugInfluences(ItemStack stack)
    {
        int wineStrength = stack.getTagCompound().getInteger("wineStrength");

        if (wineStrength < 14 && wineStrength > 2)
        {
            return Arrays.asList(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.02 * (stack.getItemDamage() - 2)));
        }

        return Arrays.asList();
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(ItemStack stack)
    {
        int wineStrength = stack.getTagCompound().getInteger("wineStrength");

        if (wineStrength < 14)
        {
            int foodLevel = wineStrength < 5 ? 1 : 0;
            return new MutablePair<>(foodLevel, foodLevel * 0.1f);
        }

        return null;
    }

    @Override
    public void applyToEntity(ItemStack stack, World world, EntityLivingBase entityLivingBase)
    {

    }

    @Override
    public String getSpecialTranslationKey(ItemStack stack)
    {
        int wineStrength = stack.getTagCompound().getInteger("wineStrength");

        return "" + wineStrength;
    }
}
