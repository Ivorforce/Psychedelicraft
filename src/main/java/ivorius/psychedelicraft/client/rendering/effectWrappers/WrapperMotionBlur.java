/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.client.rendering.EffectMotionBlur;
import ivorius.psychedelicraft.entities.drugs.Drug;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
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

        screenEffect.motionBlur = 0.0f;

        if (drugHelper != null)
        {
            for (Drug drug : drugHelper.getAllDrugs())
                screenEffect.motionBlur += (1.0f - screenEffect.motionBlur) * drug.motionBlur();
        }
    }

    @Override
    public void update()
    {

    }
}
