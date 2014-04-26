/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 *
 * You are free to:
 *
 * Share — copy and redistribute the material in any medium or format
 * Adapt — remix, transform, and build upon the material
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 *
 * Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * NonCommercial — You may not use the material for commercial purposes, unless you have a permit by the creator.
 * ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.
 */

package ivorius.psychedelicraft.ivToolkit;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class IvTileEntityMultiBlock extends TileEntity
{
    public int[] parentCoords;
    public int direction;

    public double[] centerCoords = new double[]{0.5, 0.5, 0.5};
    public double[] centerCoordsSize = new double[]{0.5, 0.5, 0.5};

    public IvTileEntityMultiBlock()
    {

    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (isParent())
        {
            updateEntityParent();
        }
        else
        {
            updateEntityChild();
        }
    }

    public void updateEntityParent()
    {

    }

    public void updateEntityChild()
    {

    }

    public int[] getActiveParentCoords()
    {
        if (parentCoords == null)
        {
            return new int[]{xCoord, yCoord, zCoord};
        }

        return new int[]{xCoord + parentCoords[0], yCoord + parentCoords[1], zCoord + parentCoords[2]};
    }

    public double[] getActiveCenterCoords()
    {
        if (centerCoords == null)
        {
            return new double[]{xCoord + 0.5, yCoord + 0.5, zCoord + 0.5};
        }

        return new double[]{xCoord + centerCoords[0], yCoord + centerCoords[1], zCoord + centerCoords[2]};
    }

    public void becomeChild(TileEntity parent)
    {
        if (parent != null)
        {
            this.parentCoords = new int[]{parent.xCoord - this.xCoord, parent.yCoord - this.yCoord, parent.zCoord - this.zCoord};
        }
        else
        {
            this.parentCoords = null;
        }
    }

    public boolean isParent()
    {
        return parentCoords == null;
    }

    public IvTileEntityMultiBlock getTotalParent()
    {
        return isParent() ? this : getParent();
    }

    public IvTileEntityMultiBlock getParent()
    {
        if (parentCoords != null)
        {
            int[] parentCoords = getActiveParentCoords();
            TileEntity parentTileEntity = getWorldObj().getTileEntity(parentCoords[0], parentCoords[1], parentCoords[2]);

            if (parentTileEntity != null && this.getClass().equals(parentTileEntity.getClass()))
            {
                return (IvTileEntityMultiBlock) parentTileEntity;
            }
        }

        return null;
    }

    public float getDistanceToCenter()
    {
        return (float) IvMathHelper.distance(new double[]{xCoord + 0.5, yCoord + 0.5, zCoord + 0.5}, getActiveCenterCoords());
    }

    @Override
    public void readFromNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.readFromNBT(par1nbtTagCompound);

        direction = par1nbtTagCompound.getInteger("direction");

        centerCoords = IvNBTHelper.readDoubleArray("multiBlockCenter", par1nbtTagCompound);
        if (centerCoords == null) // Legacy
        {
            centerCoords = new double[3];

            if (par1nbtTagCompound.hasKey("centerCoords[0]"))
            {
                centerCoords[0] = par1nbtTagCompound.getDouble("centerCoords[0]") - xCoord;
            }
            else
            {
                centerCoords[0] = 0.5f;
            }

            if (par1nbtTagCompound.hasKey("centerCoords[1]"))
            {
                centerCoords[1] = par1nbtTagCompound.getDouble("centerCoords[1]") - yCoord;
            }
            else
            {
                centerCoords[1] = 0.5f;
            }

            if (par1nbtTagCompound.hasKey("centerCoords[2]"))
            {
                centerCoords[2] = par1nbtTagCompound.getDouble("centerCoords[2]") - zCoord;
            }
            else
            {
                centerCoords[2] = 0.5f;
            }
        }

        centerCoordsSize = IvNBTHelper.readDoubleArray("multiBlockSize", par1nbtTagCompound);
        if (centerCoordsSize == null) // Legacy
        {
            centerCoordsSize = new double[3];
            if (par1nbtTagCompound.hasKey("centerCoordsSize[0]"))
            {
                centerCoordsSize[0] = par1nbtTagCompound.getFloat("centerCoordsSize[0]");
            }
            else
            {
                centerCoordsSize[0] = 0.5;
            }

            if (par1nbtTagCompound.hasKey("centerCoordsSize[1]"))
            {
                centerCoordsSize[1] = par1nbtTagCompound.getFloat("centerCoordsSize[1]");
            }
            else
            {
                centerCoordsSize[1] = 0.5;
            }

            if (par1nbtTagCompound.hasKey("centerCoordsSize[2]"))
            {
                centerCoordsSize[2] = par1nbtTagCompound.getFloat("centerCoordsSize[2]");
            }
            else
            {
                centerCoordsSize[2] = 0.5;
            }
        }

        if (par1nbtTagCompound.hasKey("parentCoords"))
        {
            this.parentCoords = par1nbtTagCompound.getIntArray("parentCoords");
        }
        else
        {
            this.parentCoords = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.writeToNBT(par1nbtTagCompound);

        par1nbtTagCompound.setInteger("direction", direction);

        IvNBTHelper.writeDoubleArray("multiBlockCenter", centerCoords, par1nbtTagCompound);
        IvNBTHelper.writeDoubleArray("multiBlockSize", centerCoordsSize, par1nbtTagCompound);

        if (this.parentCoords != null)
        {
            par1nbtTagCompound.setIntArray("parentCoords", parentCoords);
        }
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

    public IvRaytraceableAxisAlignedBox getInterpolatedRotatedBox(Object userInfo, double x, double y, double z, double width, double height, double depth, double x1, double y1, double z1, double width1, double height1, double depth1, float fraction)
    {
        double xI = IvMathHelper.mix(x, x1, fraction);
        double yI = IvMathHelper.mix(y, y1, fraction);
        double zI = IvMathHelper.mix(z, z1, fraction);

        double wI = IvMathHelper.mix(width, width1, fraction);
        double hI = IvMathHelper.mix(height, height1, fraction);
        double dI = IvMathHelper.mix(depth, depth1, fraction);

        return getRotatedBox(userInfo, xI, yI, zI, wI, hI, dI);
    }

    public IvRaytraceableAxisAlignedBox getRotatedBox(Object userInfo, double x, double y, double z, double width, double height, double depth)
    {
        return IvMultiBlockHelper.getRotatedBox(userInfo, x, y, z, width, height, depth, getDirection(), getActiveCenterCoords());
    }

    public AxisAlignedBB getRotatedBB(double x, double y, double z, double width, double height, double depth)
    {
        return IvMultiBlockHelper.getRotatedBB(x, y, z, width, height, depth, getDirection(), getActiveCenterCoords());
    }

    public int getDirection()
    {
        return direction;
    }

    public AxisAlignedBB getBoxAroundCenter(double width, double height, double length)
    {
        double[] center = getActiveCenterCoords();
        return AxisAlignedBB.getAABBPool().getAABB(center[0] - width, center[1] - height, center[2] - length, center[0] + width, center[1] + height, center[2] + length);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (!isParent())
        {
            return getBoxAroundCenter(0.0, 0.0, 0.0);
        }
        else
        {
            return getBoxAroundCenter(centerCoordsSize[0], centerCoordsSize[1], centerCoordsSize[2]);
        }
    }
}
