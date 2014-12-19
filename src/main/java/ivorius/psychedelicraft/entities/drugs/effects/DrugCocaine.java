/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import java.util.Random;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugCocaine extends DrugSimple
{
    public DrugCocaine(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public void update(EntityLivingBase entity, DrugProperties drugProperties)
    {
        super.update(entity, drugProperties);

        if (getActiveValue() > 0.0)
        {
            Random random = entity.getRNG();
            int ticksExisted = drugProperties.ticksExisted;

            if (!entity.worldObj.isRemote)
            {
                double chance = (getActiveValue() - 0.8f) * 0.1f;

                if (ticksExisted % 20 == 0 && random.nextFloat() < chance)
                {
                    DamageSource damageSource = random.nextFloat() < 0.4f ? Psychedelicraft.stroke
                            : random.nextFloat() < 0.5f ? Psychedelicraft.heartFailure
                            : Psychedelicraft.respiratoryFailure;
                    entity.attackEntityFrom(damageSource, 1000);
                }
            }
        }
    }

    @Override
    public float heartbeatVolume()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue(), 0.4f, 1.0f) * 1.2f;
    }

    @Override
    public float heartbeatSpeed()
    {
        return (float) getActiveValue() * 0.1f;
    }

    @Override
    public float breathVolume()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue(), 0.4f, 1.0f) * 1.5f;
    }

    @Override
    public float breathSpeed()
    {
        return (float) getActiveValue() * 0.8f;
    }

    @Override
    public float randomJumpChance()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue(), 0.6f, 1.0f) * 0.03f;
    }

    @Override
    public float randomPunchChance()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue(), 0.5f, 1.0f) * 0.02f;
    }

    @Override
    public float speedModifier()
    {
        return 1.0F + (float) getActiveValue() * 0.15F;
    }

    @Override
    public float digSpeedModifier()
    {
        return 1.0F + (float) getActiveValue() * 0.15F;
    }

    @Override
    public EntityPlayer.EnumStatus getSleepStatus()
    {
        return getActiveValue() > 0.4 ? Psychedelicraft.sleepStatusDrugs : null;
    }

    @Override
    public float desaturationHallucinationStrength()
    {
        return (float)getActiveValue() * 0.75f;
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
    public float headMotionInertness()
    {
        return (float)getActiveValue() * 10.0f;
    }

    @Override
    public float bloomHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float)getActiveValue(), 0.0f, 0.6f) * 1.5f;
    }

    @Override
    public float colorHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.7f, 1.0f) * 0.05f;
    }

    @Override
    public float movementHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.7f, 1.0f) * 0.05f;
    }

    @Override
    public float contextualHallucinationStrength()
    {
        return IvMathHelper.zeroToOne((float) getActiveValue() * 1.3f, 0.7f, 1.0f) * 0.05f;
    }
}
