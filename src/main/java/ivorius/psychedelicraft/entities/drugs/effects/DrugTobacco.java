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
public class DrugTobacco extends DrugSimple
{
    public DrugTobacco(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public float getDesaturationHallucinationStrength()
    {
        return (float)getActiveValue() * 0.2f;
    }
}
