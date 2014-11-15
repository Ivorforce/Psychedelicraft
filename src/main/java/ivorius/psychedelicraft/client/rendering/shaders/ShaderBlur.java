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
public class ShaderBlur extends IvShaderInstance2D
{
    public float vBlur;
    public float hBlur;

    public ShaderBlur(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean shouldApply(float ticks)
    {
        return (vBlur > 0.0f || hBlur > 0.0f) && super.shouldApply(ticks);
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

        for (int n = 0; n < MathHelper.ceiling_double_int(Math.max(hBlur, vBlur)); n++)
        {
            for (int i = 0; i < 2; i++)
            {
                float activeBlur = (i == 0 ? hBlur : vBlur) - n;
                if (activeBlur > 1.0f)
                    activeBlur = 1.0f;

                if (activeBlur > 0.0f)
                {
                    setUniformInts("vertical", i);
                    setUniformFloats("totalAlpha", activeBlur);

                    drawFullScreen(screenWidth, screenHeight, pingPong);
                }
            }
        }

        stopUsingShader();
    }
}
