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
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public interface IDrink
{
    List<DrugInfluence> getDrugInfluences(NBTTagCompound info);

    Pair<Integer, Float> getFoodLevel(NBTTagCompound info);

    void applyToEntity(NBTTagCompound info, EntityLivingBase entityLivingBase, World world);

    String getSpecialTranslationKey(NBTTagCompound info);

    @SideOnly(Side.CLIENT)
    void registerItemIcons(IIconRegister iconRegister);

    @SideOnly(Side.CLIENT)
    IIcon getDrinkIcon(NBTTagCompound info);

    DrinkInformation createDrinkInfo(ItemDrinkHolder drinkHolder, NBTTagCompound drinkInfo, int fillings, int timeFermented);

    List<NBTTagCompound> creativeTabInfos(Item drinkHolder, CreativeTabs tabs);
}
