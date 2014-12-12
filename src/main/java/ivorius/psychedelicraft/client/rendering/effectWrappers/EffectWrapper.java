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
public interface EffectWrapper
{
    public void alloc();

    public void dealloc();

    public void update();

    public void apply(float partialTicks, IvOpenGLTexturePingPong pingPong, IvDepthBuffer depthBuffer);

    public boolean wantsDepthBuffer(float partialTicks);
}
