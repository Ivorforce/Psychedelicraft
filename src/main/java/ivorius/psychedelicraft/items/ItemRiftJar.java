/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.blocks.TileEntityRiftJar;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemRiftJar extends ItemBlock
{
    public ItemRiftJar(Block block)
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

        par3World.setBlock(par4, par5, par6, field_150939_a, i1, 3);

        TileEntity tileEntity = par3World.getTileEntity(par4, par5, par6);
        if (tileEntity != null && tileEntity instanceof TileEntityRiftJar)
        {
            TileEntityRiftJar tileEntityJar = (TileEntityRiftJar) tileEntity;

            tileEntityJar.currentRiftFraction = getRiftFraction(par1ItemStack);
        }

        par1ItemStack.stackSize--;

        return true;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(createFilledRiftJar(0.0f, par1));
        par3List.add(createFilledRiftJar(0.25f, par1));
        par3List.add(createFilledRiftJar(0.55f, par1));
        par3List.add(createFilledRiftJar(0.75f, par1));
        par3List.add(createFilledRiftJar(0.9f, par1));
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

        if (getRiftFraction(par1ItemStack) > 0.0f)
        {
            par3List.add(StatCollector.translateToLocal("item.riftJar." + getUnlocalizedFractionName(getRiftFraction(par1ItemStack))));
        }
    }

    public float getRiftFraction(ItemStack itemStack)
    {
        return itemStack.hasTagCompound() ? itemStack.getTagCompound().getFloat("riftFraction") : 0.0f;
    }

    private String getUnlocalizedFractionName(float fraction)
    {
        if (fraction <= 0.0f)
        {
            return "empty";
        }
        else if (fraction < 0.4f)
        {
            return "slightlyFilled";
        }
        else if (fraction < 0.6f)
        {
            return "halfFilled";
        }
        else if (fraction < 0.8f)
        {
            return "full";
        }
        else
        {
            return "overflowing";
        }
    }

    public static ItemStack createFilledRiftJar(float riftFraction, Item item)
    {
        ItemStack stack = new ItemStack(item);

        if (riftFraction > 0.0f)
        {
            stack.setTagInfo("riftFraction", new NBTTagFloat(riftFraction));
        }

        return stack;
    }
}
