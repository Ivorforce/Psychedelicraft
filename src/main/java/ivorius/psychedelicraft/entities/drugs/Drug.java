/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs;

import ivorius.ivtoolkit.rendering.IvShaderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface Drug
{
    void update(EntityLivingBase entity, DrugHelper drugHelper);

    void reset(EntityLivingBase entity, DrugHelper drugHelper);

    void writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    double getActiveValue();

    void addToDesiredValue(double effect);

    void setDesiredValue(double effect);

    boolean isVisible();

    void setLocked(boolean drugLocked);

    boolean isLocked();

    float heartbeatVolume();

    float heartbeatSpeed();

    float breathVolume();

    float breathSpeed();

    float randomJumpChance();

    float randomPunchChance();

    float digSpeedModifier();

    float speedModifier();

    float soundVolumeModifier();

    EntityPlayer.EnumStatus getSleepStatus();

    float bigWaveHallucinationStrength();

    float smallWaveHallucinationStrength();

    float redPulsesHallucinationStrength();

    float quickColorRotationHallucinationStrength();

    float surfaceFractalHallucinationStrength();

    float slowColorRotationHallucinationStrength();

    float wiggleWaveHallucinationStrength();

    float distantWorldDeformationHallucinationStrength();

    void applyWorldColorizationHallucinationStrength(float[] rgba);

    float getDesaturationHallucinationStrength();

    float getSuperSaturationHallucinationStrength();
}
