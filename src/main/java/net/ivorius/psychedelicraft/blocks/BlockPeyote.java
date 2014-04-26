/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockPeyote extends BlockContainer implements IvBonemealCompatibleBlock
{
    public BlockPeyote()
    {
        super(Material.plants);
        float var3 = 0.2F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        this.checkFlowerChange(par1World, par2, par3, par4);

        if (par5Random.nextInt(15) == 0)
        {
            this.growStep(par1World, par2, par3, par4, false);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && this.canBlockStay(par1World, par2, par3, par4);
    }

    protected boolean canThisPlantGrowOnThisBlockID(Block par1)
    {
        return par1.isNormalCube();
    }

    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        if (par3 >= 0 && par3 < 256)
        {
            Block var5 = par1World.getBlock(par2, par3 - 1, par4);
            return this.canThisPlantGrowOnThisBlockID(var5);
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        this.checkFlowerChange(par1World, par2, par3, par4);
    }

    protected final void checkFlowerChange(World par1World, int par2, int par3, int par4)
    {
        if (!this.canBlockStay(par1World, par2, par3, par4))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityPeyote();
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (IvBonemealHelper.tryGrowing(par1World, par2, par3, par4, par5EntityPlayer, this))
        {
            return true;
        }

        return false;
    }

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            int meta = par1World.getBlockMetadata(par2, par3, par4);

            for (int i = 0; i < meta + 1; i++)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(PSBlocks.blockPeyote, 1, 0));
            }
        }
    }

    @Override
    public void growStep(World par1World, int x, int y, int z, boolean bonemeal)
    {
        int meta = par1World.getBlockMetadata(x, y, z);

        if (meta >= 3)
        {
            byte var6 = 4;
            int var8;
            int var9;
            int var10;

            var8 = x + par1World.rand.nextInt(3) - 1;
            var9 = y + par1World.rand.nextInt(2) - par1World.rand.nextInt(2);
            var10 = z + par1World.rand.nextInt(3) - 1;

            for (int var11 = 0; var11 < 4; ++var11)
            {
                if (par1World.isAirBlock(var8, var9, var10) && this.canBlockStay(par1World, var8, var9, var10))
                {
                    x = var8;
                    y = var9;
                    z = var10;
                }

                var8 = x + par1World.rand.nextInt(3) - 1;
                var9 = y + par1World.rand.nextInt(2) - par1World.rand.nextInt(2);
                var10 = z + par1World.rand.nextInt(3) - 1;
            }

            if (par1World.isAirBlock(var8, var9, var10) && this.canBlockStay(par1World, var8, var9, var10))
            {
                par1World.setBlock(var8, var9, var10, this, 0, 3);
            }
        }
        else
        {
            par1World.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
        }
    }

    @Override
    public boolean canGrow(World par1World, int x, int y, int z)
    {
        return true;
    }
}
