package net.ivorius.psychedelicraft.blocks;

import net.ivorius.psychedelicraft.toolkit.IvTileEntityHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBarrel extends TileEntity
{
    public int barrelType;

    public float currentItemDamage;
    public int currentContainedItems;

    public float tapRotation = 0.0f;
    public int timeLeftTapOpen = 0;

    public TileEntityBarrel()
    {
        currentItemDamage = 0.0f;
        currentContainedItems = 0;
    }

    public void setBarrelType(int type)
    {
        barrelType = type;

        currentItemDamage = this.getBarrelType().startMetadata;
        currentContainedItems = this.getBarrelType().containedItemQuantity;
    }

    @Override
    public void updateEntity()
    {
        currentItemDamage += 1.0f / 8000.0f;
        if (currentItemDamage > getBarrelType().maxMetadata)
        {
            currentItemDamage = getBarrelType().maxMetadata;
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
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setInteger("barrelType", barrelType);

        nbttagcompound.setFloat("currentItemDamage", currentItemDamage);

        nbttagcompound.setInteger("currentContainedItems", currentContainedItems);

        nbttagcompound.setInteger("timeLeftTapOpen", timeLeftTapOpen);
        nbttagcompound.setFloat("tapRotation", tapRotation);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        this.setBarrelType(nbttagcompound.getInteger("barrelType"));

        currentItemDamage = nbttagcompound.getFloat("currentItemDamage");

        currentContainedItems = nbttagcompound.getInteger("currentContainedItems");

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

    public BlockBarrel.BarrelEntry getBarrelType()
    {
        return ((BlockBarrel) PSBlocks.blockBarrel).entries[barrelType];
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
