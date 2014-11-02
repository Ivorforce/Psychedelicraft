/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugPower extends DrugSimple
{
    public DrugPower(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus, true);
    }

    @Override
    public float soundVolumeModifier()
    {
        return 1.0f - (float) getActiveValue();
    }

    @Override
    public float getDesaturationHallucinationStrength()
    {
        return (float)getActiveValue() * 0.75f;
    }
}
