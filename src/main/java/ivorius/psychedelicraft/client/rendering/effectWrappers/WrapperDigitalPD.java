/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.DrugShaderHelper;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderDigitalDepth;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperDigitalPD extends ShaderWrapper<ShaderDigitalDepth>
{
    public ResourceLocation digitalTextTexture;

    public WrapperDigitalPD(String utils)
    {
        super(new ShaderDigitalDepth(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderDigitalDepth.frag"), utils);

        digitalTextTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "digitalText.png");
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
        {
            shaderInstance.digital = drugHelper.getDrugValue("Zero");
            shaderInstance.maxDownscale = drugHelper.getDigitalEffectPixelResize();
            shaderInstance.digitalTextTexture = DrugShaderHelper.getTextureIndex(digitalTextTexture);
            shaderInstance.depthTextureIndex = DrugShaderHelper.depthBuffer.getDepthTextureIndex();

            shaderInstance.zNear = 0.05f;
            shaderInstance.zFar = (float) (Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16);
        }
        else
        {
            shaderInstance.digital = 0.0f;
        }
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean wantsDepthBuffer(float partialTicks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        return drugHelper != null && drugHelper.getDrugValue("Zero") > 0.0;
    }
}
