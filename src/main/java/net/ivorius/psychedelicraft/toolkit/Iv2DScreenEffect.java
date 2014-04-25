/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

/**
 * Created by lukas on 21.02.14.
 */
public interface Iv2DScreenEffect
{
    public abstract boolean shouldApply(float ticks);

    public abstract void apply(int screenWidth, int screenHeight, float ticks, IvOpenGLTexturePingPong pingPong);

    public abstract void destruct();
}
