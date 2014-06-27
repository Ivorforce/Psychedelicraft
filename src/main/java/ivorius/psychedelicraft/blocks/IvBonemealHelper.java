/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class IvBonemealHelper
{
    public static boolean tryGrowing(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, IvBonemealCompatibleBlock block)
    {
        ItemStack var2 = par5EntityPlayer.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() == Items.dye && var2.getItemDamage() == 15)
        {
            if (!par1World.isRemote && block.canGrow(par1World, par2, par3, par4))
            {
                if (!par5EntityPlayer.capabilities.isCreativeMode)
                {
                    if (--var2.stackSize == 0)
                    {
                        par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
                    }
                }

                block.growStep(par1World, par2, par3, par4, true);

                if (!par1World.isRemote)
                {
                    par1World.playAuxSFX(2005, par2, par3, par4, 0);
                }
            }

            return true;
        }

        return false;
    }
}
