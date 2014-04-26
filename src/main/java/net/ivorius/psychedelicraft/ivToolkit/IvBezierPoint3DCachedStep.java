/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.ivToolkit;

public class IvBezierPoint3DCachedStep
{
    private IvBezierPoint3D leftPoint;
    private int leftPointIndex;
    private double leftPointProgress;

    private IvBezierPoint3D rightPoint;
    private int rightPointIndex;
    private double rightPointProgress;

    private double bezierPathProgress;
    private double innerProgress;

    private double[] cachedPosition;

    public IvBezierPoint3DCachedStep(IvBezierPoint3D leftPoint, int leftPointIndex, IvBezierPoint3D rightPoint, int rightPointIndex, double leftPointProgress, double rightPointProgress, double innerProgress)
    {
        this.leftPoint = leftPoint;
        this.leftPointIndex = leftPointIndex;
        this.rightPoint = rightPoint;
        this.rightPointIndex = rightPointIndex;
        this.leftPointProgress = leftPointProgress;
        this.rightPointProgress = rightPointProgress;
        this.innerProgress = innerProgress;

        this.bezierPathProgress = leftPointProgress + (rightPointProgress - leftPointProgress) * innerProgress;
    }

    public IvBezierPoint3D getLeftPoint()
    {
        return leftPoint;
    }

    public int getLeftPointIndex()
    {
        return leftPointIndex;
    }

    public IvBezierPoint3D getRightPoint()
    {
        return rightPoint;
    }

    public int getRightPointIndex()
    {
        return rightPointIndex;
    }

    public double getLeftPointProgress()
    {
        return leftPointProgress;
    }

    public double getRightPointProgress()
    {
        return rightPointProgress;
    }

    public double getBezierPathProgress()
    {
        return bezierPathProgress;
    }

    public double getInnerProgress()
    {
        return innerProgress;
    }

    public double[] getPosition()
    {
        if (cachedPosition == null)
        {
            double[] bezierFrom = leftPoint.getBezierDirectionPointFrom();
            double[] bezierTo = rightPoint.getBezierDirectionPointTo();

            cachedPosition = IvMathHelper.cubicMix(leftPoint.position, bezierFrom, bezierTo, rightPoint.position, getInnerProgress());
        }

        return cachedPosition;
    }
}
