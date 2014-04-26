/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.client.rendering.effectWrappers;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.client.rendering.DrugEffectInterpreter;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderBloom;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderBlur;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderRadialBlur;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperBloom extends ShaderWrapper<ShaderBloom>
{
    public WrapperBloom(String utils)
    {
        super(new ShaderBloom(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderBloom.frag"), utils);

        System.out.println("shaderInstance = " + shaderInstance);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
        {
            shaderInstance.bloom = drugHelper.getDrugValue("Cocaine") * 2.5f + drugHelper.getDrugValue("Warmth") * 1.0f;
        }
        else
            shaderInstance.bloom = 0.0f;
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
