/***************************************************************************************************
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
 **************************************************************************************************/

package net.ivorius.psychedelicraft.toolkit;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class IvTileEntityMultiBlock extends TileEntity
{
    public int[] parentCoords;
    public int direction;

    public float[] centerCoords;
    public float[] centerCoordsSize;

    public IvTileEntityMultiBlock()
    {

    }

    public void becomeChild(TileEntity parent)
    {
        if (parent != null)
        {
            this.parentCoords = new int[]{parent.xCoord - this.xCoord, parent.yCoord - this.yCoord, parent.zCoord - this.zCoord};
        }
    }

    public boolean isParent()
    {
        return parentCoords == null;
    }

    public TileEntity getParent()
    {
        if (parentCoords != null)
        {
            TileEntity parentTileEntity = getWorldObj().getTileEntity(xCoord + parentCoords[0], yCoord + parentCoords[1], zCoord + parentCoords[2]);

            if (parentTileEntity != null && this.getClass().isAssignableFrom(parentTileEntity.getClass()))
            {
                return parentTileEntity;
            }
        }

        return null;
    }

    public float getDistanceToCenter()
    {
        return (float) IvMathHelper.distance(new double[]{xCoord + 0.5, yCoord + 0.5, zCoord + 0.5}, new double[]{centerCoords[0], centerCoords[1], centerCoords[2]});
    }

    @Override
    public void readFromNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.readFromNBT(par1nbtTagCompound);

        direction = par1nbtTagCompound.getInteger("direction");

        centerCoords = new float[3];
        if (par1nbtTagCompound.hasKey("centerCoords[0]"))
        {
            centerCoords[0] = par1nbtTagCompound.getFloat("centerCoords[0]");
        }
        if (par1nbtTagCompound.hasKey("centerCoords[1]"))
        {
            centerCoords[1] = par1nbtTagCompound.getFloat("centerCoords[1]");
        }
        if (par1nbtTagCompound.hasKey("centerCoords[2]"))
        {
            centerCoords[2] = par1nbtTagCompound.getFloat("centerCoords[2]");
        }

        centerCoordsSize = new float[3];
        if (par1nbtTagCompound.hasKey("centerCoordsSize[0]"))
        {
            centerCoordsSize[0] = par1nbtTagCompound.getFloat("centerCoordsSize[0]");
        }
        if (par1nbtTagCompound.hasKey("centerCoordsSize[1]"))
        {
            centerCoordsSize[1] = par1nbtTagCompound.getFloat("centerCoordsSize[1]");
        }
        if (par1nbtTagCompound.hasKey("centerCoordsSize[2]"))
        {
            centerCoordsSize[2] = par1nbtTagCompound.getFloat("centerCoordsSize[2]");
        }

        if (par1nbtTagCompound.hasKey("parentCoords"))
        {
            this.parentCoords = par1nbtTagCompound.getIntArray("parentCoords");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.writeToNBT(par1nbtTagCompound);

        par1nbtTagCompound.setInteger("direction", direction);

        if (centerCoords != null)
        {
            par1nbtTagCompound.setFloat("centerCoords[0]", centerCoords[0]);
            par1nbtTagCompound.setFloat("centerCoords[1]", centerCoords[1]);
            par1nbtTagCompound.setFloat("centerCoords[2]", centerCoords[2]);
        }
        if (centerCoordsSize != null)
        {
            par1nbtTagCompound.setFloat("centerCoordsSize[0]", centerCoordsSize[0]);
            par1nbtTagCompound.setFloat("centerCoordsSize[1]", centerCoordsSize[1]);
            par1nbtTagCompound.setFloat("centerCoordsSize[2]", centerCoordsSize[2]);
        }

        if (this.parentCoords != null)
        {
            par1nbtTagCompound.setIntArray("parentCoords", parentCoords);
        }
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
        if (centerCoords != null)
        {
            return IvMultiBlockHelper.getRotatedBox(userInfo, x, y, z, width, height, depth, getDirection(), this.centerCoords[0], this.centerCoords[1], this.centerCoords[2]);
        }

        return IvMultiBlockHelper.getRotatedBox(userInfo, x, y, z, width, height, depth, getDirection(), xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f);
    }

    public AxisAlignedBB getRotatedBB(double x, double y, double z, double width, double height, double depth)
    {
        if (centerCoords != null)
        {
            return IvMultiBlockHelper.getRotatedBB(x, y, z, width, height, depth, getDirection(), this.centerCoords[0], this.centerCoords[1], this.centerCoords[2]);
        }

        return IvMultiBlockHelper.getRotatedBB(x, y, z, width, height, depth, getDirection(), xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f);
    }

    public int getDirection()
    {
        return direction;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (centerCoords != null)
        {
            if (!isParent())
            {
                return AxisAlignedBB.getAABBPool().getAABB(centerCoords[0], centerCoords[1], centerCoords[2], centerCoords[0], centerCoords[1], centerCoords[2]);
            }
            else
            {
                return AxisAlignedBB.getAABBPool().getAABB(centerCoords[0] - centerCoordsSize[0], centerCoords[1] - centerCoordsSize[1], centerCoords[2] - centerCoordsSize[2], centerCoords[0] + centerCoordsSize[0], centerCoords[1] + centerCoordsSize[1], centerCoords[2] + centerCoordsSize[2]);
            }
        }
        else
        {
            return super.getRenderBoundingBox();
        }
    }
}
