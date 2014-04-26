package net.ivorius.psychedelicraft.client.rendering;

import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.ivToolkit.IvDepthBuffer;
import net.ivorius.psychedelicraft.ivToolkit.IvShaderInstance3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

/**
 * Created by lukas on 26.02.14.
 */
public class ShaderMain extends IvShaderInstance3D implements ShaderWorld
{
    public boolean shouldDoShadows;
    public int shadowDepthTextureIndex;

    public ShaderMain(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean activate(float partialTicks, float ticks)
    {
        if (useShader())
        {
            Minecraft mc = Minecraft.getMinecraft();
            DrugHelper drugHelper = DrugHelper.getDrugHelper(mc.thePlayer);

            if (DrugEffectInterpreter.shouldRegisterFractalTextures(drugHelper))
            {
                registerFractals();
            }

            for (int i = 0; i < 2; i++)
            {
                setUniformInts("tex" + i, i);
            }

            setUniformFloats("ticks", ticks);
            setUniformInts("worldTime", (int) mc.theWorld.getWorldTime());

            setUniformFloats("playerPos", (float) mc.thePlayer.posX, (float) mc.thePlayer.posY, (float) mc.thePlayer.posZ);

            setTexture2DEnabled(true);
            setLightmapEnabled(true);
            setBlendFunc(GL_ONE_MINUS_SRC_ALPHA);
            setDepthMultiplier(1.0f);
            setUseScreenTexCoords(false);
            setPixelSize(1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
            setFogMode(GL11.GL_LINEAR);
            setOverrideColor(null);

            setUniformFloats("desaturation", DrugEffectInterpreter.getDesaturation(drugHelper, partialTicks));
            setUniformFloats("colorIntensification", DrugEffectInterpreter.getColorIntensification(drugHelper, partialTicks));

            for (String key : drugHelper.getAllVisibleDrugNames())
            {
                drugHelper.getDrug(key).applyToShader(this, key, mc, drugHelper);
            }

            if (shouldDoShadows)
            {
                setUniformMatrix("inverseViewMatrix", PsycheShadowHelper.getInverseViewMatrix());
                setUniformMatrix("sunMatrix", PsycheShadowHelper.getSunMatrix());

                setUniformInts("texShadow", 3);
                setUniformFloats("sunDepthRange", PsycheShadowHelper.getSunZNear(), PsycheShadowHelper.getSunZFar());
                setUniformFloats("shadowBias", PsycheShadowHelper.getShadowBias());
                IvDepthBuffer.bindTextureForSource(OpenGlHelper.lightmapTexUnit + 2, shadowDepthTextureIndex);
            }
            setUniformInts("doShadows", shouldDoShadows ? 1 : 0);

            return true;
        }

        return false;
    }

    @Override
    public void deactivate()
    {
        stopUsingShader();
    }

    public void registerFractals()
    {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit + 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

        IIcon icon = Blocks.portal.getIcon(0, 0);
        float var4 = icon.getMinU();
        float var5 = icon.getMinV();
        float var6 = icon.getMaxU();
        float var7 = icon.getMaxV();

        setUniformFloats("fractal0TexCoords", var4, var5, var6, var7);
    }

    @Override
    public void setTexture2DEnabled(boolean enabled)
    {
        setUniformInts("texture2DEnabled", enabled ? 1 : 0);
    }

    @Override
    public void setLightmapEnabled(boolean enabled)
    {
        setUniformInts("lightmapEnabled", enabled ? 1 : 0);
    }

    @Override
    public void setBlendFunc(int func)
    {
//        setUniformInts("blendFunc", func);
    }

    @Override
    public void setOverrideColor(float[] color)
    {
        if (color != null)
        {
            setUniformFloats("overrideColor", color);
        }
        else
        {
            setUniformFloats("overrideColor", 1F, 1F, 1F, 1F);
        }
    }

    @Override
    public void setGLLightEnabled(boolean enabled)
    {
        setUniformInts("glLightEnabled", enabled ? 1 : 0);
    }

    @Override
    public void setGLLight(int number, float x, float y, float z, float strength, float specular)
    {
        setUniformFloats("glLightPos" + number, x, y, z);
        setUniformFloats("glLightStrength" + number, strength, specular);
    }

    @Override
    public void setGLLightAmbient(float strength)
    {
        setUniformFloats("glLightAmbient", strength);
    }

    @Override
    public void setFogMode(int mode)
    {
        setUniformInts("fogMode", mode);
    }

    @Override
    public void setFogEnabled(boolean enabled)
    {
        setUniformInts("fogEnabled", enabled ? 1 : 0);
    }

    @Override
    public void setDepthMultiplier(float depthMultiplier)
    {
        setUniformFloats("depthMultiplier", depthMultiplier);
    }

    @Override
    public void setUseScreenTexCoords(boolean enabled)
    {
        setUniformInts("useScreenTexCoords", enabled ? 1 : 0);
    }

    @Override
    public void setPixelSize(float pixelWidth, float pixelHeight)
    {
        setUniformFloats("pixelSize", pixelWidth, pixelHeight);
    }

    @Override
    public void setProjectShadows(boolean projectShadows)
    {
        if (shouldDoShadows)
        {
            setUniformInts("doShadows", projectShadows ? 1 : 0);
        }
    }
}
