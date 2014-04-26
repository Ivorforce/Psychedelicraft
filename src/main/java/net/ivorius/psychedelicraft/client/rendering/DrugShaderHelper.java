package net.ivorius.psychedelicraft.client.rendering;

import com.google.common.base.Charsets;
import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.toolkit.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Random;

public class DrugShaderHelper
{
    public static ShaderWorld currentShader;
    public static ArrayList<ShaderWorld> worldShaders = new ArrayList<ShaderWorld>();
    public static ShaderMain shaderInstance;
    public static ShaderMainDepth shaderInstanceDepth;
    public static ShaderShadows shaderInstanceShadows;

    public static ShaderBlur blurShaderInstance;
    public static ShaderRadialBlur radialBlurShaderInstance;
    public static ShaderDoF dofShaderInstance;
    public static ShaderDoubleVision doubleVisionShaderInstance;
    public static ShaderBloom bloomShaderInstance;
    public static ShaderColorBloom colorBloomShaderInstance;
    public static ShaderDigital digitalShaderInstance;
    public static ShaderDigitalDepth digitalDepthShaderInstance;
    public static ShaderBlurNoise blurNoiseShaderInstance;
    public static ShaderHeatDistortions heatDistortionShaderInstance;
    public static ShaderDistortionMap distortionMapShaderInstance;

    public static EffectMotionBlur motionBlurEffect;
    public static EffectLensFlare effectLensFlare;

    public static IvOpenGLTexturePingPong realtimePingPong;

    public static IvDepthBuffer depthBuffer;

    public static boolean shaderEnabled = true;
    public static boolean shader2DEnabled = true;
    public static boolean doShadows = false;
    public static boolean doHeatDistortion = false;
    public static boolean doWaterDistortion = false;
    public static boolean bypassFramebuffers = false;

    public static float sunFlareIntensity;
    public static int shadowPixelsPerChunk = 256;

    public static String currentRenderPass;
    public static float currentRenderPassTicks;

    public static ResourceLocation digitalTextTexture;
    public static ResourceLocation heatDistortionNoiseTexture;
    public static ResourceLocation waterDropletsDistortionTexture;

    public static void preRender(float ticks)
    {
    }

    public static void preRender3D(float ticks)
    {

    }

    public static ArrayList<String> getRenderPasses(float partialTicks, float ticks)
    {
        Minecraft mc = Minecraft.getMinecraft();
        DrugHelper drugHelper = DrugHelper.getDrugHelper(mc.thePlayer);

        ArrayList<String> passes = new ArrayList<String>();

        passes.add("Default");

        if (depthBuffer.isAllocated() && shaderInstanceDepth.getShaderID() > 0 && needsDepthPass(drugHelper, ticks, partialTicks))
        {
            passes.add("Depth");
        }

        if (shaderInstanceShadows.depthBuffer.isAllocated() && shaderInstanceShadows.getShaderID() > 0 && doShadows)
        {
            passes.add("Shadows");
        }

        return passes;
    }

    public static boolean startRenderPass(String pass, float partialTicks, float ticks)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (currentRenderPass != null)
        {
            endRenderPass();
        }

        currentRenderPass = pass;
        currentRenderPassTicks = ticks;

        if ("Default".equals(pass))
        {
//            IvRenderHelper.drawRectFullScreen(mc);
//            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

            shaderInstance.shouldDoShadows = doShadows;
            shaderInstance.shadowDepthTextureIndex = shaderInstanceShadows.depthBuffer.getDepthTextureIndex();

            return useShader(partialTicks, ticks, shaderInstance);
        }
        else if ("Depth".equals(pass))
        {
            depthBuffer.setParentFB(mc.getFramebuffer() != null ? mc.getFramebuffer().framebufferObject : 0);
            depthBuffer.setSize(mc.displayWidth, mc.displayHeight);
            depthBuffer.bindTextureForDestination();
            depthBuffer.bind();

            return useShader(partialTicks, ticks, shaderInstanceDepth);
        }
        else if ("Shadows".equals(pass))
        {
            Minecraft.getMinecraft().renderViewEntity = new EntityFakeSun(mc.renderViewEntity);
            return useShader(partialTicks, ticks, shaderInstanceShadows);
        }

        return true;
    }

    public static void endRenderPass()
    {
        if ("Default".equals(currentRenderPass))
        {

        }
        else if ("Depth".equals(currentRenderPass))
        {
            depthBuffer.unbind();
        }
        else if ("Shadows".equals(currentRenderPass))
        {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.renderViewEntity instanceof EntityFakeSun)
            {
                mc.renderViewEntity = ((EntityFakeSun) mc.renderViewEntity).prevViewEntity;
            }
        }

        if (currentShader != null)
        {
            currentShader.deactivate();
            currentShader = null;
        }

        currentRenderPass = null;
    }

    public static boolean setupCameraTransform()
    {
        if ("Shadows".equals(currentRenderPass)/* || (Minecraft.getMinecraft().ingameGUI.getUpdateCounter() % 100 > 2)*/)
        {
            PsycheShadowHelper.setupSunGLTransform();

            return true;
        }

        return false;
    }

    public static void setShaderEnabled(boolean enabled)
    {
        shaderEnabled = enabled;
    }

    public static void setShader2DEnabled(boolean enabled)
    {
        shader2DEnabled = enabled;
    }

    public static void allocate()
    {
        Minecraft mc = Minecraft.getMinecraft();
        deleteShaders();

        String utils = null;

        try
        {
            IResource utilsResource = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathShaders + "shaderUtils.frag"));
            utils = IOUtils.toString(utilsResource.getInputStream(), Charsets.UTF_8);
        }
        catch (Exception ex)
        {
            Psychedelicraft.logger.error("Could not load shader utils!", utils);
        }

        shaderInstance = new ShaderMain(Psychedelicraft.logger);
        setUpShader(shaderInstance, "shader3D.vert", "shader3D.frag", utils);
        worldShaders.add(shaderInstance);

        shaderInstanceDepth = new ShaderMainDepth(Psychedelicraft.logger);
        setUpShader(shaderInstanceDepth, "shader3D.vert", "shader3DDepth.frag", utils);
        worldShaders.add(shaderInstanceDepth);

        shaderInstanceShadows = new ShaderShadows(Psychedelicraft.logger);
        setUpShader(shaderInstanceShadows, "shader3D.vert", "shader3DDepth.frag", utils);
        worldShaders.add(shaderInstanceShadows);

        blurShaderInstance = new ShaderBlur(Psychedelicraft.logger);
        setUpShader(blurShaderInstance, "shaderBasic.vert", "shaderBlur.frag", utils);

        radialBlurShaderInstance = new ShaderRadialBlur(Psychedelicraft.logger);
        setUpShader(radialBlurShaderInstance, "shaderBasic.vert", "shaderRadialBlur.frag", utils);

        dofShaderInstance = new ShaderDoF(Psychedelicraft.logger);
        setUpShader(dofShaderInstance, "shaderBasic.vert", "shaderDof.frag", utils);

        doubleVisionShaderInstance = new ShaderDoubleVision(Psychedelicraft.logger);
        setUpShader(doubleVisionShaderInstance, "shaderBasic.vert", "shaderDoubleVision.frag", utils);

        bloomShaderInstance = new ShaderBloom(Psychedelicraft.logger);
        setUpShader(bloomShaderInstance, "shaderBasic.vert", "shaderBloom.frag", utils);

        colorBloomShaderInstance = new ShaderColorBloom(Psychedelicraft.logger);
        setUpShader(colorBloomShaderInstance, "shaderBasic.vert", "shaderColoredBloom.frag", utils);

        digitalShaderInstance = new ShaderDigital(Psychedelicraft.logger);
        setUpShader(digitalShaderInstance, "shaderBasic.vert", "shaderDigital.frag", utils);

        digitalDepthShaderInstance = new ShaderDigitalDepth(Psychedelicraft.logger);
        setUpShader(digitalDepthShaderInstance, "shaderBasic.vert", "shaderDigitalDepth.frag", utils);

        blurNoiseShaderInstance = new ShaderBlurNoise(Psychedelicraft.logger);
        setUpShader(blurNoiseShaderInstance, "shaderBasic.vert", "shaderBlurNoise.frag", utils);

        heatDistortionShaderInstance = new ShaderHeatDistortions(Psychedelicraft.logger);
        setUpShader(heatDistortionShaderInstance, "shaderBasic.vert", "shaderHeatDistortion.frag", utils);

        distortionMapShaderInstance = new ShaderDistortionMap(Psychedelicraft.logger);
        setUpShader(distortionMapShaderInstance, "shaderBasic.vert", "shaderDistortionMap.frag", utils);

        motionBlurEffect = new EffectMotionBlur();

        effectLensFlare = new EffectLensFlare();
        effectLensFlare.sunFlareSizes = new float[]{0.15f, 0.24f, 0.12f, 0.036f, 0.06f, 0.048f, 0.006f, 0.012f, 0.5f, 0.09f, 0.036f, 0.09f, 0.06f, 0.05f, 0.6f};
        effectLensFlare.sunFlareInfluences = new float[]{-1.3f, -2.0f, 0.2f, 0.4f, 0.25f, -0.25f, -0.7f, -1.0f, 1.0f, 1.4f, -1.31f, -1.2f, -1.5f, -1.55f, -3.0f};
        effectLensFlare.sunBlindnessTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "sunBlindness.png");
        effectLensFlare.sunFlareTextures = new ResourceLocation[effectLensFlare.sunFlareSizes.length];
        for (int i = 0; i < effectLensFlare.sunFlareTextures.length; i++)
        {
            effectLensFlare.sunFlareTextures[i] = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "flare" + i + ".png");
        }

        setUpRealtimeCacheTexture();
        depthBuffer = new IvDepthBuffer(mc.displayWidth, mc.displayHeight, Psychedelicraft.logger);
        if (!bypassFramebuffers)
            depthBuffer.allocate();

        digitalTextTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "digitalText.png");
        heatDistortionNoiseTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "heatDistortionNoise.png");
        waterDropletsDistortionTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "waterDistortion.png");
    }

    public static void setUpShader(IvShaderInstance shader, String vertexFile, String fragmentFile, String utils)
    {
        IvShaderInstanceMC.trySettingUpShader(shader, new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathShaders + vertexFile), new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathShaders + fragmentFile), utils);
    }

    public static void setUpRealtimeCacheTexture()
    {
        deleteRealtimeCacheTexture();

        realtimePingPong = new IvOpenGLTexturePingPong(Psychedelicraft.logger);
        realtimePingPong.setScreenSize(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        realtimePingPong.initialize(!bypassFramebuffers);
    }

    public static void update()
    {
        effectLensFlare.updateLensFlares();
    }

    public static boolean useShader(float partialTicks, float ticks, ShaderWorld shader)
    {
        currentShader = null;

        if (shader != null && shaderEnabled)
        {
            if (shader.isShaderActive())
            {
                return true;
            }

            if (shader.activate(partialTicks, ticks))
            {
                currentShader = shader;
                return true;
            }
        }

        return false;
    }

    public static void setTexture2DEnabled(boolean enabled)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setTexture2DEnabled(enabled);
        }
    }

    public static void setLightmapEnabled(boolean enabled)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setLightmapEnabled(enabled);
        }
    }

    public static void setBlendFunc(int func)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setBlendFunc(func);
        }
    }

    public static void setOverrideColor(float... color)
    {
        if (color != null && color.length != 4)
        {
            throw new IllegalArgumentException("Color must be a length-4 float array");
        }

        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setOverrideColor(color);
        }
    }

    public static void setGLLightEnabled(boolean enabled)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setGLLightEnabled(enabled);
        }
    }

    public static void setGLLight(int number, float x, float y, float z, float strength, float specular)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setGLLight(number, x, y, z, strength, specular);
        }
    }

    public static void setGLLightAmbient(float strength)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setGLLightAmbient(strength);
        }
    }

    public static void setFogMode(int mode)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setFogMode(mode);
        }
    }

    public static void setFogEnabled(boolean enabled)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setFogEnabled(enabled);
        }
    }

    public static void setDepthMultiplier(float depthMultiplier)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setDepthMultiplier(depthMultiplier);
        }
    }

    public static void setUseScreenTexCoords(boolean enabled)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setUseScreenTexCoords(enabled);
        }
    }

    public static void setScreenSizeDefault()
    {
        Minecraft mc = Minecraft.getMinecraft();
        setScreenSize(mc.displayWidth, mc.displayHeight);
    }

    public static void setScreenSize(float screenWidth, float screenHeight)
    {
        setPixelSize(1.0f / screenWidth, 1.0f / screenHeight);
    }

    public static void setPixelSize(float pixelWidth, float pixelHeight)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setPixelSize(pixelWidth, pixelHeight);
        }
    }

    public static void setProjectShadows(boolean projectShadows)
    {
        for (ShaderWorld shaderWorld : worldShaders)
        {
            shaderWorld.setProjectShadows(projectShadows);
        }
    }

    private static boolean needsDepthPass(DrugHelper drugHelper, float ticks, float partialTicks)
    {
        boolean needsHeatDistortion = doHeatDistortion && drugHelper.drugRenderer.getCurrentHeatDistortion() > 0.0f;
        boolean needsWaterDistortion = doWaterDistortion && drugHelper.drugRenderer.getCurrentWaterDistortion() > 0.0f;

        return DrugEffectInterpreter.getDoF(drugHelper, ticks) > 0.0f || drugHelper.getDrugValue("Zero") > 0.0f || needsHeatDistortion || needsWaterDistortion;
    }

    public static void postRender(float ticks, float partialTicks)
    {
        apply2DShaders(ticks, partialTicks);
    }

    public static void apply2DShaders(float ticks, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();
        DrugHelper drugHelper = DrugHelper.getDrugHelper(mc.thePlayer);

        int screenWidth = mc.displayWidth;
        int screenHeight = mc.displayHeight;

        IvOpenGLHelper.setUpOpenGLStandard2D(screenWidth, screenHeight);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);

        effectLensFlare.sunFlareIntensity = sunFlareIntensity;
        if (effectLensFlare.shouldApply(ticks))
        {
            effectLensFlare.renderLensFlares(screenWidth, screenHeight, partialTicks);
        }
        // Doesn't need to be in the pingpong

        realtimePingPong.setParentFrameBuffer(mc.getFramebuffer() != null ? mc.getFramebuffer().framebufferObject : 0);
        realtimePingPong.preTick(screenWidth, screenHeight);

        // -----------------------------------------------

        motionBlurEffect.motionBlur = DrugEffectInterpreter.getMotionBlur(drugHelper, ticks);
        if (motionBlurEffect.shouldApply(ticks))
        {
            motionBlurEffect.apply(screenWidth, screenHeight, ticks, realtimePingPong);
        }

        // -----------------------------------------------

        if (shader2DEnabled)
        {
            if (depthBuffer.isAllocated())
            {
                float heatDistortion = doHeatDistortion ? drugHelper.drugRenderer.getCurrentHeatDistortion() : 0.0f;
                float waterDistortion = doWaterDistortion ? drugHelper.drugRenderer.getCurrentWaterDistortion() : 0.0f;

                heatDistortionShaderInstance.depthTextureIndex = depthBuffer.getDepthTextureIndex();
                heatDistortionShaderInstance.noiseTextureIndex = getTextureIndex(heatDistortionNoiseTexture);

                heatDistortionShaderInstance.strength = heatDistortion;
                heatDistortionShaderInstance.wobbleSpeed = 0.15f;
                if (heatDistortionShaderInstance.shouldApply(ticks))
                {
                    heatDistortionShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
                }

                heatDistortionShaderInstance.strength = waterDistortion;
                heatDistortionShaderInstance.wobbleSpeed = 0.03f;
                if (heatDistortionShaderInstance.shouldApply(ticks))
                {
                    heatDistortionShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
                }
            }

            if (DrugHelper.waterOverlayEnabled)
            {
                float waterScreenDistortion = drugHelper.drugRenderer.getCurrentWaterScreenDistortion();
                distortionMapShaderInstance.strength = waterScreenDistortion * 0.2f;
                distortionMapShaderInstance.alpha = waterScreenDistortion;
                distortionMapShaderInstance.noiseTextureIndex0 = getTextureIndex(waterDropletsDistortionTexture);
                distortionMapShaderInstance.noiseTextureIndex1 = getTextureIndex(waterDropletsDistortionTexture);
                distortionMapShaderInstance.texTranslation0 = new float[]{0.0f, ticks * 0.005f};
                distortionMapShaderInstance.texTranslation1 = new float[]{0.5f, ticks * 0.007f};
                if (distortionMapShaderInstance.shouldApply(ticks))
                {
                    distortionMapShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
                }
            }

            // -----------------------------------------------

            blurShaderInstance.vBlur = DrugEffectInterpreter.getVerticalBlur(drugHelper, ticks);
            blurShaderInstance.hBlur = DrugEffectInterpreter.getHorizontalBlur(drugHelper, ticks);
            if (blurShaderInstance.shouldApply(ticks))
            {
                blurShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
            }

            // -----------------------------------------------

            radialBlurShaderInstance.radialBlur = DrugEffectInterpreter.getRadialBlur(drugHelper, ticks);
            if (radialBlurShaderInstance.shouldApply(ticks))
            {
                radialBlurShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
            }

            // -----------------------------------------------

            bloomShaderInstance.bloom = DrugEffectInterpreter.getBloom(drugHelper, ticks);
            if (bloomShaderInstance.shouldApply(ticks))
            {
                bloomShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
            }

            // -----------------------------------------------

            colorBloomShaderInstance.coloredBloom = DrugEffectInterpreter.getColoredBloom(drugHelper, ticks);
            if (colorBloomShaderInstance.shouldApply(ticks))
            {
                colorBloomShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
            }

            // -----------------------------------------------

            if (depthBuffer.isAllocated())
            {
                dofShaderInstance.dof = DrugEffectInterpreter.getDoF(drugHelper, ticks);
                dofShaderInstance.depthTextureIndex = depthBuffer.getDepthTextureIndex();

                if (dofShaderInstance.shouldApply(ticks))
                {
                    dofShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
                }
            }

            // -----------------------------------------------

            doubleVisionShaderInstance.doubleVision = DrugEffectInterpreter.getDoubleVision(drugHelper, ticks);
            doubleVisionShaderInstance.doubleVisionDistance = MathHelper.sin(mc.thePlayer.ticksExisted / 20.0f) * 0.05f * doubleVisionShaderInstance.doubleVision;
            if (doubleVisionShaderInstance.shouldApply(ticks))
            {
                doubleVisionShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
            }

            // -----------------------------------------------

            blurNoiseShaderInstance.strength = DrugEffectInterpreter.getBlurNoise(drugHelper, ticks);
            blurNoiseShaderInstance.seed = new Random((long) ((mc.thePlayer.ticksExisted + partialTicks) * 1000.0)).nextFloat() * 9.0f + 1.0f;
            if (blurNoiseShaderInstance.shouldApply(ticks))
            {
                blurNoiseShaderInstance.apply(screenWidth, screenHeight, ticks, realtimePingPong);
            }

            // -----------------------------------------------

            ShaderDigital digitalShader = depthBuffer.isAllocated() ? digitalDepthShaderInstance : digitalShaderInstance;

            digitalShader.digital = DrugHelper.getDrugHelper(mc.thePlayer).getDrugValue("Zero");
            digitalShader.maxDownscale = DrugHelper.getDrugHelper(mc.thePlayer).getDigitalEffectPixelResize();
            digitalShader.digitalTextTexture = getTextureIndex(digitalTextTexture);

            if (depthBuffer.isAllocated())
            {
                ((ShaderDigitalDepth) digitalShader).depthTextureIndex = depthBuffer.getDepthTextureIndex();
            }

            if (digitalShader.shouldApply(ticks))
            {
                digitalShader.apply(screenWidth, screenHeight, ticks, realtimePingPong);
            }
        }

        realtimePingPong.postTick();
    }

    private static int getTextureIndex(ResourceLocation loc)
    {
        TextureManager tm = Minecraft.getMinecraft().renderEngine;
        tm.bindTexture(loc); // Allocate texture. MOJANG!
        ITextureObject texture = tm.getTexture(loc);
        return texture.getGlTextureId();
    }

    public static void deleteShaders()
    {
        if (shaderInstance != null)
        {
            shaderInstance.deleteShader();
        }
        shaderInstance = null;

        if (shaderInstanceDepth != null)
        {
            shaderInstanceDepth.deleteShader();
        }
        shaderInstanceDepth = null;

        if (shaderInstanceShadows != null)
        {
            shaderInstanceShadows.deleteShader();
        }
        shaderInstanceShadows = null;

        deleteEffect(blurShaderInstance);
        blurShaderInstance = null;

        deleteEffect(radialBlurShaderInstance);
        radialBlurShaderInstance = null;

        deleteEffect(dofShaderInstance);
        dofShaderInstance = null;

        deleteEffect(doubleVisionShaderInstance);
        doubleVisionShaderInstance = null;

        deleteEffect(bloomShaderInstance);
        bloomShaderInstance = null;

        deleteEffect(colorBloomShaderInstance);
        colorBloomShaderInstance = null;

        deleteEffect(digitalShaderInstance);
        digitalShaderInstance = null;

        deleteEffect(blurNoiseShaderInstance);
        blurNoiseShaderInstance = null;

        deleteEffect(heatDistortionShaderInstance);
        heatDistortionShaderInstance = null;

        deleteEffect(distortionMapShaderInstance);
        distortionMapShaderInstance = null;
    }

    public static void delete2DEffects()
    {
        deleteEffect(motionBlurEffect);
        motionBlurEffect = null;

        deleteEffect(effectLensFlare);
        effectLensFlare = null;
    }

    private static void deleteEffect(Iv2DScreenEffect instance)
    {
        if (instance != null)
        {
            instance.destruct();
        }
    }

    public static void deleteRealtimeCacheTexture()
    {
        if (realtimePingPong != null)
        {
            realtimePingPong.destroy();
            realtimePingPong = null;
        }
    }

    public static void deallocate()
    {
        deleteShaders();
        delete2DEffects();
        deleteRealtimeCacheTexture();

        if (depthBuffer != null)
        {
            depthBuffer.deallocate();
        }
        depthBuffer = null;

        worldShaders.clear();
    }

    public static void outputShaderInfo()
    {
        Psychedelicraft.logger.info("Graphics card info: ");
        IvShaderInstance.outputShaderInfo(Psychedelicraft.logger);
    }
}
