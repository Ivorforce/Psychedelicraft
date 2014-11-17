/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore;

import ivorius.ivtoolkit.asm.IvDevRemapper;

/**
 * Created by lukas on 25.02.14.
 */
public class PsycheDevRemapper
{
    public static void setUp()
    {
        IvDevRemapper.fakeMappings.put("updateCameraAndRender", "func_78480_b");
        IvDevRemapper.fakeMappings.put("orientCamera", "func_78467_g");
        IvDevRemapper.fakeMappings.put("renderHand", "func_78476_b");
        IvDevRemapper.fakeMappings.put("enableLightmap", "func_78463_b");
        IvDevRemapper.fakeMappings.put("disableLightmap", "func_78483_a");
        IvDevRemapper.fakeMappings.put("renderWorld", "func_78471_a");
        IvDevRemapper.fakeMappings.put("glBlendFunc", "func_148821_a");
        IvDevRemapper.fakeMappings.put("renderSky", "func_72714_a");
        IvDevRemapper.fakeMappings.put("renderEntities", "func_147589_a");
        IvDevRemapper.fakeMappings.put("enableStandardItemLighting", "func_74519_b");
        IvDevRemapper.fakeMappings.put("disableStandardItemLighting", "func_74518_a");
        IvDevRemapper.fakeMappings.put("setupFog", "func_78468_a");
        IvDevRemapper.fakeMappings.put("getFOVModifier", "func_78481_a");
        IvDevRemapper.fakeMappings.put("wakeUpPlayer", "func_70999_a");
        IvDevRemapper.fakeMappings.put("getNormalizedVolume", "func_148594_a");
        IvDevRemapper.fakeMappings.put("drawSelectionBox", "func_72731_b");
        IvDevRemapper.fakeMappings.put("setActiveTexture", "func_77473_a");
        IvDevRemapper.fakeMappings.put("setupCameraTransform", "func_78479_a");
        IvDevRemapper.fakeMappings.put("renderOverlays", "func_78447_b");
        IvDevRemapper.fakeMappings.put("getInstance", "func_78558_a");

        IvDevRemapper.fakeMappings.put("shadersSupported", "field_148824_g");
    }
}
