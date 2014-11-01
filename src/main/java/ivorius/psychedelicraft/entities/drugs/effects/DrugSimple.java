/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.ivtoolkit.rendering.IvShaderInstance;
import ivorius.psychedelicraft.entities.drugs.Drug;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class DrugSimple implements Drug
{
    protected double effect;
    protected double effectActive;
    protected boolean locked = false;

    private final double decreaseSpeed;
    private final double decreaseSpeedPlus;
    private final boolean invisible;

    public DrugSimple(double decSpeed, double decSpeedPlus)
    {
        this(decSpeed, decSpeedPlus, false);
    }

    public DrugSimple(double decSpeed, double decSpeedPlus, boolean invisible)
    {
        decreaseSpeed = decSpeed;
        decreaseSpeedPlus = decSpeedPlus;

        this.invisible = invisible;
    }

    public void setActiveValue(double value)
    {
        effectActive = value;
    }

    @Override
    public double getActiveValue()
    {
        return effectActive;
    }

    public double getDesiredValue()
    {
        return effect;
    }

    @Override
    public void setDesiredValue(double value)
    {
        effect = value;
    }

    @Override
    public void addToDesiredValue(double value)
    {
        if (!locked)
        {
            effect += value;
        }
    }

    @Override
    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    @Override
    public boolean isLocked()
    {
        return locked;
    }

    @Override
    public boolean isVisible()
    {
        return !invisible;
    }

    @Override
    public void update(EntityLivingBase entity, DrugHelper drugHelper)
    {
        if (!locked)
        {
            effect *= decreaseSpeed;
            effect -= decreaseSpeedPlus;
        }

        effect = IvMathHelper.clamp(0.0, effect, 1.0);

        effectActive = IvMathHelper.nearValue(effectActive, effect, 0.05, 0.005);
    }

    @Override
    public void reset(EntityLivingBase entity, DrugHelper drugHelper)
    {
        if (!locked)
            effect = 0.0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        setDesiredValue(par1NBTTagCompound.getDouble("effect"));
        setActiveValue(par1NBTTagCompound.getDouble("effectActive"));
        setLocked(par1NBTTagCompound.getBoolean("locked"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setDouble("effect", getDesiredValue());
        compound.setDouble("effectActive", getActiveValue());
        compound.setBoolean("locked", isLocked());
    }

    @Override
    public void applyToShader(IvShaderInstance shaderInstance, String key, Minecraft mc, DrugHelper drugHelper)
    {

    }

    @Override
    public float heartbeatVolume()
    {
        return 0;
    }

    @Override
    public float heartbeatSpeed()
    {
        return 0;
    }

    @Override
    public float breathVolume()
    {
        return 0;
    }

    @Override
    public float breathSpeed()
    {
        return 0;
    }

    @Override
    public float randomJumpChance()
    {
        return 0;
    }

    @Override
    public float randomPunchChance()
    {
        return 0;
    }

    @Override
    public float digSpeedModifier()
    {
        return 1;
    }

    @Override
    public float speedModifier()
    {
        return 1;
    }

    @Override
    public float soundVolumeModifier()
    {
        return 1;
    }

    @Override
    public EntityPlayer.EnumStatus getSleepStatus()
    {
        return null;
    }
}
