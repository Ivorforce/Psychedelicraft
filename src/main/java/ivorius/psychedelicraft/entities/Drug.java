/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import ivorius.psychedelicraft.ivtoolkit.IvMathHelper;
import ivorius.psychedelicraft.ivtoolkit.IvShaderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class Drug
{
    private double effect;
    private double effectActive;
    private boolean locked = false;

    private final double decreaseSpeed;
    private final double decreaseSpeedPlus;
    private final boolean invisible;

    private boolean shouldApplyToShader;

    public Drug(double decSpeed, double decSpeedPlus)
    {
        this(decSpeed, decSpeedPlus, false);
    }

    public Drug(double decSpeed, double decSpeedPlus, boolean invisible)
    {
        decreaseSpeed = decSpeed;
        decreaseSpeedPlus = decSpeedPlus;

        this.invisible = invisible;
    }

    public void updateValues()
    {
        if (!locked)
        {
            effect *= decreaseSpeed;
            effect -= decreaseSpeedPlus;
        }

        effect = IvMathHelper.clamp(0.0, effect, 1.0);

        effectActive = IvMathHelper.nearValue(effectActive, effect, 0.05, 0.005);
    }

    public void setActiveValue(double value)
    {
        effectActive = value;
    }

    public double getActiveValue()
    {
        return effectActive;
    }

    public double getDesiredValue()
    {
        return effect;
    }

    public void setDesiredValue(double value)
    {
        effect = value;
    }

    public void addToDesiredValue(double value)
    {
        if (!locked)
        {
            effect += value;
        }
    }

    public void resetDrugValue()
    {
        if (!locked)
        {
            effect = 0.0;
        }
    }

    public void applyToShader(IvShaderInstance shaderInstance, String key, Minecraft mc, DrugHelper drugHelper)
    {
        if (shouldApplyToShader())
        {
            shaderInstance.setUniformFloats(key.toLowerCase(), (float) getActiveValue());
        }
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public boolean isVisible()
    {
        return !invisible;
    }

    public boolean shouldApplyToShader()
    {
        return shouldApplyToShader;
    }

    public Drug setShouldApplyToShader(boolean apply)
    {
        shouldApplyToShader = apply;
        return this;
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setDouble("effect", getDesiredValue());
        par1NBTTagCompound.setDouble("effectActive", getActiveValue());
        par1NBTTagCompound.setBoolean("locked", isLocked());
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        setDesiredValue(par1NBTTagCompound.getDouble("effect"));
        setActiveValue(par1NBTTagCompound.getDouble("effectActive"));
        setLocked(par1NBTTagCompound.getBoolean("locked"));
    }
}
