/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.ivtoolkit.blocks.IvTileEntityHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.TileFluidHandler;

import static ivorius.psychedelicraft.fluids.FluidHelper.MILLIBUCKETS_PER_LITER;

/**
 * Created by lukas on 25.10.14.
 */
public class TileEntityFlask extends TileFluidHandler
{
    public static final int FLASK_CAPACITY = MILLIBUCKETS_PER_LITER * 8;

    public TileEntityFlask()
    {
        tank = new FluidTank(FLASK_CAPACITY);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int fill = super.fill(from, resource, doFill);

        if (doFill)
        {
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
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        return drain;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        tank.setFluid(null); // Doesn't override if empty >.>
        super.readFromNBT(tag);
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
