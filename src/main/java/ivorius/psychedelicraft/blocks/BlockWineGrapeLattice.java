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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockWineGrapeLattice extends Block implements IvBonemealCompatibleBlock
{
    public IIcon currentIcon;

    public IIcon leavesIcons[] = new IIcon[4];

    public BlockWineGrapeLattice()
    {
        super(Material.wood);
        setTickRandomly(true);
    }

    @Override
    public int getRenderType()
    {
        return Psychedelicraft.blockWineGrapeLatticeRenderType;
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
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3;

        if (l == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 3);
        }
        if (l == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 3);
        }
        if (l == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 3);
        }
        if (l == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 3);
        }

        super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, par6ItemStack);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int m = iblockaccess.getBlockMetadata(i, j, k);

        float d = 0.1F;
        if ((m & 1) == 0)
        {
            setBlockBounds(0f, 0f, 0.5f - d, 1f, 1f, 0.5f + d);
        }
        else
        {
            setBlockBounds(0.5f - d, 0f, 0F, 0.5F + d, 1f, 1f);
        }
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        float d = 0.1F;
        setBlockBounds(0.5F - d, 0f, 0f, 0.5f + d, 1f, 1f);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        if (!world.isRemote && l >> 1 > 0 && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == Items.shears)
        {
            if (l >> 1 == 4)
            {
                dropBlockAsItem(world, i, j, k, new ItemStack(PSItems.wineGrapes, world.rand.nextInt(3) + 1));
            }

            world.setBlock(i, j, k, this, (l & 1 | 2 << 1), 3);

            entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);
        }
        else
        {
            super.harvestBlock(world, entityplayer, i, j, k, l);
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);

        leavesIcons[0] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "grapeLeaves0");
        leavesIcons[1] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "grapeLeaves1");
        leavesIcons[2] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "grapeLeaves2");
        leavesIcons[3] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "grapeLeaves3");

        currentIcon = getDefaultBlockIcon();
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        return currentIcon;
    }

    public IIcon getDefaultBlockIcon()
    {
        return blockIcon;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        super.updateTick(world, i, j, k, random);

        if (world.getBlockLightValue(i, j + 1, k) >= 9)
        {
            if (this.canGrow(world, i, j, k) && random.nextInt(35) == 0)
            {
                this.growStep(world, i, j, k, false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        return IvBonemealHelper.tryGrowing(par1World, par2, par3, par4, par5EntityPlayer, this);

    }

    @Override
    public void growStep(World par1World, int x, int y, int z, boolean bonemeal)
    {
        if (!bonemeal || par1World.rand.nextInt(2) == 0)
        {
            int m = par1World.getBlockMetadata(x, y, z);

            int d = m & 1;
            int g = m >> 1;

            if (g < 4 && g > 0)
            {
                g++;

                par1World.setBlockMetadataWithNotify(x, y, z, d | (g << 1), 3);
            }
        }
    }

    @Override
    public boolean canGrow(World par1World, int x, int y, int z)
    {
        int m = par1World.getBlockMetadata(x, y, z);

        int d = m & 1;
        int g = m >> 1;

        if (g < 4 && g > 0)
        {
            return true;
        }

        return false;
    }
}
