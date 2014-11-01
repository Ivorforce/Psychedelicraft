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
import net.minecraft.util.MathHelper;

import java.util.Random;

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
                ((EntityPlayer) entity).addExhaustion(0.03F * (float)getActiveValue());
        }
    }
}
