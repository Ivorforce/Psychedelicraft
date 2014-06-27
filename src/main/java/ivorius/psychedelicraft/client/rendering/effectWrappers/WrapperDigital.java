/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.effectWrappers;

import ivorius.psychedelicraft.client.rendering.shaders.DrugShaderHelper;
import ivorius.ivtoolkit.IvOpenGLTexturePingPong;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperDigital implements IEffectWrapper
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
    public void apply(float partialTicks, IvOpenGLTexturePingPong pingPong)
    {
        if (DrugShaderHelper.depthBuffer.isAllocated())
        {
            digitalPD.apply(partialTicks, pingPong);
        }
        else
        {
            digitalMD.apply(partialTicks, pingPong);
        }
    }

    @Override
    public boolean wantsDepthBuffer(float partialTicks)
    {
        return digitalPD.wantsDepthBuffer(partialTicks) || digitalMD.wantsDepthBuffer(partialTicks);
    }
}
