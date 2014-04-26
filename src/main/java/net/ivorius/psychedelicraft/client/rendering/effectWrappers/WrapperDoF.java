/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.client.rendering.effectWrappers;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.client.rendering.DrugEffectInterpreter;
import net.ivorius.psychedelicraft.client.rendering.shaders.DrugShaderHelper;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderColorBloom;
import net.ivorius.psychedelicraft.client.rendering.shaders.ShaderDoF;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperDoF extends ShaderWrapper<ShaderDoF>
{
    public WrapperDoF(String utils)
    {
        super(new ShaderDoF(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderDof.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
        {
            shaderInstance.dof = 0.0f;
            shaderInstance.depthTextureIndex = DrugShaderHelper.depthBuffer.getDepthTextureIndex();
        }
        else
        {
            shaderInstance.dof = 0.0f;
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
