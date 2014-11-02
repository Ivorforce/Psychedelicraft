/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.psychedelicraft.entities.drugs.DrugHelper;
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
    public void update(EntityLivingBase entity, DrugHelper drugHelper)
    {
        super.update(entity, drugHelper);

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
        return (float)getActiveValue() * 0.5f;
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
