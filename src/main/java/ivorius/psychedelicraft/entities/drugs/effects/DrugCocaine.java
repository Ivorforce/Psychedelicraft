/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
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
    public void update(EntityLivingBase entity, DrugHelper drugHelper)
    {
        super.update(entity, drugHelper);

        if (getActiveValue() > 0.0)
        {
            Random random = entity.getRNG();
            int ticksExisted = drugHelper.ticksExisted;

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
}
