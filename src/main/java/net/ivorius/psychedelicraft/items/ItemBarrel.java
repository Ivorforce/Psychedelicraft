/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.items;

import net.ivorius.psychedelicraft.blocks.TileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class ItemBarrel extends ItemBlock
{
    public BarrelEntry[] entries = new BarrelEntry[50];

    public ItemBarrel(Block block)
    {
        super(block);

        maxStackSize = 16;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par3World.getBlock(par4, par5, par6) == Blocks.snow)
        {
            par5--;
        }

        if (par7 != 1)
        {
            return false;
        }

        par5++;

        int i1 = MathHelper.floor_double((par2EntityPlayer.rotationYaw * 4F) / 360F + 0.5D) & 3;

        if (par1ItemStack.getItemDamage() < entries.length && entries[par1ItemStack.getItemDamage()] != null)
        {
            par3World.setBlock(par4, par5, par6, field_150939_a, i1, 3);

            TileEntity tileEntity = par3World.getTileEntity(par4, par5, par6);
            if (tileEntity != null && tileEntity instanceof TileEntityBarrel)
            {
                TileEntityBarrel tileEntityBarrel = (TileEntityBarrel) tileEntity;
                tileEntityBarrel.setBarrelType(entries[par1ItemStack.getItemDamage()].placementType);
            }

            par1ItemStack.stackSize--;
        }

        return true;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        for (int i = 0; i < entries.length; i++)
        {
            if (entries[i] != null)
            {
                entries[i].icon = par1IconRegister.registerIcon(entries[i].iconName);
            }
        }
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        if (par1 < entries.length && entries[par1] != null)
        {
            return entries[par1].icon;
        }

        return entries[0].icon;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
                            List par3List)
    {
        for (int i = 0; i < entries.length; i++)
        {
            if (i < entries.length && entries[i] != null)
            {
                par3List.add(new ItemStack(par1, 1, i));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getItemDamage() < entries.length && entries[par1ItemStack.getItemDamage()] != null)
        {
            return super.getUnlocalizedName(par1ItemStack) + "." + entries[par1ItemStack.getItemDamage()].displayName;
        }

        return super.getUnlocalizedName(par1ItemStack);
    }

    public static class BarrelEntry
    {
        public String displayName;

        public int placementType;

        public String iconName;
        public IIcon icon;

        public BarrelEntry(String displayName, int placementType, String iconName)
        {
            this.displayName = displayName;

            this.placementType = placementType;

            this.iconName = iconName;
        }
    }
}
