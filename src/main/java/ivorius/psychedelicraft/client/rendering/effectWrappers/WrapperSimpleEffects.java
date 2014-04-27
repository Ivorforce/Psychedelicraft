/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.DrugEffectInterpreter;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderBlur;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderSimpleEffects;
import ivorius.psychedelicraft.entities.DrugHelper;
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
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
        {
            shaderInstance.brownShrooms = drugHelper.getDrugValue("BrownShrooms");
            shaderInstance.desaturation = DrugEffectInterpreter.getDesaturation(drugHelper, partialTicks);
            shaderInstance.colorIntensification = DrugEffectInterpreter.getColorIntensification(drugHelper, partialTicks);
        }
        else
        {
            shaderInstance.brownShrooms = 0.0f;
            shaderInstance.desaturation = 0.0f;
            shaderInstance.colorIntensification = 0.0f;
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
