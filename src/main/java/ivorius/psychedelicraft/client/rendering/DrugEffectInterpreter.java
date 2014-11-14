/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import ivorius.psychedelicraft.entities.drugs.Drug;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.util.MathHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Created by lukas on 25.02.14.
 */
@ParametersAreNonnullByDefault
public class DrugEffectInterpreter
{
    public static float getSmoothVision(DrugHelper drugHelper)
    {
        float smoothVision = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            smoothVision += drug.headMotionInertness();
        return  1.0f / (1.0f + smoothVision);
    }

    public static float getCameraShiftY(DrugHelper drugHelper, float ticks)
    {
        float amplitude = 0.0f;

        for (Drug drug : drugHelper.getAllDrugs())
            amplitude += (1.0f - amplitude) * drug.viewTrembleStrength();

        if (amplitude > 0.0f)
            return MathHelper.sin(ticks / 3.0f) * MathHelper.sin(ticks / 3.0f) * amplitude * 0.1f;

        return 0.0f;
    }

    public static float getCameraShiftX(DrugHelper drugHelper, float ticks)
    {
        float amplitude = 0.0f;

        for (Drug drug : drugHelper.getAllDrugs())
            amplitude += (1.0f - amplitude) * drug.viewTrembleStrength();

        if (amplitude > 0.0f)
            return (new Random((long) (ticks * 1000.0f)).nextFloat() - 0.5f) * 0.05f * amplitude;

        return 0.0f;
    }

    public static float getHandShiftY(DrugHelper drugHelper, float ticks)
    {
        return getCameraShiftY(drugHelper, ticks) * 0.3f;
    }

    public static float getHandShiftX(DrugHelper drugHelper, float ticks)
    {
        float amplitude = 0.0f;

        for (Drug drug : drugHelper.getAllDrugs())
            amplitude += (1.0f - amplitude) * drug.handTrembleStrength();

        if (amplitude > 0.0f)
            return (new Random((long) (ticks * 1000.0f)).nextFloat() - 0.5f) * 0.015f * amplitude;

        return 0.0f;
    }
}
