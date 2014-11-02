/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderBloom;
import ivorius.psychedelicraft.entities.drugs.Drug;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperBloom extends ShaderWrapper<ShaderBloom>
{
    public WrapperBloom(String utils)
    {
        super(new ShaderBloom(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderBloom.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        shaderInstance.bloom = 0.0f;
        if (drugHelper != null)
        {
            for (Drug drug : drugHelper.getAllDrugs())
                shaderInstance.bloom += drug.bloomHallucinationStrength();
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
