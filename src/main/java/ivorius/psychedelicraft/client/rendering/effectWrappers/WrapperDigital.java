/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.ivtoolkit.rendering.IvOpenGLTexturePingPong;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperDigital implements EffectWrapper
{
    public WrapperDigitalMD digitalMD;
    public WrapperDigitalPD digitalPD;

    public WrapperDigital(String utils)
    {
        digitalMD = new WrapperDigitalMD(utils);
        digitalPD = new WrapperDigitalPD(utils);
    }

    @Override
    public void alloc()
    {
        digitalMD.alloc();
        digitalPD.alloc();
    }

    @Override
    public void dealloc()
    {
        digitalMD.dealloc();
        digitalPD.dealloc();
    }

    @Override
    public void update()
    {
        digitalMD.update();
        digitalPD.update();
    }

    @Override
    public void apply(float partialTicks, IvOpenGLTexturePingPong pingPong, IvDepthBuffer depthBuffer)
    {
        if (depthBuffer != null)
            digitalPD.apply(partialTicks, pingPong, depthBuffer);
        else
            digitalMD.apply(partialTicks, pingPong, depthBuffer);
    }

    @Override
    public boolean wantsDepthBuffer(float partialTicks)
    {
        return digitalPD.wantsDepthBuffer(partialTicks) || digitalMD.wantsDepthBuffer(partialTicks);
    }
}
