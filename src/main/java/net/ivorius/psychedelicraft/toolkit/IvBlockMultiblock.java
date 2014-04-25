/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;


import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public abstract class IvBlockMultiblock extends BlockContainer
{
    protected IvBlockMultiblock(Material par2Material)
    {
        super(par2Material);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int blockMeta)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        int[][] toDestroy = null;

        if (tileEntity instanceof IvTileEntityMultiBlock)
        {
            IvTileEntityMultiBlock tileEntityMultiBlock = (IvTileEntityMultiBlock) tileEntity;

            if (tileEntityMultiBlock.isParent())
            {
                if (!world.isRemote)
                    this.parentBlockDropItemContents(world, tileEntityMultiBlock, x, y, z, block, blockMeta);
            }

            if (!tileEntityMultiBlock.isParent())
                toDestroy = new int[][]{tileEntityMultiBlock.getActiveParentCoords()};
        }

        super.breakBlock(world, x, y, z, block, blockMeta);

        if (toDestroy != null)
        {
            for (int[] coords : toDestroy)
            {
                if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof IvTileEntityMultiBlock)
                    world.setBlockToAir(coords[0], coords[1], coords[2]);
            }
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        IvTileEntityMultiBlock tileEntityMultiBlock = getValidatedTotalParent(this, world, x, y, z);

        if (tileEntityMultiBlock != null)
        {
            int blockMeta = world.getBlockMetadata(x, y, z);

            if (!world.isRemote && canHarvestBlock(player, blockMeta))
                this.parentBlockHarvestItem(world, tileEntityMultiBlock, x, y, z, this, blockMeta);
        }

        return super.removedByPlayer(world, player, x, y, z);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
    {
        IvTileEntityMultiBlock tileEntityMultiBlock = getValidatedTotalParent(this, world, x, y, z);

        if (tileEntityMultiBlock != null)
        {
            int blockMeta = world.getBlockMetadata(x, y, z);

            if (!world.isRemote)
                this.parentBlockHarvestItem(world, tileEntityMultiBlock, x, y, z, this, blockMeta);
        }

        super.onBlockExploded(world, x, y, z, explosion);
    }

    public void parentBlockDropItemContents(World world, IvTileEntityMultiBlock tileEntity, int x, int y, int z, Block block, int metadata) {}

    public void parentBlockHarvestItem(World world, IvTileEntityMultiBlock tileEntity, int x, int y, int z, Block block, int metadata) {}

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {

    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);

        validateMultiblock(this, par1World, par2, par3, par4);
    }

    public static boolean validateMultiblock(Block block, World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z) != block)
            return false;

        boolean isValidChild = false;

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IvTileEntityMultiBlock)
        {
            IvTileEntityMultiBlock tileEntityMultiBlock = (IvTileEntityMultiBlock) tileEntity;
            IvTileEntityMultiBlock parent = tileEntityMultiBlock.getParent();

            isValidChild = tileEntityMultiBlock.isParent() || (parent != null && parent.isParent());
        }

        if (!isValidChild)
        {
            world.setBlockToAir(x, y, z);
        }

        return isValidChild;
    }

    public static IvTileEntityMultiBlock getValidatedIfParent(Block block, World world, int x, int y, int z)
    {
        if (validateMultiblock(block, world, x, y, z))
        {
            IvTileEntityMultiBlock tileEntity = (IvTileEntityMultiBlock) world.getTileEntity(x, y, z);

            return tileEntity.isParent() ? tileEntity : null;
        }

        return null;
    }

    public static IvTileEntityMultiBlock getValidatedTotalParent(Block block, World world, int x, int y, int z)
    {
        if (validateMultiblock(block, world, x, y, z))
        {
            IvTileEntityMultiBlock tileEntity = (IvTileEntityMultiBlock) world.getTileEntity(x, y, z);

            return tileEntity.getTotalParent();
        }

        return null;
    }
}
