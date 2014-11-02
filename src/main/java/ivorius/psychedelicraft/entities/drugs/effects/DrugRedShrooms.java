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
public class DrugRedShrooms extends DrugSimple
{
    public DrugRedShrooms(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public float smallWaveHallucinationStrength()
    {
        return (float) getActiveValue();
    }

    @Override
    public float redPulsesHallucinationStrength()
    {
        return (float) getActiveValue();
    }

    @Override
    public float quickColorRotationHallucinationStrength()
    {
        return (float)getActiveValue() * 1.5f;
    }

    @Override
    public float viewWobblyness()
    {
        return (float)getActiveValue() * 0.03f;
    }
}
