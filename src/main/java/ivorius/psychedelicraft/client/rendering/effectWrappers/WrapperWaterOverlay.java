/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.PSRenderStates;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderDistortionMap;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperWaterOverlay extends ShaderWrapper<ShaderDistortionMap>
{
    public ResourceLocation waterDropletsDistortionTexture;

    public WrapperWaterOverlay(String utils)
    {
        super(new ShaderDistortionMap(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderDistortionMap.frag"), utils);

        waterDropletsDistortionTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "waterDistortion.png");
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks, IvDepthBuffer depthBuffer)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        if (drugProperties != null && DrugProperties.waterOverlayEnabled)
        {
            float waterScreenDistortion = drugProperties.renderer.getCurrentWaterScreenDistortion();
            shaderInstance.strength = waterScreenDistortion * 0.2f;
            shaderInstance.alpha = waterScreenDistortion;
            shaderInstance.noiseTextureIndex0 = PSRenderStates.getTextureIndex(waterDropletsDistortionTexture);
            shaderInstance.noiseTextureIndex1 = PSRenderStates.getTextureIndex(waterDropletsDistortionTexture);
            shaderInstance.texTranslation0 = new float[]{0.0f, ticks * 0.005f};
            shaderInstance.texTranslation1 = new float[]{0.5f, ticks * 0.007f};
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
