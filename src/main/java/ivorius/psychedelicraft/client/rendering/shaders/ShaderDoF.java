/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.shaders;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.ivtoolkit.rendering.IvOpenGLTexturePingPong;
import ivorius.ivtoolkit.rendering.IvShaderInstance2D;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.Logger;

/**
 * Created by lukas on 18.02.14.
 */
public class ShaderDoF extends IvShaderInstance2D
{
    public float dof;

    public int depthTextureIndex;

    public ShaderDoF(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean shouldApply(float ticks)
    {
        return depthTextureIndex > 0 && dof > 0.0f && super.shouldApply(ticks);
    }

    @Override
    public void apply(int screenWidth, int screenHeight, float ticks, IvOpenGLTexturePingPong pingPong)
    {
        IvDepthBuffer.bindTextureForSource(OpenGlHelper.lightmapTexUnit + 2, depthTextureIndex);
        pingPong.bindCurrentTexture();

        float dofHBlur = dof;
        float dofVBlur = dof;

        useShader();

        for (int i = 0; i < 3; i++)
        {
            setUniformInts("tex" + i, i);
        }

        setUniformFloats("pixelSize", 1.0f / screenWidth, 1.0f / screenHeight);

        for (int n = 0; n < MathHelper.floor_double(dof) + 1; n++)
        {
            for (int i = 0; i < 2; i++)
            {
                float activeBlur = (i == 0 ? dofHBlur : dofVBlur) - n;
                if (activeBlur > 1.0f)
                {
                    activeBlur = 1.0f;
                }

                if (activeBlur > 0.0f)
                {
                    setUniformInts("vertical", i);
                    setUniformFloats("totalAlpha", activeBlur);

                    drawFullScreen(screenWidth, screenHeight, pingPong);
                }
            }
        }

        pingPong.bindCurrentTexture();

        stopUsingShader();
    }
}
