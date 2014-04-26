package net.ivorius.psychedelicraft.entities;

import net.ivorius.psychedelicraft.ivToolkit.IvShaderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class DrugHarmonium extends Drug
{
    public float[] currentColor;

    public DrugHarmonium(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);

        currentColor = new float[]{1.0f, 1.0f, 1.0f};
    }

    public float[] getHarmonizedColorAsPrimary(DrugHelper drugHelper)
    {
        float[] safeColor = new float[4];

        safeColor[0] = currentColor[0];
        safeColor[1] = currentColor[1];
        safeColor[2] = currentColor[2];
        safeColor[3] = (float) getActiveValue();

        return safeColor;
    }

    @Override
    public void applyToShader(IvShaderInstance shaderInstance, String key, Minecraft mc, DrugHelper drugHelper)
    {
        shaderInstance.setUniformFloats("harmoniumColor", this.getHarmonizedColorAsPrimary(drugHelper));
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setFloat("currentColor[0]", currentColor[0]);
        par1NBTTagCompound.setFloat("currentColor[1]", currentColor[1]);
        par1NBTTagCompound.setFloat("currentColor[2]", currentColor[2]);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        currentColor[0] = par1NBTTagCompound.getFloat("currentColor[0]");
        currentColor[1] = par1NBTTagCompound.getFloat("currentColor[1]");
        currentColor[2] = par1NBTTagCompound.getFloat("currentColor[2]");
    }
}
