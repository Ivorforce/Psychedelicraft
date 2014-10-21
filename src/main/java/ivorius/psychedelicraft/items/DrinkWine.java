/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.PSConfig;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public class DrinkWine implements IDrink
{
    public static DrinkInformation createWineDrinkInfo(float wineStrength, float vinegarProgress)
    {
        NBTTagCompound drinkInfo = new NBTTagCompound();
        drinkInfo.setFloat("wineStrength", wineStrength);
        drinkInfo.setFloat("vinegarProgress", vinegarProgress);
        return new DrinkInformation("wine", 1, drinkInfo);
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
        if (isVinegar(info))
            return Collections.emptyList();

        float wineStrength = info.getFloat("wineStrength");
        return Arrays.asList(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.3 * wineStrength));
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(NBTTagCompound info)
    {
        if (isVinegar(info))
            return null;

        float wineStrength = info.getFloat("wineStrength");

        if (wineStrength < 0.3f)
        {
            int foodLevel = MathHelper.floor_float(IvMathHelper.zeroToOne(wineStrength, 0.0f, 0.3f) * 3.0f + 0.5f);
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
        if (isVinegar(info))
            return "vinegar";

        return "quality" + (MathHelper.floor_float(info.getFloat("wineStrength") * 6.0f + 0.5f));
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

    public boolean isVinegar(NBTTagCompound info)
    {
        return info.getFloat("vinegarProgress") >= 1.0f;
    }

    @Override
    public DrinkInformation createDrinkInfo(ItemDrinkHolder drinkHolder, NBTTagCompound drinkInfo, int fillings, int timeFermented)
    {
        float oldStrength = drinkInfo.getFloat("wineStrength");
        float oldVinegarProgress = drinkInfo.getFloat("vinegarProgress");

        float fullStrength = oldStrength + (float) timeFermented / (float) PSConfig.ticksForFullWineFermentation;
        float strength = IvMathHelper.clamp(0.0f, fullStrength, 1.0f);
        int timeFermentedOverMax = Math.max(MathHelper.floor_float(fullStrength * PSConfig.ticksForFullWineFermentation) - PSConfig.ticksForFullWineFermentation, 0);
        float vinegarProgress = oldVinegarProgress;
        if (PSConfig.ticksUntilWineAcetification >= 0)
            vinegarProgress += (float)timeFermentedOverMax / (float)PSConfig.ticksUntilWineAcetification;

        return createWineDrinkInfo(strength, vinegarProgress);
    }

    @Override
    public List<NBTTagCompound> creativeTabInfos(Item drinkHolder, CreativeTabs tabs)
    {
        List<NBTTagCompound> compounds = new ArrayList<>(8);
        for (int strength = 0; strength < 7; strength ++)
        {
            NBTTagCompound drinkInfo = new NBTTagCompound();
            drinkInfo.setFloat("wineStrength", strength / 6.0f);
            compounds.add(drinkInfo);
        }

        NBTTagCompound drinkInfo = new NBTTagCompound();
        drinkInfo.setFloat("wineStrength", 1.0f);
        drinkInfo.setFloat("vinegarProgress", 1.0f);
        compounds.add(drinkInfo);

        return compounds;
    }
}
