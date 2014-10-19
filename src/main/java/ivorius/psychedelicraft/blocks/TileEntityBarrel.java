/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.ivtoolkit.blocks.IvTileEntityHelper;
import ivorius.psychedelicraft.items.DrinkInformation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityBarrel extends TileEntity
{
    public int ticksExisted;

    public DrinkInformation containedDrink;

    public float tapRotation = 0.0f;
    public int timeLeftTapOpen = 0;

    @Override
    public void updateEntity()
    {
        ticksExisted++;

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

        nbttagcompound.setInteger("ticksExisted", ticksExisted);

        if (containedDrink != null)
           nbttagcompound.setTag("containedDrink", containedDrink.writeToNBT());

        nbttagcompound.setInteger("timeLeftTapOpen", timeLeftTapOpen);
        nbttagcompound.setFloat("tapRotation", tapRotation);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        ticksExisted = nbttagcompound.getInteger("ticksExisted");

        if (nbttagcompound.hasKey("containedDrink", Constants.NBT.TAG_COMPOUND))
            containedDrink = new DrinkInformation(nbttagcompound.getCompoundTag("containedDrink"));

        timeLeftTapOpen = nbttagcompound.getInteger("timeLeftTapOpen");
        tapRotation = nbttagcompound.getFloat("tapRotation");
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
