package net.ivorius.psychedelicraftcore;

import java.util.Hashtable;

/**
 * Created by lukas on 25.02.14.
 */
public class IvDevRemapper
{
    private static Hashtable<String, String> fakeMappings = new Hashtable<String, String>();

    public static void setUp()
    {
        fakeMappings.put("updateCameraAndRender", "func_78480_b");
        fakeMappings.put("orientCamera", "func_78467_g");
        fakeMappings.put("renderHand", "func_78476_b");
        fakeMappings.put("enableLightmap", "func_78463_b");
        fakeMappings.put("disableLightmap", "func_78483_a");
        fakeMappings.put("renderWorld", "func_78471_a");
        fakeMappings.put("glBlendFunc", "func_148821_a");
        fakeMappings.put("renderSky", "func_72714_a");
        fakeMappings.put("renderEntities", "func_147589_a");
        fakeMappings.put("enableStandardItemLighting", "func_74519_b");
        fakeMappings.put("disableStandardItemLighting", "func_74518_a");
        fakeMappings.put("setupFog", "func_78468_a");
        fakeMappings.put("getFOVModifier", "func_78481_a");
        fakeMappings.put("wakeUpPlayer", "func_70999_a");
        fakeMappings.put("getNormalizedVolume", "func_148594_a");
        fakeMappings.put("drawSelectionBox", "func_72731_b");
        fakeMappings.put("setActiveTexture", "func_77473_a");
        fakeMappings.put("setupCameraTransform", "func_78479_a");
        fakeMappings.put("renderOverlays", "func_78447_b");

        fakeMappings.put("shadersSupported", "field_148824_g");
    }

    // Hurr fake and gay
    public static String getSRGName(String name)
    {
        String mapping = fakeMappings.get(name);

        return mapping != null ? mapping : name;
    }
}
