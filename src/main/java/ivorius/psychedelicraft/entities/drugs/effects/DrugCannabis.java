/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugCannabis extends DrugSimple
{
    public DrugCannabis(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public void update(EntityLivingBase entity, DrugProperties drugProperties)
    {
        super.update(entity, drugProperties);

        if (getActiveValue() > 0.0)
        {
            if (entity instanceof EntityPlayer)
                ((EntityPlayer) entity).addExhaustion(0.03F * (float) getActiveValue());
        }
    }

    @Override
    public float speedModifier()
    {
        return (1.0F - (float) getActiveValue()) * 0.5F + 0.5F;
    }

    @Override
    public float digSpeedModifier()
    {
        return (1.0F - (float) getActiveValue()) * 0.5F + 0.5F;
    }

    @Override
    public float superSaturationHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float)getActiveValue(), 0.0f, 0.5f) * 0.3f;
    }

    @Override
    public float colorHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.5f, 1.0f) * 0.1f;
    }

    @Override
    public float movementHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.5f, 1.0f) * 0.1f;
    }

    @Override
    public float contextualHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.5f, 1.0f) * 0.1f;
    }

    @Override
    public float headMotionInertness()
    {
        return (float)getActiveValue() * 8.0f;
    }

    @Override
    public float viewWobblyness()
    {
        return (float)getActiveValue() * 0.02f;
    }
}
