/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.shaders;

/**
 * Created by lukas on 03.03.14.
 */
public interface ShaderWorld
{
    public boolean isShaderActive();

    public boolean activate(float partialTicks, float ticks);

    public void deactivate();

    public void setTexture2DEnabled(boolean enabled);

    public void setLightmapEnabled(boolean enabled);

    public void setOverrideColor(float[] color);

    public void setGLLightEnabled(boolean enabled);

    public void setGLLight(int number, float x, float y, float z, float strength, float specular);

    public void setGLLightAmbient(float strength);

    public void setFogMode(int mode);

    public void setFogEnabled(boolean enabled);

    public void setDepthMultiplier(float depthMultiplier);

    public void setUseScreenTexCoords(boolean enabled);

    public void setPixelSize(float pixelWidth, float pixelHeight);

    public void setBlendModeEnabled(boolean enabled);

    public void setBlendFunc(int sFactor, int dFactor, int sFactorA, int dFactorA);

    public void setProjectShadows(boolean projectShadows);

    void setForceColorSafeMode(boolean enable);
}
