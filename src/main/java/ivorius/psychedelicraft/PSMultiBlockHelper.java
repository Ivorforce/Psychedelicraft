/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import net.minecraft.util.AxisAlignedBB;

/**
 * Created by lukas on 31.10.14.
 */
public class PSMultiBlockHelper
{
    public static AxisAlignedBB intersection(AxisAlignedBB bb, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox(Math.max(bb.minX, x), Math.max(bb.minY, y), Math.max(bb.minZ, z),
                Math.min(bb.maxX, x + 1), Math.min(bb.maxY, y + 1), Math.min(bb.maxZ, z + 1));
    }

    public static AxisAlignedBB boundsIntersection(AxisAlignedBB bb, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox(Math.max(bb.minX - x, 0), Math.max(bb.minY - y, 0), Math.max(bb.minZ - z, 0),
                Math.min(bb.maxX - x, 1), Math.min(bb.maxY - y, 1), Math.min(bb.maxZ - z, 1));
    }
}
