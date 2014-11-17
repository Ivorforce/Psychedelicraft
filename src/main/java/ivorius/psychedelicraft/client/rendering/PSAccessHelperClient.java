package ivorius.psychedelicraft.client.rendering;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

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
        if (fieldFogColorRed == null)
            fieldFogColorRed = ReflectionHelper.findField(EntityRenderer.class, "fogColorRed", "field_78518_n");
        if (fieldFogColorGreen == null)
            fieldFogColorGreen = ReflectionHelper.findField(EntityRenderer.class, "fogColorGreen", "field_78519_o");
        if (fieldFogColorBlue == null)
            fieldFogColorBlue = ReflectionHelper.findField(EntityRenderer.class, "fogColorBlue", "field_78533_p");

        try
        {
            EntityRenderer entityRenderer = Minecraft.getMinecraft().entityRenderer;

            return new float[]{fieldFogColorRed.getFloat(entityRenderer),
                    fieldFogColorGreen.getFloat(entityRenderer),
                    fieldFogColorBlue.getFloat(entityRenderer)};
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return new float[]{1.0f, 1.0f, 1.0f};
    }
}
