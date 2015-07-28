/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.ClientProxy;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderBlur;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperBlur extends ShaderWrapper<ShaderBlur>
{
    private double guiBackgroundBlur;

    public WrapperBlur(String utils)
    {
        super(new ShaderBlur(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderBlur.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks, IvDepthBuffer depthBuffer)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        if (drugProperties != null)
        {
            shaderInstance.vBlur = drugProperties.getDrugValue("Power");
            shaderInstance.hBlur = 0.0f;
        }
        else
        {
            shaderInstance.vBlur = 0.0f;
            shaderInstance.hBlur = 0.0f;
        }

        shaderInstance.vBlur += ClientProxy.pauseMenuBlur * guiBackgroundBlur * guiBackgroundBlur * guiBackgroundBlur;
        shaderInstance.hBlur += ClientProxy.pauseMenuBlur * guiBackgroundBlur * guiBackgroundBlur * guiBackgroundBlur;
    }

    @Override
    public void update()
    {
        if (Minecraft.getMinecraft().isGamePaused())
            guiBackgroundBlur = Math.min(1, guiBackgroundBlur + 0.25f);
        else
            guiBackgroundBlur = Math.max(0, guiBackgroundBlur - 0.25f);
    }

    @Override
    public boolean wantsDepthBuffer(float partialTicks)
    {
        return false;
    }
}
