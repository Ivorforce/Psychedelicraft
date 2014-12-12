/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderSimpleEffects;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperSimpleEffects extends ShaderWrapper<ShaderSimpleEffects>
{
    public WrapperSimpleEffects(String utils)
    {
        super(new ShaderSimpleEffects(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderSimpleEffects.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks, IvDepthBuffer depthBuffer)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        if (drugProperties != null)
        {
            shaderInstance.quickColorRotation = drugProperties.hallucinationManager.getQuickColorRotation(drugProperties, partialTicks);
            shaderInstance.slowColorRotation = drugProperties.hallucinationManager.getSlowColorRotation(drugProperties, partialTicks);
            shaderInstance.desaturation = drugProperties.hallucinationManager.getDesaturation(drugProperties, partialTicks);
            shaderInstance.colorIntensification = drugProperties.hallucinationManager.getColorIntensification(drugProperties, partialTicks);
        }
        else
        {
            shaderInstance.slowColorRotation = 0.0f;
            shaderInstance.quickColorRotation = 0.0f;
            shaderInstance.desaturation = 0.0f;
            shaderInstance.colorIntensification = 0.0f;
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
