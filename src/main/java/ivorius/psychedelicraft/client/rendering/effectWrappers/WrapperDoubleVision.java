/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderDoubleVision;
import ivorius.psychedelicraft.entities.drugs.Drug;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperDoubleVision extends ShaderWrapper<ShaderDoubleVision>
{
    public WrapperDoubleVision(String utils)
    {
        super(new ShaderDoubleVision(Psychedelicraft.logger), getRL("shaderBasic.vert"), getRL("shaderDoubleVision.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        shaderInstance.doubleVision = 0.0f;

        if (drugHelper != null)
        {
            for (Drug drug : drugHelper.getAllDrugs())
                shaderInstance.doubleVision += (1.0f - shaderInstance.doubleVision) * drug.doubleVision();

            shaderInstance.doubleVisionDistance = MathHelper.sin((ticks + partialTicks) / 20.0f) * 0.05f * shaderInstance.doubleVision;
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
