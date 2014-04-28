/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderRadialBlur;
import ivorius.psychedelicraft.entities.DrugHelper;
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
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
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
