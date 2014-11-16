package ivorius.psychedelicraft.client.rendering;

import cpw.mods.fml.relauncher.ReflectionHelper;
import ivorius.ivtoolkit.rendering.IvRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

/**
 * Created by lukas on 17.11.14.
 */
public class PSAccessHelperClient
{
    private static Field fieldFogColorRed;
    private static Field fieldFogColorGreen;
    private static Field fieldFogColorBlue;

    public static float[] getFogColor()
    {
        if (PSAccessHelperClient.fieldFogColorRed == null)
        {
            PSAccessHelperClient.fieldFogColorRed = ReflectionHelper.findField(EntityRenderer.class, "fogColorRed", "field_175080_Q");
            PSAccessHelperClient.fieldFogColorRed.setAccessible(true);
        }
        if (PSAccessHelperClient.fieldFogColorGreen == null)
        {
            PSAccessHelperClient.fieldFogColorGreen = ReflectionHelper.findField(EntityRenderer.class, "fogColorGreen", "field_175082_R");
            PSAccessHelperClient.fieldFogColorGreen.setAccessible(true);
        }
        if (PSAccessHelperClient.fieldFogColorBlue == null)
        {
            PSAccessHelperClient.fieldFogColorBlue = ReflectionHelper.findField(EntityRenderer.class, "fogColorBlue", "field_175081_S");
            PSAccessHelperClient.fieldFogColorBlue.setAccessible(true);
        }

        try
        {
            float fogColorRed = PSAccessHelperClient.fieldFogColorRed.getFloat(Minecraft.getMinecraft().entityRenderer);
            float fogColorGreen = PSAccessHelperClient.fieldFogColorGreen.getFloat(Minecraft.getMinecraft().entityRenderer);
            float fogColorBlue = PSAccessHelperClient.fieldFogColorBlue.getFloat(Minecraft.getMinecraft().entityRenderer);

            return new float[]{fogColorRed, fogColorGreen, fogColorBlue};
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return new float[]{1.0f, 1.0f, 1.0f};
    }
}
