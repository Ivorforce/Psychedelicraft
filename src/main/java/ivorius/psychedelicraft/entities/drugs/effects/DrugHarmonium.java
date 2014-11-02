/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.ivtoolkit.rendering.IvShaderInstance;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class DrugHarmonium extends DrugSimple
{
    public float[] currentColor;

    public DrugHarmonium(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);

        currentColor = new float[]{1.0f, 1.0f, 1.0f};
    }

    @Override
    public void applyWorldColorizationHallucinationStrength(float[] rgba)
    {
        rgba[0] *= currentColor[0];
        rgba[1] *= currentColor[1];
        rgba[2] *= currentColor[2];
        rgba[3] += (float)getActiveValue();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setFloat("currentColor[0]", currentColor[0]);
        tagCompound.setFloat("currentColor[1]", currentColor[1]);
        tagCompound.setFloat("currentColor[2]", currentColor[2]);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        currentColor[0] = tagCompound.getFloat("currentColor[0]");
        currentColor[1] = tagCompound.getFloat("currentColor[1]");
        currentColor[2] = tagCompound.getFloat("currentColor[2]");
    }
}
