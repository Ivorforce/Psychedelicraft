/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.blocks;

import ivorius.ivtoolkit.blocks.IvTileEntityMultiBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by lukas on 31.10.14.
 */
public class TileEntityMultiblockFluidHandler extends IvTileEntityMultiBlock implements IFluidHandler
{
    protected FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        tank.setFluid(null); // Doesn't override if empty >.>
        tank.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tank.writeToNBT(tag);
    }

    /* IFluidHandler */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (!isParent())
            return ((TileEntityMultiblockFluidHandler) getParent()).fill(from, resource, doFill);

        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (!isParent())
            return ((TileEntityMultiblockFluidHandler) getParent()).drain(from, resource, doDrain);

        if (resource == null || !resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }
        return tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (!isParent())
            return ((TileEntityMultiblockFluidHandler) getParent()).drain(from, maxDrain, doDrain);

        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (!isParent())
            return ((TileEntityMultiblockFluidHandler) getParent()).canFill(from, fluid);

        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if (!isParent())
            return ((TileEntityMultiblockFluidHandler) getParent()).canDrain(from, fluid);

        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        if (!isParent())
            return ((TileEntityMultiblockFluidHandler) getParent()).getTankInfo(from);

        return new FluidTankInfo[]{tank.getInfo()};
    }
}
