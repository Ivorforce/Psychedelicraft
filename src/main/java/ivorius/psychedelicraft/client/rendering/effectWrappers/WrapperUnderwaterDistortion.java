/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.PSRenderStates;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderHeatDistortions;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperUnderwaterDistortion extends ShaderWrapper<ShaderHeatDistortions>
{
    public ResourceLocation heatDistortionNoiseTexture;

    public WrapperUnderwaterDistortion(String utils)
    {
        super(new ShaderHeatDistortions(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderHeatDistortion.frag"), utils);

        heatDistortionNoiseTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "heatDistortionNoise.png");
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks, IvDepthBuffer depthBuffer)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        if (PSRenderStates.doWaterDistortion && drugProperties != null && depthBuffer != null)
        {
            float waterDistortion = drugProperties.renderer.getCurrentWaterDistortion();

            shaderInstance.depthTextureIndex = depthBuffer.getDepthTextureIndex();
            shaderInstance.noiseTextureIndex = PSRenderStates.getTextureIndex(heatDistortionNoiseTexture);

            shaderInstance.strength = waterDistortion;
            shaderInstance.wobbleSpeed = 0.03f;
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
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        if (drugProperties != null)
        {
            float waterDistortion = PSRenderStates.doWaterDistortion ? drugProperties.renderer.getCurrentWaterDistortion() : 0.0f;

            return waterDistortion > 0.0f;
        }

        return false;
    }
}
