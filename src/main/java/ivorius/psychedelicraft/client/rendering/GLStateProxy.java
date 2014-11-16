package ivorius.psychedelicraft.client.rendering;

import net.minecraft.client.renderer.OpenGlHelper;

import java.util.Arrays;

/**
 * Created by lukas on 16.11.14.
 */
public class GLStateProxy
{
    private static final boolean[] enabledTextures = new boolean[40];
    private static boolean fogEnabled = false;
    private static int activeTextureUnit = OpenGlHelper.defaultTexUnit;

    static
    {
        Arrays.fill(enabledTextures, true);
    }


    public static boolean isTextureEnabled(int textureUnit)
    {
        textureUnit -= OpenGlHelper.defaultTexUnit;

        return textureUnit >= 0 && textureUnit < enabledTextures.length && enabledTextures[textureUnit];
    }

    public static void setTextureEnabled(int textureUnit, boolean enabled)
    {
        textureUnit -= OpenGlHelper.defaultTexUnit;

        if (textureUnit >= 0 && textureUnit < enabledTextures.length)
            enabledTextures[textureUnit] = enabled;
    }

    public static boolean isFogEnabled()
    {
        return fogEnabled;
    }

    public static void setFogEnabled(boolean fogEnabled)
    {
        GLStateProxy.fogEnabled = fogEnabled;
    }

    public static void setActiveTextureUnit(int textureUnit)
    {
        activeTextureUnit = textureUnit;
    }

    public static int getActiveTextureUnit()
    {
        return activeTextureUnit;
    }
}
