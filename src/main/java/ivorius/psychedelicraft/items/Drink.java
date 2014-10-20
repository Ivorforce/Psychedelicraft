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

import java.util.Arrays;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public class Drink implements IDrink
{
    public List<DrugInfluence> drugInfluences;

    public String iconString;
    @SideOnly(Side.CLIENT)
    public IIcon icon;

    public int foodLevel;
    public float foodSaturation;

    public Drink(int foodLevel, float foodSaturation, String icon, DrugInfluence... drugInfluences)
    {
        this.drugInfluences = Arrays.asList(drugInfluences);
        this.iconString = icon;
        this.foodLevel = foodLevel;
        this.foodSaturation = foodSaturation;
    }

    public Drink(String icon, DrugInfluence... drugInfluences)
    {
        this(0, 0.0f, icon, drugInfluences);
    }

    @Override
    public List<DrugInfluence> getDrugInfluences(NBTTagCompound info)
    {
        return drugInfluences;
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(NBTTagCompound info)
    {
        return foodLevel != 0 || foodSaturation != 0.0f ? new MutablePair<>(foodLevel, foodSaturation) : null;
    }

    @Override
    public void applyToEntity(NBTTagCompound info, EntityLivingBase entityLivingBase, World world)
    {

    }

    @Override
    public String getSpecialTranslationKey(NBTTagCompound info)
    {
        return null;
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
        return drinkHolder.createDrinkStack(1, new DrinkInformation(DrinkRegistry.getDrinkID(this), 1, drinkInfo));
    }

    @Override
    public List<NBTTagCompound> creativeTabInfos(Item drinkHolder, CreativeTabs tabs)
    {
        return Arrays.asList(new NBTTagCompound());
    }
}
