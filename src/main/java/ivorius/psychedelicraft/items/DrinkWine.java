/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public class DrinkWine implements IDrink
{
    public static ItemStack createWineStack(Item item, int stackSize, int wineStrength)
    {
        NBTTagCompound drinkInfo = new NBTTagCompound();
        drinkInfo.setInteger("wineStrength", wineStrength);
        ItemStack stack = ItemDrinkHolder.createDrinkStack(item, stackSize, new DrinkInformation("wine", 1, drinkInfo));
        return stack;
    }

    public String iconString;
    @SideOnly(Side.CLIENT)
    public IIcon icon;

    public DrinkWine(String icon)
    {
        this.iconString = icon;
    }

    @Override
    public List<DrugInfluence> getDrugInfluences(NBTTagCompound info)
    {
        int wineStrength = info.getInteger("wineStrength");

        if (wineStrength < 14 && wineStrength > 2)
        {
            return Arrays.asList(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.02 * (wineStrength - 2)));
        }

        return Arrays.asList();
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(NBTTagCompound info)
    {
        int wineStrength = info.getInteger("wineStrength");

        if (wineStrength < 14)
        {
            int foodLevel = wineStrength < 5 ? 1 : 0;
            return new MutablePair<>(foodLevel, foodLevel * 0.1f);
        }

        return null;
    }

    @Override
    public void applyToEntity(NBTTagCompound info, EntityLivingBase entityLivingBase, World world)
    {

    }

    @Override
    public String getSpecialTranslationKey(NBTTagCompound info)
    {
        return "" + info.getInteger("wineStrength");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerItemIcons(IIconRegister iconRegister)
    {
        if (iconString != null)
            icon = iconRegister.registerIcon(iconString);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getDrinkIcon(NBTTagCompound info)
    {
        return icon;
    }

    @Override
    public ItemStack createItemStack(ItemDrinkHolder drinkHolder, NBTTagCompound drinkInfo, int timeFermented)
    {
        return createWineStack(drinkHolder, 1, (timeFermented / (20 * 60 * 15)) + drinkInfo.getInteger("wineStrength"));
    }

    @Override
    public List<NBTTagCompound> creativeTabInfos(Item drinkHolder, CreativeTabs tabs)
    {
        List<NBTTagCompound> compounds = new ArrayList<>(16);
        for (int strength = 0; strength < 16; strength += 2)
        {
            NBTTagCompound drinkInfo = new NBTTagCompound();
            drinkInfo.setInteger("wineStrength", strength);
            compounds.add(drinkInfo);
        }
        return compounds;
    }
}
