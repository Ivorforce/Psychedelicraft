/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.DrugHelper;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemGlassChalice extends Item
{
    public IIcon filledIcon;

    public ItemGlassChalice()
    {
        super();

        setMaxStackSize(16);
        setHasSubtypes(true);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        int wineStrength = par1ItemStack.getItemDamage() - 1;

        if (wineStrength < 14)
        {
            int foodLevel = wineStrength < 5 ? 1 : 0;

            if (foodLevel > 0)
            {
                par3EntityPlayer.getFoodStats().addStats(foodLevel, foodLevel / 10f);
            }

            if (wineStrength > 2)
            {
                DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.02 * (par1ItemStack.getItemDamage() - 2)));
            }
        }

        par1ItemStack.stackSize--;

        par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(this, 1, 0));

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.getItemDamage() > 0)
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);

        filledIcon = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "glassChaliceWine");
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        if (par1 > 0)
        {
            return filledIcon;
        }

        return super.getIconFromDamage(par1);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getItemDamage() == 0)
        {
            return super.getUnlocalizedName(par1ItemStack);
        }

        return super.getUnlocalizedName(par1ItemStack) + ".wine" + (par1ItemStack.getItemDamage() - 1);
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 9; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4 * 2));
        }
    }
}
