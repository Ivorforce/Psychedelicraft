/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.ivtoolkit.rendering.IvShaderInstance;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugBrownShrooms extends DrugSimple
{
    public DrugBrownShrooms(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public float surfaceFractalHallucinationStrength()
    {
        return (float)getActiveValue();
    }

    @Override
    public float slowColorRotationHallucinationStrength()
    {
        return 0.5f * (float)getActiveValue();
    }

    @Override
    public float wiggleWaveHallucinationStrength()
    {
        return (float)getActiveValue() * 1.5f;
    }

    @Override
    public float distantWorldDeformationHallucinationStrength()
    {
        return (float)getActiveValue();
    }

    @Override
    public float getSuperSaturationHallucinationStrength()
    {
        return (float)getActiveValue();
    }
}
