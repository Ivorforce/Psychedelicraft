/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.client.rendering.effectWrappers;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.client.rendering.DrugEffectInterpreter;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderBlurNoise;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderDoubleVision;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

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
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
        {
            shaderInstance.strength = drugHelper.getDrugValue("Power") * 0.6f;
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
    public boolean wantsDepthBuffer()
    {
        return true;
    }
}
