/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCannabisPlant extends Block implements IGrowable, IvTilledFieldPlant
{
    public IIcon[] textures = new IIcon[3];

    public BlockCannabisPlant()
    {
        super(Material.plants);
        float var3 = 0.375F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        this.setTickRandomly(true);

        this.disableStats();
        setStepSound(Block.soundTypeGrass);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote)
        {
            if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextFloat() < 0.12f)
            {
                if (this.func_149851_a(world, x, y, z, world.isRemote))
                {
                    this.growStep(world, random, x, y, z, false);
                }
            }
        }
    }

    @Override
    public int getMaxMetadata(int position)
    {
        if (position > 2)
        {
            return -1;
        }

        return (position == 2) ? 11 : 15;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        Block var5 = par1World.getBlock(par2, par3 - 1, par4);
        return var5 == this || var5 == Blocks.grass || var5 == Blocks.dirt || var5 == Blocks.farmland;
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        this.checkBlockCoordValid(par1World, par2, par3, par4);
    }

    /**
     * Checks if current block pos is valid, if not, breaks the block as dropable item. Used for reed and cactus.
     */
    protected final void checkBlockCoordValid(World par1World, int par2, int par3, int par4)
    {
        if (!this.canBlockStay(par1World, par2, par3, par4))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), par1World.getBlockMetadata(par2, par3, par4));
            par1World.setBlock(par2, par3, par4, Blocks.air, 0, 3);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return this.canPlaceBlockAt(par1World, par2, par3, par4);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return 1;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float par6, int par7)
    {
        if (!world.isRemote)
        {
            int countB = world.rand.nextInt(meta / 6 + 1);
            for (int i = 0; i < countB; i++)
            {
                this.dropBlockAsItem(world, x, y, z, new ItemStack(PSItems.cannabisBuds, 1, 0));
            }

            int countL = world.rand.nextInt(meta / 5 + 1) + meta / 6;
            for (int i = 0; i < countL; i++)
            {
                this.dropBlockAsItem(world, x, y, z, new ItemStack(PSItems.cannabisLeaf, 1, 0));
            }

            int countS = meta / 8;
            for (int i = 0; i < countS; i++)
            {
                this.dropBlockAsItem(world, x, y, z, new ItemStack(PSItems.cannabisSeeds, 1, 0));
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);

        textures[0] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "cannabisPlant1");
        textures[1] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "cannabisPlant2");
        textures[2] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "cannabisPlant3");
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        if (par2 < 4)
        {
            return super.getIcon(par1, par2);
        }
        else if (par2 < 8)
        {
            return textures[0];
        }
        else if (par2 < 12)
        {
            return textures[1];
        }
        else if (par2 < 16)
        {
            return textures[2];
        }

        return super.getIcon(par1, par2);
    }

    public void growStep(World par1World, Random random, int x, int y, int z, boolean bonemeal)
    {
        int number = bonemeal ? random.nextInt(4) + 1 : 1;

        for (int i = 0; i < number; i++)
        {
            int var6;

            for (var6 = 1; par1World.getBlock(x, y - var6, z) == this; ++var6)
            {

            }

            int m = par1World.getBlockMetadata(x, y, z);
            boolean freeOver = par1World.isAirBlock(x, y + 1, z) && var6 < 3;

            if ((m < 15 && freeOver) || (!freeOver && m < 11))
            {
                par1World.setBlockMetadataWithNotify(x, y, z, m + 1, 3);
            }
            else if (par1World.isAirBlock(x, y + 1, z))
            {
                if (freeOver && m == 15)
                {
                    par1World.setBlock(x, y + 1, z, this, 0, 3);
                }
            }
        }
    }

    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote)
    {
        int plantSize = 1;
        while (world.getBlock(x, y - plantSize, z) == this)
            ++plantSize;

        int m = world.getBlockMetadata(x, y, z);
        boolean freeOver = plantSize < 3;

        if ((m < 15 && freeOver) || (!freeOver && m < 11))
        {
            return true;
        }
        else if (world.isAirBlock(x, y + 1, z))
        {
            if (freeOver && m == 15)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean func_149852_a(World world, Random random, int x, int y, int z)
    {
        // shouldGrow
        return true;
    }

    @Override
    public void func_149853_b(World world, Random random, int x, int y, int z)
    {
        // grow
        growStep(world, random, x, y, z, true);
    }
}
