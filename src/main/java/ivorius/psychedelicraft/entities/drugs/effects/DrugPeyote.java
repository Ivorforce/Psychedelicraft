/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.ivtoolkit.rendering.IvShaderInstance;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugPeyote extends DrugSimple
{
    public DrugPeyote(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public void applyToShader(IvShaderInstance shaderInstance, Minecraft mc, DrugHelper drugHelper)
    {
        shaderInstance.setUniformFloats("peyote", (float) getActiveValue());
    }
}
