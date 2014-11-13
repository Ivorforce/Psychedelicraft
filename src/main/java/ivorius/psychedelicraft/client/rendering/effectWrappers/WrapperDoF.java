/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.DrugShaderHelper;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderDoF;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;

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
    public void setShaderValues(float partialTicks, int ticks, IvDepthBuffer depthBuffer)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null && depthBuffer != null)
        {
            shaderInstance.dof = 0.0f;
            shaderInstance.depthTextureIndex = depthBuffer.getDepthTextureIndex();
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
    public boolean wantsDepthBuffer(float partialTicks)
    {
        return false;
    }
}
