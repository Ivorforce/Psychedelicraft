/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import net.minecraft.util.MathHelper;
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

    public static int mixColors(int left, int right, float progress)
    {
        float alphaL = (float) (left >> 24 & 255) / 255.0F;
        float redL = (float) (left >> 16 & 255) / 255.0F;
        float greenL = (float) (left >> 8 & 255) / 255.0F;
        float blueL = (float) (left & 255) / 255.0F;

        float alphaR = (float) (right >> 24 & 255) / 255.0F;
        float redR = (float) (right >> 16 & 255) / 255.0F;
        float greenR = (float) (right >> 8 & 255) / 255.0F;
        float blueR = (float) (right & 255) / 255.0F;

        float alpha = alphaL * (1.0f - progress) + alphaR * progress;
        float red = redL * (1.0f - progress) + redR * progress;
        float green = greenL * (1.0f - progress) + greenR * progress;
        float blue = blueL * (1.0f - progress) + blueR * progress;

        return (MathHelper.floor_float(alpha * 255.0f + 0.5f) << 24)
                | (MathHelper.floor_float(red * 255.0f + 0.5f) << 16)
                | (MathHelper.floor_float(green * 255.0f + 0.5f) << 8)
                | MathHelper.floor_float(blue * 255.0f + 0.5f);
    }
}
