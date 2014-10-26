/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.ivtoolkit.blocks.IvTileEntityHelper;
import ivorius.ivtoolkit.tools.IvGsonHelper;
import ivorius.psychedelicraft.fluids.FluidDistillable;
import ivorius.psychedelicraft.fluids.FluidFermentable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.TileFluidHandler;

import static ivorius.psychedelicraft.fluids.FluidHelper.MILLIBUCKETS_PER_LITER;

/**
 * Created by lukas on 25.10.14.
 */
public class TileEntityDistillery extends TileFluidHandler
{
    public static final int DISTILLERY_CAPACITY = TileEntityFlask.FLASK_CAPACITY;

    public int direction;
    public int timeDistilled;

    public TileEntityDistillery()
    {
        tank = new FluidTank(DISTILLERY_CAPACITY);
    }

    @Override
    public void updateEntity()
    {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack != null && fluidStack.getFluid() instanceof FluidFermentable)
        {
            IFluidHandler destination = getDestinationFluidHandler();

            FluidDistillable fluidFermentable = (FluidDistillable) fluidStack.getFluid();
            int neededDistillationTime = fluidFermentable.distillationTime(fluidStack);

            if (neededDistillationTime >= 0 && destination != null)
            {
                if (timeDistilled >= neededDistillationTime)
                {
                    if (!worldObj.isRemote)
                    {
                        FluidStack leftover = fluidFermentable.distillStep(fluidStack);

                        FluidStack distilled = drain(ForgeDirection.EAST, fluidStack, true);
                        fill(ForgeDirection.EAST, leftover, true);
                        int spilled = destination.fill(getDestinationOrientation().getOpposite(), distilled, true);

                        timeDistilled = 0;

                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        markDirty();
                    }
                }
                else
                    timeDistilled ++;
            }
        }
    }

    public ForgeDirection getDestinationOrientation()
    {
        switch (direction)
        {
            case 0:
                return ForgeDirection.SOUTH;
            case 1:
                return ForgeDirection.WEST;
            case 2:
                return ForgeDirection.NORTH;
            case 3:
                return ForgeDirection.EAST;
            default:
                return ForgeDirection.EAST;
        }
    }

    public IFluidHandler getDestinationFluidHandler()
    {
        ForgeDirection to = getDestinationOrientation();
        TileEntity tileEntity = worldObj.getTileEntity(xCoord + to.offsetX, yCoord + to.offsetY, zCoord + to.offsetZ);
        return tileEntity instanceof IFluidHandler ? (IFluidHandler) tileEntity : null;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int fill = super.fill(from, resource, doFill);

        if (doFill)
        {
            double amountFilled = (double)fill / (double)tank.getFluidAmount();
            timeDistilled = MathHelper.floor_double(timeDistilled * (1.0 - amountFilled));

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        return fill;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        FluidStack drain = super.drain(from, resource, doDrain);

        if (doDrain)
        {
            if (tank.getFluidAmount() == 0)
                timeDistilled = 0;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        return drain;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        FluidStack drain = super.drain(from, maxDrain, doDrain);

        if (doDrain)
        {
            if (tank.getFluidAmount() == 0)
                timeDistilled = 0;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        return drain;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setInteger("direction", direction);
        nbttagcompound.setInteger("timeDistilled", timeDistilled);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        direction = nbttagcompound.getInteger("direction");
        timeDistilled = nbttagcompound.getInteger("timeDistilled");
    }

    public FluidStack containedFluid()
    {
        return tank.getFluid() != null ? tank.getFluid().copy() : null;
    }

    public int tankCapacity()
    {
        return tank.getCapacity();
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return IvTileEntityHelper.getStandardDescriptionPacket(this);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
    }
}
