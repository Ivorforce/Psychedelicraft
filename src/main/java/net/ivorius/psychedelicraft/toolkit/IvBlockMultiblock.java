/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 *
 * You are free to:
 *
 * Share — copy and redistribute the material in any medium or format
 * Adapt — remix, transform, and build upon the material
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 *
 * Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * NonCommercial — You may not use the material for commercial purposes, unless you have a permit by the creator.
 * ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.
 **************************************************************************************************/

package net.ivorius.psychedelicraft.toolkit;


import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class IvBlockMultiblock extends BlockContainer
{
    protected IvBlockMultiblock(Material par2Material)
    {
        super(par2Material);
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);
        if (tileEntity != null && tileEntity instanceof IvTileEntityMultiBlock)
        {
            IvTileEntityMultiBlock parent = (IvTileEntityMultiBlock) ((IvTileEntityMultiBlock) tileEntity).getParent();

            if (parent != null)
            {
                par1World.setBlock(parent.xCoord, parent.yCoord, parent.zCoord, Blocks.air, 0, 3);
            }

            if (((IvTileEntityMultiBlock) tileEntity).isParent())
            {
                if (!par1World.isRemote)
                {
                    this.parentBlockDropItem(par1World, (IvTileEntityMultiBlock) tileEntity, par2, par3, par4, par5, par6);
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    public abstract void parentBlockDropItem(World par1World, IvTileEntityMultiBlock tileEntity, int parentX, int parentY, int parentZ, Block block, int blockMeta);

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {

    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);

        if (!canBlockStay(par1World, par2, par3, par4))
        {
            par1World.setBlock(par2, par3, par4, Blocks.air, 0, 3);
        }
    }

    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        boolean isValidChild = true;
        TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);
        if (tileEntity != null && tileEntity instanceof IvTileEntityMultiBlock)
        {
            IvTileEntityMultiBlock parent = (IvTileEntityMultiBlock) ((IvTileEntityMultiBlock) tileEntity).getParent();

            if (!((IvTileEntityMultiBlock) tileEntity).isParent() && parent == null)
            {
                isValidChild = false;
            }
        }

        return isValidChild && super.canBlockStay(par1World, par2, par3, par4);
    }
}
