/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.client.rendering.effectWrappers;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.client.rendering.DrugEffectInterpreter;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderBloom;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderColorBloom;
import net.ivorius.psychedelicraft.entities.DrugHarmonium;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperColorBloom extends ShaderWrapper<ShaderColorBloom>
{
    public WrapperColorBloom(String utils)
    {
        super(new ShaderColorBloom(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderColoredBloom.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
        {
            float[] coloredBloom = ((DrugHarmonium) drugHelper.getDrug("Harmonium")).getHarmonizedColorAsPrimary(drugHelper);
            coloredBloom[3] = Math.min(coloredBloom[3] * 4.0f, 1.0f);

            shaderInstance.coloredBloom = coloredBloom;
        }
        else
        {
            shaderInstance.coloredBloom = new float[4];
        }
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean wantsDepthBuffer()
    {
        return false;
    }
}
