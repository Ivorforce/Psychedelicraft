/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderColorBloom;
import ivorius.psychedelicraft.entities.drugs.Drug;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import ivorius.psychedelicraft.entities.drugs.effects.DrugHarmonium;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperColorBloom extends ShaderWrapper<ShaderColorBloom>
{
    public WrapperColorBloom(String utils)
    {
        super(new ShaderColorBloom(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderColoredBloom.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        shaderInstance.coloredBloom = new float[4];

        if (drugHelper != null)
        {
            Drug harmonium = drugHelper.getDrug("Harmonium");
            if (harmonium instanceof DrugHarmonium)
            {
                float[] coloredBloom = ((DrugHarmonium) harmonium).getHarmonizedColorAsPrimary(drugHelper);
                coloredBloom[3] = Math.min(coloredBloom[3] * 4.0f, 1.0f);

                shaderInstance.coloredBloom = coloredBloom;
            }
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
