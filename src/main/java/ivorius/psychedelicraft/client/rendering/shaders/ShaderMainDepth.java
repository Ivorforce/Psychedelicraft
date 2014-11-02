/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.shaders;

import ivorius.ivtoolkit.rendering.IvShaderInstance3D;
import ivorius.psychedelicraft.entities.drugs.Drug;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import org.apache.logging.log4j.Logger;

/**
 * Created by lukas on 26.02.14.
 */
public class ShaderMainDepth extends IvShaderInstance3D implements ShaderWorld
{
    public ShaderMainDepth(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean activate(float partialTicks, float ticks)
    {
        if (useShader())
        {
            Minecraft mc = Minecraft.getMinecraft();

            EntityLivingBase renderEntity = mc.renderViewEntity;
            DrugHelper drugHelper = DrugHelper.getDrugHelper(renderEntity);

            setUniformFloats("ticks", ticks);
            setUniformInts("worldTime", (int) mc.theWorld.getWorldTime());

            setUniformFloats("playerPos", (float) renderEntity.posX, (float) renderEntity.posY, (float) renderEntity.posZ);
            setDepthMultiplier(1.0f);
            setTexture2DEnabled(true);
            setOverrideColor(null);
            setUseScreenTexCoords(false);
            setPixelSize(1.0f / mc.displayWidth, 1.0f / mc.displayHeight);

            if (drugHelper != null)
            {
                for (Drug drug : drugHelper.getAllDrugs())
                    drug.applyToShader(this, mc, drugHelper);
            }

            return true;
        }

        return false;
    }

    @Override
    public void deactivate()
    {
        stopUsingShader();
    }

    @Override
    public void setTexture2DEnabled(boolean enabled)
    {
        setUniformInts("texture2DEnabled", enabled ? 1 : 0);
    }

    @Override
    public void setLightmapEnabled(boolean enabled)
    {

    }

    @Override
    public void setBlendFunc(int func)
    {

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

    }

    @Override

    public void setGLLight(int number, float x, float y, float z, float strength, float specular)
    {

    }

    @Override
    public void setGLLightAmbient(float strength)
    {

    }

    @Override
    public void setFogMode(int mode)
    {

    }

    @Override
    public void setFogEnabled(boolean enabled)
    {

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

    }
}
