/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.blocks;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTobaccoPlant extends Block implements IvBonemealCompatibleBlock, IvTilledFieldPlant
{
    IIcon[] icons = new IIcon[8];

    public BlockTobaccoPlant()
    {
        super(Material.plants);
        float var3 = 0.375F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        this.setTickRandomly(true);

        this.setCreativeTab((CreativeTabs) null);
        this.disableStats();
        setStepSound(Block.soundTypeGrass);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 && par5Random.nextFloat() < 0.1f)
            {
                if (this.canGrow(par1World, par2, par3, par4))
                {
                    this.growStep(par1World, par2, par3, par4, false);
                }
            }
        }
    }

    @Override
    public int getMaxMetadata(int position)
    {
        if (position > 1)
        {
            return -1;
        }

        return (7 << 1) | (position == 0 ? 0 : 1);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        Block var5 = par1World.getBlock(par2, par3 - 1, par4);
        return var5 == this ? true : (var5 != Blocks.grass && var5 != Blocks.dirt && var5 != Blocks.farmland ? false : true);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor block
     */
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
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            int countL = par1World.rand.nextInt(par5 / 3 + 1) + par5 / 5;
            for (int i = 0; i < countL; i++)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(PSItems.itemTobaccoLeaf, 1, 0));
            }

            int countS = par5 / 8;
            for (int i = 0; i < countS; i++)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(PSItems.itemTobaccoSeeds, 1, 0));
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);

        icons[0] = blockIcon;
        icons[1] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "tobaccoPlant1");
        icons[2] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "tobaccoPlant2");
        icons[3] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "tobaccoPlant3");

        icons[4] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "tobaccoPlantTop");
        icons[5] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "tobaccoPlant1Top");
        icons[6] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "tobaccoPlant2Top");
        icons[7] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "tobaccoPlant3Top");
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        int stage = meta >> 1;
        boolean above = (meta & 1) == 1;

        return icons[(stage / 2) + (above ? 4 : 0)];
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
    public void growStep(World par1World, int x, int y, int z, boolean bonemeal)
    {
        int number = bonemeal ? par1World.rand.nextInt(2) + 1 : 1;

        for (int i = 0; i < number; i++)
        {
            int var6;

            for (var6 = 1; par1World.getBlock(x, y - var6, z) == this; ++var6)
            {

            }

            int meta = par1World.getBlockMetadata(x, y, z);
            int stage = meta >> 1;
            boolean freeOver = par1World.isAirBlock(x, y + 1, z) && var6 < 2;

            if (stage < 7)
            {
                par1World.setBlockMetadataWithNotify(x, y, z, ((stage + 1) << 1) | (meta & 1), 3);
            }
            if (freeOver && stage == 7)
            {
                par1World.setBlock(x, y + 1, z, this, (0 << 1) | (1), 3);
            }
        }
    }

    @Override
    public boolean canGrow(World par1World, int x, int y, int z)
    {
        int var6;

        for (var6 = 1; par1World.getBlock(x, y - var6, z) == this; ++var6)
        {

        }

        int meta = par1World.getBlockMetadata(x, y, z);
        int stage = meta >> 1;
        boolean freeOver = par1World.isAirBlock(x, y + 1, z) && var6 < 2;

        if (stage < 7)
        {
            return true;
        }
        else if (freeOver && stage == 7)
        {
            return true;
        }

        return false;
    }
}
