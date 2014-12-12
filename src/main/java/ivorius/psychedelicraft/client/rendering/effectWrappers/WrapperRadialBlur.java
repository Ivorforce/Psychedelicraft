/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderRadialBlur;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperRadialBlur extends ShaderWrapper<ShaderRadialBlur>
{
    public WrapperRadialBlur(String utils)
    {
        super(new ShaderRadialBlur(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderRadialBlur.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks, IvDepthBuffer depthBuffer)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        if (drugProperties != null)
        {
            shaderInstance.radialBlur = 0.0f;
        }
        else
        {
            shaderInstance.radialBlur = 0.0f;
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
