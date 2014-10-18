/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.blocks.PSBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWineGrapes extends ItemFoodSpecial
{
    public ItemWineGrapes(int healAmount, float saturation, boolean canFeedWolves, int eatSpeed)
    {
        super(healAmount, saturation, canFeedWolves, eatSpeed);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else
        {
            if (par3World.getBlock(par4, par5, par6) == PSBlocks.wineGrapeLattice && (par3World.getBlockMetadata(par4, par5, par6) >> 1) == 0)
            {
                int m = par3World.getBlockMetadata(par4, par5, par6);
                par3World.setBlockMetadataWithNotify(par4, par5, par6, m | 1 << 1, 3);

                par1ItemStack.stackSize--;

                return true;
            }

            return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
        }
    }
}
