/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCoffea extends Block implements IvBonemealCompatibleBlock, IvTilledFieldPlant
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

        return position == 0 ? ((7 << 1) | 0) : (3 << 1) | 1;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        Block var5 = par1World.getBlock(par2, par3 - 1, par4);
        return var5 == this ? true : (var5 != Blocks.grass && var5 != Blocks.dirt && var5 != Blocks.farmland ? false : true);
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
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            int meta = par1World.getBlockMetadata(par2, par3, par4);
            int stage = (meta >> 1);
            boolean above = (meta & 1) == 1;

            if (above)
            {
                stage += 4;
            }

            if (stage == 6 || stage == 7)
            {
                int countL = (par1World.rand.nextInt(3) + 1) * (stage - 5);
                for (int i = 0; i < countL; i++)
                {
                    this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(PSItems.itemCoffeaCherries, 1, 0));
                }
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);

        bottomIcons[0] = blockIcon;
        for (int i = 1; i < 8; i++)
        {
            bottomIcons[i] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "coffea" + i);
        }

        for (int i = 0; i < 4; i++)
        {
            topIcons[i] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "coffeaTop" + i);
        }
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        int stage = par2 >> 1;
        boolean above = (par2 & 1) == 1;

        return above ? topIcons[stage] : bottomIcons[stage];
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
            int stage = (meta >> 1);
            boolean above = (meta & 1) == 1;
            boolean freeOver = par1World.isAirBlock(x, y + 1, z) && var6 < 2;

            if ((!above && stage < 7) || (above && stage < 3))
            {
                par1World.setBlockMetadataWithNotify(x, y, z, ((stage + 1) << 1) | (meta & 1), 3);
            }
            if (freeOver && !above && stage >= 3)
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
        int stage = (meta >> 1);
        boolean above = (meta & 1) == 1;
        boolean freeOver = par1World.isAirBlock(x, y + 1, z) && var6 < 2;

        if ((!above && stage < 7) || (above && stage < 3))
        {
            return true;
        }
        if (freeOver && !above && stage >= 3)
        {
            return true;
        }

        return false;
    }
}
