/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderBlurNoise;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.client.Minecraft;

import java.util.Random;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperBlurNoise extends ShaderWrapper<ShaderBlurNoise>
{
    public WrapperBlurNoise(String utils)
    {
        super(new ShaderBlurNoise(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderBlurNoise.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks, IvDepthBuffer depthBuffer)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        if (drugProperties != null)
        {
            shaderInstance.strength = drugProperties.getDrugValue("Power") * 0.6f;
            shaderInstance.seed = new Random((long) ((ticks + partialTicks) * 1000.0)).nextFloat() * 9.0f + 1.0f;
        }
        else
        {
            shaderInstance.strength = 0.0f;
        }
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean wantsDepthBuffer(float partialTicks)
    {
        return false;
    }
}
