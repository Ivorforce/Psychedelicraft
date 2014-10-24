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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.TileFluidHandler;

import static ivorius.psychedelicraft.fluids.FluidHelper.MILLIBUCKETS_PER_LITER;

public class TileEntityBarrel extends TileFluidHandler
{
    public static final int BARREL_CAPACITY = MILLIBUCKETS_PER_LITER * 16;

    public int barrelWoodType;

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
        if (fluidStack instanceof FluidFermentable)
            ((FluidFermentable) fluidStack).updateFermenting(fluidStack);

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
        return tank.getFluid();
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
