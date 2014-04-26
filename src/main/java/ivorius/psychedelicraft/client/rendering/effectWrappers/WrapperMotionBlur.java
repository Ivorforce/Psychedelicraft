/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.client.rendering.EffectMotionBlur;
import ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperMotionBlur extends ScreenEffectWrapper<EffectMotionBlur>
{
    public WrapperMotionBlur()
    {
        super(new EffectMotionBlur());
    }

    @Override
    public void setScreenEffectValues(float partialTicks, int ticks)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(Minecraft.getMinecraft().renderViewEntity);

        if (drugHelper != null)
        {
            screenEffect.motionBlur = drugHelper.getDrugClamped("Alcohol", 0.5f, 1.0f) * 0.3f + drugHelper.getDrugValue("Power") * 0.3f;
        }
        else
        {
            screenEffect.motionBlur = 0.0f;
        }
    }

    @Override
    public void update()
    {

    }
}
