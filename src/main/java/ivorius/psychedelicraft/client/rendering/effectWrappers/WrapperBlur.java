/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderBlur;
import ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperBlur extends ShaderWrapper<ShaderBlur>
{
    public WrapperBlur(String utils)
    {
        super(new ShaderBlur(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderBlur.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
        {
            shaderInstance.vBlur = drugHelper.getDrugValue("Power");
            shaderInstance.hBlur = 0.0f;
        }
        else
        {
            shaderInstance.vBlur = 0.0f;
            shaderInstance.hBlur = 0.0f;
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
