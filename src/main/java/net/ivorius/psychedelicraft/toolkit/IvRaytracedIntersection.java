/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

public class IvRaytracedIntersection
{
    public IvRaytraceableObject info;
    public double[] point;

    public IvRaytracedIntersection(IvRaytraceableObject info, double[] point)
    {
        this.info = info;
        this.point = point;
    }

    public Object getUserInfo()
    {
        return this.info.userInfo;
    }

    public double getX()
    {
        return point[0];
    }

    public double getY()
    {
        return point[1];
    }

    public double getZ()
    {
        return point[2];
    }

    @Override
    public String toString()
    {
        return String.format("%s: [%.3f, %.3f, %.3f]", this.getUserInfo().toString(), this.getX(), this.getY(), this.getZ());
    }
}
