/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.ivtoolkit.rendering.IvShaderInstance;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugBrownShrooms extends DrugSimple
{
    public DrugBrownShrooms(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public void applyToShader(IvShaderInstance shaderInstance, String key, Minecraft mc, DrugHelper drugHelper)
    {
        shaderInstance.setUniformFloats("brownshrooms", (float) getActiveValue());
    }
}
