/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import java.util.Random;

public class BlockPeyote extends BlockBush implements IGrowable, ITileEntityProvider
{
    public BlockPeyote()
    {
        super(Material.plants);
        float var3 = 0.2F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setStepSound(soundTypeGrass);
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityPeyote();
    }

    @Override
    public void dropBlockAsItemWithChance(World par1World, int x, int y, int z, int meta, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            for (int i = 0; i < meta + 1; i++)
            {
                this.dropBlockAsItem(par1World, x, y, z, new ItemStack(PSBlocks.peyote, 1, 0));
            }
        }
    }

    public void growStep(World world, Random random, int x, int y, int z, boolean bonemeal)
    {
        int meta = world.getBlockMetadata(x, y, z);

        if (meta >= 3)
        {
            byte var6 = 4;
            int var8;
            int var9;
            int var10;

            var8 = x + random.nextInt(3) - 1;
            var9 = y + random.nextInt(2) - random.nextInt(2);
            var10 = z + random.nextInt(3) - 1;

            for (int var11 = 0; var11 < 4; ++var11)
            {
                if (world.isAirBlock(var8, var9, var10) && this.canBlockStay(world, var8, var9, var10))
                {
                    x = var8;
                    y = var9;
                    z = var10;
                }

                var8 = x + random.nextInt(3) - 1;
                var9 = y + random.nextInt(2) - random.nextInt(2);
                var10 = z + random.nextInt(3) - 1;
            }

            if (world.isAirBlock(var8, var9, var10) && this.canBlockStay(world, var8, var9, var10))
            {
                world.setBlock(var8, var9, var10, this, 0, 3);
            }
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
        }
    }

    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote)
    {
        return true;
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

    @Override
    protected boolean canPlaceBlockOn(Block block)
    {
        return block.isNormalCube();
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Desert;
    }
}
