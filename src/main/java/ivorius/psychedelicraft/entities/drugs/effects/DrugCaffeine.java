/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.Psychedelicraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugCaffeine extends DrugSimple
{
    public DrugCaffeine(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public float heartbeatVolume()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue(), 0.6f, 1.0f);
    }

    @Override
    public float heartbeatSpeed()
    {
        return (float) getActiveValue() * 0.2f;
    }

    @Override
    public float breathVolume()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue(), 0.4f, 1.0f) * 0.5f;
    }

    @Override
    public float breathSpeed()
    {
        return (float) getActiveValue() * 0.3f;
    }

    @Override
    public float randomJumpChance()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue(), 0.6f, 1.0f) * 0.07f;
    }

    @Override
    public float randomPunchChance()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue(), 0.3f, 1.0f) * 0.05f;
    }

    @Override
    public float speedModifier()
    {
        return 1.0F + (float) getActiveValue() * 0.2F;
    }

    @Override
    public float digSpeedModifier()
    {
        return 1.0F + (float) getActiveValue() * 0.2F;
    }

    @Override
    public EntityPlayer.EnumStatus getSleepStatus()
    {
        return getActiveValue() > 0.1 ? Psychedelicraft.sleepStatusDrugs : null;
    }

    @Override
    public float superSaturationHallucinationStrength()
    {
        return (float)getActiveValue() * 0.3f;
    }

    @Override
    public float handTrembleStrength()
    {
        return IvMathHelper.zeroToOne((float)getActiveValue(), 0.6f, 1.0f);
    }

    @Override
    public float viewTrembleStrength()
    {
        return IvMathHelper.zeroToOne((float)getActiveValue(), 0.8f, 1.0f);
    }

    @Override
    public float colorHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.7f, 1.0f) * 0.03f;
    }

    @Override
    public float movementHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.7f, 1.0f) * 0.03f;
    }

    @Override
    public float contextualHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.7f, 1.0f) * 0.05f;
    }
}
