/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

public class IvBezierPoint3D
{
    public double[] position;
    public double[] bezierDirection;
    public double rotation;

    public int color;

    public double fontSize;

    public IvBezierPoint3D(double[] position, double[] bezierDirection, int color, double rotation)
    {
        this.position = position;

        this.bezierDirection = bezierDirection;

        this.color = color;

        this.rotation = rotation;

        this.fontSize = 1.0;
    }

    public IvBezierPoint3D(double[] position, double[] bezierDirection, int color, double rotation, double fontSize)
    {
        this(position, bezierDirection, color, rotation);

        this.fontSize = fontSize;
    }

    public double getAlpha()
    {
        return ((color >> 24) & 255) / 255.0;
    }

    public double getRed()
    {
        return ((color >> 16) & 255) / 255.0;
    }

    public double getGreen()
    {
        return ((color >> 8) & 255) / 255.0;
    }

    public double getBlue()
    {
        return (color & 255) / 255.0;
    }

    public double[] getBezierDirectionPointTo()
    {
        double[] result = new double[bezierDirection.length];

        for (int i = 0; i < result.length; i++)
        {
            result[i] = position[i] - bezierDirection[i];
        }

        return result;
    }

    public double[] getBezierDirectionPointFrom()
    {
        double[] result = new double[bezierDirection.length];

        for (int i = 0; i < result.length; i++)
        {
            result[i] = position[i] + bezierDirection[i];
        }

        return result;
    }

    public double getFontSize()
    {
        return fontSize;
    }
}
