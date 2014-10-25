/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.ivtoolkit.blocks.IvTileEntityHelper;
import ivorius.psychedelicraft.fluids.FluidFermentable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.TileFluidHandler;

import static ivorius.psychedelicraft.fluids.FluidHelper.MILLIBUCKETS_PER_LITER;

public class TileEntityBarrel extends TileFluidHandler
{
    public static final int BARREL_CAPACITY = MILLIBUCKETS_PER_LITER * 16;

    public int barrelWoodType;

    public int timeFermented;

    public float tapRotation = 0.0f;
    public int timeLeftTapOpen = 0;

    public TileEntityBarrel()
    {
        tank = new FluidTank(BARREL_CAPACITY);
    }

    @Override
    public void updateEntity()
    {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack != null && fluidStack.getFluid() instanceof FluidFermentable)
        {
            FluidFermentable fluidFermentable = (FluidFermentable) fluidStack.getFluid();
            int neededFermentationTime = fluidFermentable.fermentationTime(fluidStack);

            if (neededFermentationTime >= 0 && timeFermented >= neededFermentationTime)
            {
                if (!worldObj.isRemote)
                {
                    fluidFermentable.fermentStep(fluidStack);
                    timeFermented = 0;

                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    markDirty();
                }
            }
            else
                timeFermented ++;
        }

        if (timeLeftTapOpen > 0)
        {
            timeLeftTapOpen--;
        }

        if (timeLeftTapOpen > 0 && tapRotation < 3.141f * 0.5f)
        {
            tapRotation += 3.141f * 0.1f;
        }
        if (timeLeftTapOpen == 0 && tapRotation > 0.0f)
        {
            tapRotation -= 3.141f * 0.1f;
        }
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int fill = super.fill(from, resource, doFill);

        if (doFill)
        {
            double amountFilled = (double)fill / (double)tank.getFluidAmount();
            timeFermented = MathHelper.floor_double(timeFermented * (1.0 - amountFilled));

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
                timeFermented = 0;

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
                timeFermented = 0;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        return drain;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setInteger("barrelWoodType", barrelWoodType);

        nbttagcompound.setInteger("timeLeftTapOpen", timeLeftTapOpen);
        nbttagcompound.setFloat("tapRotation", tapRotation);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        barrelWoodType = nbttagcompound.getInteger("barrelWoodType");

        timeLeftTapOpen = nbttagcompound.getInteger("timeLeftTapOpen");
        tapRotation = nbttagcompound.getFloat("tapRotation");
    }

    public FluidStack containedFluid()
    {
        return tank.getFluid() != null ? tank.getFluid().copy() : null;
    }

    public int getBlockRotation()
    {
        return getBlockMetadata();
    }

    public float getTapRotation()
    {
        return tapRotation;
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
