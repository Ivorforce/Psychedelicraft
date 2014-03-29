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

public class IvBezierPoint3DCachedStep
{
    public final IvBezierPoint3D point1;
    public final int point1Index;

    public final IvBezierPoint3D point2;

    public final double progress;

    private double[] cachedPosition;

    public IvBezierPoint3DCachedStep(IvBezierPoint3D point1, IvBezierPoint3D point2, double progress, int point1Index)
    {
        this.point1 = point1;
        this.point2 = point2;

        this.progress = progress;

        this.point1Index = point1Index;
    }

    public double[] getPosition()
    {
        if (cachedPosition == null)
        {
            double[] bezierFrom = point1.getBezierDirectionPointFrom();
            double[] bezierTo = point2.getBezierDirectionPointTo();

            cachedPosition = IvMathHelper.cubicMix(point1.position, bezierFrom, bezierTo, point2.position, progress);
        }

        return cachedPosition;
    }
}
