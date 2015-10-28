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

import java.util.ArrayList;
import java.util.Random;

public class BlockCoffea extends Block implements IGrowable, IvTilledFieldPlant
{
    IIcon[] bottomIcons = new IIcon[8];
    IIcon[] topIcons = new IIcon[4];

    public BlockCoffea()
    {
        super(Material.plants);
        float var3 = 0.375F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        this.setTickRandomly(true);

        this.disableStats();
        setStepSound(Block.soundTypeGrass);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote)
        {
            if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextFloat() < 0.1f)
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
        if (position > 1)
        {
            return -1;
        }

        return position == 0 ? ((7 << 1) | 0) : (3 << 1) | 1;
    }

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

    protected final void checkBlockCoordValid(World par1World, int par2, int par3, int par4)
    {
        if (!this.canBlockStay(par1World, par2, par3, par4))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), par1World.getBlockMetadata(par2, par3, par4));
            par1World.setBlock(par2, par3, par4, Blocks.air, 0, 3);
        }
    }

    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return this.canPlaceBlockAt(par1World, par2, par3, par4);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 1;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList<>();

        int stage = (meta >> 1);
        boolean above = (meta & 1) == 1;

        if (above)
            stage += 4;

        if (stage == 6 || stage == 7)
        {
            int countL = (world.rand.nextInt(3) + 1) * (stage - 5);
            for (int i = 0; i < countL; i++)
                drops.add(new ItemStack(PSItems.coffeaCherries, 1, 0));
        }

        return drops;
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);

        bottomIcons[0] = blockIcon;
        for (int i = 1; i < 8; i++)
        {
            bottomIcons[i] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "coffea" + i);
        }

        for (int i = 0; i < 4; i++)
        {
            topIcons[i] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "coffeaTop" + i);
        }
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        int stage = par2 >> 1;
        boolean above = (par2 & 1) == 1;

        return above ? topIcons[stage % topIcons.length] : bottomIcons[stage % bottomIcons.length];
    }

    public void growStep(World world, Random random, int x, int y, int z, boolean bonemeal)
    {
        int number = bonemeal ? random.nextInt(2) + 1 : 1;

        for (int i = 0; i < number; i++)
        {
            int plantSize = 1;
            while (y > 0 && world.getBlock(x, y - plantSize, z) == this)
                ++plantSize;

            int meta = world.getBlockMetadata(x, y, z);
            int stage = (meta >> 1);
            boolean above = (meta & 1) == 1;
            boolean freeOver = world.isAirBlock(x, y + 1, z) && plantSize < 2;

            if ((!above && stage < 7) || (above && stage < 3))
            {
                world.setBlockMetadataWithNotify(x, y, z, ((stage + 1) << 1) | (meta & 1), 3);
            }
            if (freeOver && !above && stage >= 3)
            {
                world.setBlock(x, y + 1, z, this, (0 << 1) | (1), 3);
            }
        }
    }

    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote)
    {
        // canGrow
        int plantSize = 1;
        while (y > 0 && world.getBlock(x, y - plantSize, z) == this)
            ++plantSize;

        int meta = world.getBlockMetadata(x, y, z);
        int stage = (meta >> 1);
        boolean above = (meta & 1) == 1;
        boolean freeOver = world.isAirBlock(x, y + 1, z) && plantSize < 2;

        if ((!above && stage < 7) || (above && stage < 3))
            return true;

        return freeOver && !above && stage >= 3;
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
