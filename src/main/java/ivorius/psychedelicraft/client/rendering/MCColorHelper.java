/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 25.10.14.
 */
public class MCColorHelper
{
    public static void setColor(int color, boolean hasAlpha)
    {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        if (hasAlpha)
        {
            float alpha = (float) (color >> 24 & 255) / 255.0F;
            GL11.glColor4f(1.0F * red, 1.0F * green, 1.0F * blue, alpha);
        }
        else
            GL11.glColor3f(1.0F * red, 1.0F * green, 1.0F * blue);
    }
}
