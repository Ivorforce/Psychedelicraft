/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.shaders;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.ivtoolkit.rendering.IvOpenGLTexturePingPong;
import ivorius.ivtoolkit.rendering.IvShaderInstance2D;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

/**
 * Created by lukas on 18.02.14.
 */
public class ShaderHeatDistortions extends IvShaderInstance2D
{
    public float strength;
    public float wobbleSpeed;
    public int depthTextureIndex;
    public int noiseTextureIndex;

    public ShaderHeatDistortions(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean shouldApply(float ticks)
    {
        return strength > 0.0f && depthTextureIndex > 0 && noiseTextureIndex > 0 && super.shouldApply(ticks);
    }

    @Override
    public void apply(int screenWidth, int screenHeight, float ticks, IvOpenGLTexturePingPong pingPong)
    {
        useShader();

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit + 2);
        glBindTexture(GL_TEXTURE_2D, noiseTextureIndex);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        IvDepthBuffer.bindTextureForSource(OpenGlHelper.lightmapTexUnit + 1, depthTextureIndex);

        for (int i = 0; i < 3; i++)
        {
            setUniformInts("tex" + i, i);
        }
        setUniformInts("noiseTex", 3);

        setUniformFloats("totalAlpha", 1.0f);
        setUniformFloats("ticks", ticks * wobbleSpeed);
        setUniformFloats("strength", strength);

        drawFullScreen(screenWidth, screenHeight, pingPong);

        stopUsingShader();
    }
}
