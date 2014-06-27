/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.shaders;

import ivorius.ivtoolkit.rendering.IvOpenGLTexturePingPong;
import ivorius.ivtoolkit.rendering.IvShaderInstance2D;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

/**
 * Created by lukas on 18.02.14.
 */
public class ShaderDistortionMap extends IvShaderInstance2D
{
    public float strength;
    public float alpha = 1.0f;

    public int noiseTextureIndex0;
    public int noiseTextureIndex1;

    public float[] texTranslation0;
    public float[] texTranslation1;

    public ShaderDistortionMap(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean shouldApply(float ticks)
    {
        return strength > 0.0f && alpha > 0.0f && noiseTextureIndex0 > 0 && noiseTextureIndex1 > 0 && super.shouldApply(ticks);
    }

    @Override
    public void apply(int screenWidth, int screenHeight, float ticks, IvOpenGLTexturePingPong pingPong)
    {
        useShader();

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit + 1);
        glBindTexture(GL_TEXTURE_2D, noiseTextureIndex0);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit + 2);
        glBindTexture(GL_TEXTURE_2D, noiseTextureIndex1);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

        for (int i = 0; i < 2; i++)
        {
            setUniformInts("tex" + i, i);
        }
        setUniformInts("noiseTex0", 2);
        setUniformInts("noiseTex1", 3);

        setUniformFloats("totalAlpha", alpha);
        setUniformFloats("strength", strength);
        setUniformFloats("texTranslation0", texTranslation0);
        setUniformFloats("texTranslation1", texTranslation1);

        drawFullScreen(screenWidth, screenHeight, pingPong);

        stopUsingShader();
    }
}
