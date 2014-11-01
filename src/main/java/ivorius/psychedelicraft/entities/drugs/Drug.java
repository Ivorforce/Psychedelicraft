/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs;

import ivorius.ivtoolkit.rendering.IvShaderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
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

    void applyToShader(IvShaderInstance shaderWorld, String drugID, Minecraft mc, DrugHelper drugHelper);
}
