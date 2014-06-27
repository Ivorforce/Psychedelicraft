/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.shaders;

import ivorius.ivtoolkit.rendering.IvOpenGLTexturePingPong;
import ivorius.ivtoolkit.rendering.IvShaderInstance2D;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL11.glColor3f;

/**
 * Created by lukas on 18.02.14.
 */
public class ShaderSimpleEffects extends IvShaderInstance2D
{
    public float brownShrooms;
    public float colorIntensification;
    public float desaturation;

    public ShaderSimpleEffects(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean shouldApply(float ticks)
    {
        return (brownShrooms > 0.0f || colorIntensification > 0.0f || desaturation > 0.0f) && super.shouldApply(ticks);
    }

    @Override
    public void apply(int screenWidth, int screenHeight, float ticks, IvOpenGLTexturePingPong pingPong)
    {
        useShader();

        setUniformInts("tex0", 0);

        setUniformFloats("totalAlpha", 1.0f);

        setUniformFloats("brownshrooms", brownShrooms);
        setUniformFloats("colorIntensification", colorIntensification);
        setUniformFloats("desaturation", desaturation);

        glColor3f(1.0f, 1.0f, 1.0f);
        drawFullScreen(screenWidth, screenHeight, pingPong);

        stopUsingShader();
    }
}
