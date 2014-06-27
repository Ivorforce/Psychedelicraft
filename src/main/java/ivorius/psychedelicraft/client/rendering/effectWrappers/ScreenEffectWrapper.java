/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.ivtoolkit.Iv2DScreenEffect;
import ivorius.psychedelicraft.ivtoolkit.IvOpenGLTexturePingPong;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public abstract class ScreenEffectWrapper<ScreenEffect extends Iv2DScreenEffect> implements IEffectWrapper
{
    public ScreenEffect screenEffect;

    protected ScreenEffectWrapper(ScreenEffect screenEffect)
    {
        this.screenEffect = screenEffect;
    }

    @Override
    public void alloc()
    {

    }

    @Override
    public void dealloc()
    {

    }

    @Override
    public void apply(float partialTicks, IvOpenGLTexturePingPong pingPong)
    {
        Minecraft mc = Minecraft.getMinecraft();
        int ticks = mc.ingameGUI.getUpdateCounter();

        setScreenEffectValues(partialTicks, ticks);

        if (screenEffect.shouldApply(ticks + partialTicks))
        {
            screenEffect.apply(mc.displayWidth, mc.displayHeight, ticks + partialTicks, pingPong);
        }
    }

    public abstract void setScreenEffectValues(float partialTicks, int ticks);

    @Override
    public boolean wantsDepthBuffer(float partialTicks)
    {
        return false;
    }
}
