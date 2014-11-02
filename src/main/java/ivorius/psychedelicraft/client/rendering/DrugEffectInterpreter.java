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
        return 1.0f / (drugHelper.getDrugValue("Cocaine") * 10.0f + drugHelper.getDrugValue("Cannabis") * 8.0f + 1.0f);
    }

    public static float getCameraShiftY(DrugHelper drugHelper, float ticks)
    {
        float amplitude = 0.0f;

        amplitude += drugHelper.getDrugValue("Cocaine") * 0.10f;
        amplitude += drugHelper.getDrugValue("Caffeine") * 0.18f;

        if (amplitude > 0.0f)
        {
            return MathHelper.sin(ticks / 3.0f) * MathHelper.sin(ticks / 3.0f) * amplitude;
        }

        return 0.0f;
    }

    public static float getCameraShiftX(DrugHelper drugHelper, float ticks)
    {
        float amplitude = 0.0f;

        float coca = (drugHelper.getDrugValue("Cocaine") - 0.8f) * 5.0f;
        if (coca > 0.0f)
        {
            amplitude += coca;
        }
        float caffe = (drugHelper.getDrugValue("Caffeine") - 0.8f) * 5.0f;
        if (caffe > 0.0f)
        {
            amplitude += caffe;
        }

        if (amplitude > 0.0f)
        {
            return (new Random((long) (ticks * 1000.0f)).nextFloat() - 0.5f) * 0.05f * amplitude;
        }

        return 0.0f;
    }

    public static float getHandShiftY(DrugHelper drugHelper, float ticks)
    {
        return getCameraShiftY(drugHelper, ticks) * 0.3f;
    }

    public static float getHandShiftX(DrugHelper drugHelper, float ticks)
    {
        float amplitude = 0.0f;

        float coca = (drugHelper.getDrugValue("Cocaine") - 0.6f) * 2.5f;
        if (coca > 0.0f)
        {
            amplitude += coca;
        }
        float caffe = (drugHelper.getDrugValue("Caffeine") - 0.6f) * 2.5f;
        if (caffe > 0.0f)
        {
            amplitude += caffe;
        }

        if (amplitude > 0.0f)
        {
            return (new Random((long) (ticks * 1000.0f)).nextFloat() - 0.5f) * 0.015f * amplitude;
        }

        return 0.0f;
    }

    public static float getHallucinationStrength(DrugHelper drugHelper, float partialTicks)
    {
        return drugHelper.getDrugValue("BrownShrooms") * 0.2f + drugHelper.getDrugValue("Peyote") * 0.2f;
    }

    public static float getDesaturation(DrugHelper drugHelper, float partialTicks)
    {
        float desaturation = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            desaturation += (1.0f - desaturation) * drug.getDesaturationHallucinationStrength();
        return desaturation;
    }

    public static float getColorIntensification(DrugHelper drugHelper, float partialTicks)
    {
        float intensification = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            intensification += (1.0f - intensification) * drug.getSuperSaturationHallucinationStrength();
        return intensification;
    }

    public static float getSlowColorRotation(DrugHelper drugHelper, float partialTicks)
    {
        float slowColorRotationStrength = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            slowColorRotationStrength += (1.0f - slowColorRotationStrength) * drug.slowColorRotationHallucinationStrength();
        return slowColorRotationStrength;
    }

    public static float getQuickColorRotation(DrugHelper drugHelper, float partialTicks)
    {
        float quickColorRotationStrength = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            quickColorRotationStrength += (1.0f - quickColorRotationStrength) * drug.quickColorRotationHallucinationStrength();
        return quickColorRotationStrength;
    }
}
