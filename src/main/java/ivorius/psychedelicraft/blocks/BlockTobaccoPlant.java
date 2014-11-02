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

public class BlockTobaccoPlant extends Block implements IGrowable, IvTilledFieldPlant
{
    IIcon[] icons = new IIcon[8];

    public BlockTobaccoPlant()
    {
        super(Material.plants);
        float var3 = 0.375F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        this.setTickRandomly(true);

        this.setCreativeTab(null);
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
                    this.growStep(world, random, x, y, z, false);
            }
        }
    }

    @Override
    public int getMaxMetadata(int position)
    {
        if (position > 1)
            return -1;

        return (7 << 1) | (position == 0 ? 0 : 1);
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

        int countL = world.rand.nextInt(meta / 3 + 1) + meta / 5;
        for (int i = 0; i < countL; i++)
            drops.add(new ItemStack(PSItems.tobaccoLeaf, 1, 0));

        int countS = meta / 8;
        for (int i = 0; i < countS; i++)
            drops.add(new ItemStack(PSItems.tobaccoSeeds, 1, 0));

        return drops;
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);

        icons[0] = blockIcon;
        icons[1] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "tobaccoPlant1");
        icons[2] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "tobaccoPlant2");
        icons[3] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "tobaccoPlant3");

        icons[4] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "tobaccoPlantTop");
        icons[5] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "tobaccoPlant1Top");
        icons[6] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "tobaccoPlant2Top");
        icons[7] = par1IconRegister.registerIcon(Psychedelicraft.modBase + "tobaccoPlant3Top");
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        int stage = meta >> 1;
        boolean above = (meta & 1) == 1;

        return icons[(stage / 2) + (above ? 4 : 0)];
    }

    public void growStep(World world, Random random, int x, int y, int z, boolean bonemeal)
    {
        int number = bonemeal ? random.nextInt(2) + 1 : 1;

        for (int i = 0; i < number; i++)
        {
            int plantSize = 1;
            while (world.getBlock(x, y - plantSize, z) == this)
                ++plantSize;

            int meta = world.getBlockMetadata(x, y, z);
            int stage = meta >> 1;
            boolean freeOver = world.isAirBlock(x, y + 1, z) && plantSize < 2;

            if (stage < 7)
            {
                world.setBlockMetadataWithNotify(x, y, z, ((stage + 1) << 1) | (meta & 1), 3);
            }
            if (freeOver && stage == 7)
            {
                world.setBlock(x, y + 1, z, this, (0 << 1) | (1), 3);
            }
        }
    }

    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote)
    {
        int plantSize = 1;
        while (world.getBlock(x, y - plantSize, z) == this)
            ++plantSize;

        int meta = world.getBlockMetadata(x, y, z);
        int stage = meta >> 1;
        boolean freeOver = world.isAirBlock(x, y + 1, z) && plantSize < 2;

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
