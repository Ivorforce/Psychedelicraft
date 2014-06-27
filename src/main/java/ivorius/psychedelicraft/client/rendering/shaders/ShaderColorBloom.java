/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.shaders;

import ivorius.ivtoolkit.rendering.IvOpenGLTexturePingPong;
import ivorius.ivtoolkit.rendering.IvShaderInstance2D;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.Logger;

/**
 * Created by lukas on 18.02.14.
 */
public class ShaderColorBloom extends IvShaderInstance2D
{
    public float[] coloredBloom;

    public ShaderColorBloom(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean shouldApply(float ticks)
    {
        return coloredBloom[3] > 0.0f && super.shouldApply(ticks);
    }

    @Override
    public void apply(int screenWidth, int screenHeight, float ticks, IvOpenGLTexturePingPong pingPong)
    {
        useShader();

        for (int i = 0; i < 1; i++)
        {
            setUniformInts("tex" + i, i);
        }

        setUniformFloats("pixelSize", 1.0f / screenWidth, 1.0f / screenHeight);
        setUniformFloats("bloomColor", coloredBloom[0], coloredBloom[1], coloredBloom[2]);

        for (int n = 0; n < MathHelper.floor_double(coloredBloom[3]) + 1; n++)
        {
            for (int i = 0; i < 2; i++)
            {
                float activeBloom = coloredBloom[3] - n;
                if (activeBloom > 1.0f)
                {
                    activeBloom = 1.0f;
                }

                setUniformInts("vertical", i);
                setUniformFloats("totalAlpha", activeBloom);

                drawFullScreen(screenWidth, screenHeight, pingPong);
            }
        }

        stopUsingShader();
    }
}
