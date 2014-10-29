/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.gui.PSGuiHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Created by lukas on 27.10.14.
 */
public class BlockMashTub extends BlockContainer
{
    public BlockMashTub()
    {
        super(Material.wood);

        setStepSound(soundTypeWood);

        float size = 6.0f / 16.0f;
        float borderWidth = 1.0f / 16.0f;
        float height = 12.0f / 16.0f;

        this.setBlockBounds(0.5f - size - borderWidth, 0.0f, 0.5f - size - borderWidth, 0.5f + size + borderWidth, height, 0.5f + size + borderWidth);
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
        return Psychedelicraft.blockMashTubRenderType;
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        return Blocks.planks.getIcon(0, 0);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack stack)
    {
        int direction = MathHelper.floor_double((entityLivingBase.rotationYaw * 4F) / 360F + 0.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, direction, 3);

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityMashTub)
        {
            TileEntityMashTub tileEntityMashTub = (TileEntityMashTub) tileEntity;

            FluidStack fluidStack = stack.getItem() instanceof IFluidContainerItem ? ((IFluidContainerItem) stack.getItem()).getFluid(stack) : null;
            if (fluidStack != null)
                tileEntityMashTub.fill(ForgeDirection.UP, fluidStack, true);
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        if (willHarvest)
        {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityMashTub)
            {
                TileEntityMashTub tileEntityMashTub = (TileEntityMashTub) tileEntity;
                FluidStack fluidStack = tileEntityMashTub.drain(ForgeDirection.DOWN, TileEntityMashTub.MASH_TUB_CAPACITY, true);
                ItemStack stack = new ItemStack(this);

                if (fluidStack != null && fluidStack.amount > 0)
                    ((IFluidContainerItem) stack.getItem()).fill(stack, fluidStack, true);

                dropBlockAsItem(world, x, y, z, stack);
            }
        }

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_)
    {

    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityMashTub)
        {
            if (!world.isRemote)
                player.openGui(Psychedelicraft.instance, PSGuiHandler.fluidHandlerContainerID_UP, world, x, y, z);
        }

        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {

    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityMashTub();
    }
}
