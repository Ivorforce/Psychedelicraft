/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ivorius.pscoreutils.events.*;
import ivorius.psychedelicraft.client.rendering.DrugEffectInterpreter;
import ivorius.psychedelicraft.client.rendering.GLStateProxy;
import ivorius.psychedelicraft.client.rendering.SmoothCameraHelper;
import ivorius.psychedelicraft.client.rendering.shaders.DrugShaderHelper;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import ivorius.psychedelicraftcore.PsycheCoreBusCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 21.02.14.
 */
public class PSCoreHandlerClient
{
    // Taken from RenderHelper
    private final Vec3 field_82884_b = Vec3.createVectorHelper(0.20000000298023224D, 1.0D, -0.699999988079071D).normalize();
    private final Vec3 field_82885_c = Vec3.createVectorHelper(-0.20000000298023224D, 1.0D, 0.699999988079071D).normalize();

    public void register()
    {
        PsycheCoreBusCommon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        float partialTicks = event.partialTicks;
        float ticks = partialTicks + mc.ingameGUI.getUpdateCounter();

        if (event instanceof RenderWorldEvent.Pre)
        {
//            setPlayerAngles(partialTicks);

            int rendererUpdateCount = Minecraft.getMinecraft().ingameGUI.getUpdateCounter();
            DrugShaderHelper.preRender(partialTicks + rendererUpdateCount);

            for (String s : DrugShaderHelper.getRenderPasses(partialTicks))
            {
                if (!s.equals("Default"))
                {
                    if (DrugShaderHelper.startRenderPass(s, partialTicks, ticks))
                    {
                        Minecraft.getMinecraft().entityRenderer.renderWorld(partialTicks, 0L);
                        DrugShaderHelper.endRenderPass();
                    }
                }
            }

            DrugShaderHelper.startRenderPass("Default", partialTicks, ticks);
            DrugShaderHelper.preRender3D(ticks);
        }
        else if (event instanceof RenderWorldEvent.Post)
        {
            DrugShaderHelper.endRenderPass();

            DrugHelper drugHelper = DrugHelper.getDrugHelper(mc.renderViewEntity);

            if (drugHelper != null && drugHelper.drugRenderer != null)
            {
                drugHelper.drugRenderer.renderOverlaysBeforeShaders(event.partialTicks, mc.renderViewEntity, mc.ingameGUI.getUpdateCounter(), mc.displayWidth, mc.displayHeight, drugHelper);
            }

            DrugShaderHelper.postRender(ticks, partialTicks);
        }
    }

    @SubscribeEvent
    public void orientCamera(OrientCameraEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase renderEntity = mc.renderViewEntity;
        DrugHelper drugHelper = DrugHelper.getDrugHelper(renderEntity);
        int rendererUpdateCount = renderEntity.ticksExisted;

        if (drugHelper != null && drugHelper.drugRenderer != null)
        {
            drugHelper.drugRenderer.distortScreen(event.partialTicks, renderEntity, rendererUpdateCount, drugHelper);
        }
    }

    @SubscribeEvent
    public void psycheGLEnable(GLSwitchEvent event)
    {
        DrugShaderHelper.setEnabled(event.cap, event.enable);
    }

    @SubscribeEvent
    public void psycheGLBlendFunc(GLBlendFuncEvent event)
    {
        DrugShaderHelper.setBlendFunc(event.sFactor, event.dFactor, event.dfactorAlpha, event.dfactorAlpha);
    }

    @SubscribeEvent
    public void psycheGLActiveTexture(GLActiveTextureEvent event)
    {
        GLStateProxy.setActiveTextureUnit(event.texture);
    }

    @SubscribeEvent
    public void standardItemLighting(ItemLightingEvent event)
    {
        if (event.enable)
        {
            float var0 = 0.4F;
            float var1 = 0.6F;
            float var2 = 0.0F;

            DrugShaderHelper.setGLLightEnabled(true);
            DrugShaderHelper.setGLLight(0, (float) field_82884_b.xCoord, (float) field_82884_b.yCoord, (float) field_82884_b.zCoord, var1, var2); //Psyche
            DrugShaderHelper.setGLLight(1, (float) field_82885_c.xCoord, (float) field_82885_c.yCoord, (float) field_82885_c.zCoord, var1, var2); //Psyche
            DrugShaderHelper.setGLLightAmbient(var0);
        }
        else
        {
            DrugShaderHelper.setGLLightEnabled(false);
        }
    }

    @SubscribeEvent
    public void renderHeldItem(RenderHeldItemEvent event)
    {
        float partialTicks = event.partialTicks;

        Minecraft mc = Minecraft.getMinecraft();
        int rendererUpdateCount = mc.ingameGUI.getUpdateCounter();

        if (mc.renderViewEntity instanceof EntityPlayer)  //Psyche
        {
            EntityPlayer player = (EntityPlayer) mc.renderViewEntity;
            DrugHelper drugHelper = DrugHelper.getDrugHelper(player);

            if (drugHelper != null)
            {
                float shiftX = DrugEffectInterpreter.getHandShiftX(drugHelper, (float) rendererUpdateCount + partialTicks);
                float shiftY = DrugEffectInterpreter.getHandShiftY(drugHelper, (float) rendererUpdateCount + partialTicks);
                GL11.glTranslatef(shiftX, shiftY, 0.0f);
            }
        }
    }

    @SubscribeEvent
    public void renderEntities(RenderEntitiesEvent event)
    {
        int pass = MinecraftForgeClient.getRenderPass();

        if (pass == 1)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityLivingBase renderEntity = mc.renderViewEntity;
            DrugHelper drugHelper = DrugHelper.getDrugHelper(renderEntity);

            if (drugHelper != null && drugHelper.drugRenderer != null)
            {
                drugHelper.drugRenderer.renderAllHallucinations(event.partialTicks, drugHelper);
            }
        }
    }

    @SubscribeEvent
    public void renderHand(RenderHandEvent event)
    {
        if (event instanceof RenderHandEvent.Pre)
        {
            if (!"Default".equals(DrugShaderHelper.currentRenderPass))
            {
                DrugShaderHelper.setDepthMultiplier(0.0f);
                event.setCanceled(true);
            }
        }
        else if (event instanceof RenderHandEvent.Post)
        {
            if (!"Default".equals(DrugShaderHelper.currentRenderPass))
            {
                DrugShaderHelper.setDepthMultiplier(1.0f);
            }
        }
    }

    @SubscribeEvent
    public void setPlayerAngles(SetPlayerAnglesEvent event)
    {
        float partialTicks = event.partialTicks;

        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase renderEntity = mc.renderViewEntity;
        DrugHelper drugHelper = DrugHelper.getDrugHelper(renderEntity);

        if (drugHelper != null)
        {
            float smoothness = DrugEffectInterpreter.getSmoothVision(drugHelper);
            if (smoothness < 1.0f && mc.inGameHasFocus)
            {
                float deltaX = mc.mouseHelper.deltaX;
                float deltaY = mc.mouseHelper.deltaY;

                float[] angles = SmoothCameraHelper.instance.getAngles(mc.gameSettings.mouseSensitivity, partialTicks, deltaX, deltaY, mc.gameSettings.invertMouse);

                if (!mc.gameSettings.smoothCamera)
                {
                    float[] originalAngles = SmoothCameraHelper.instance.getOriginalAngles(mc.gameSettings.mouseSensitivity, partialTicks, deltaX, deltaY, mc.gameSettings.invertMouse);
                    renderEntity.setAngles(angles[0] - originalAngles[0], angles[1] - originalAngles[1]);
                }
                else
                {
                    renderEntity.setAngles(angles[0], angles[1]);
                }
            }
        }
    }

    @SubscribeEvent
    public void psycheGLFogi(GLFogiEvent event)
    {
        if (event.pname == GL11.GL_FOG_MODE)
        {
            DrugShaderHelper.setFogMode(event.param);
        }
    }

    @SubscribeEvent
    public void getSoundVolume(GetSoundVolumeEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        DrugHelper drugHelper = DrugHelper.getDrugHelper(mc.renderViewEntity);

        if (drugHelper != null)
        {
            event.volume = MathHelper.clamp_float(event.volume * drugHelper.getSoundMultiplier(), 0.0f, 1.0f);
        }
    }

    @SubscribeEvent
    public void setupCameraTransform(SetupCameraTransformEvent event)
    {
        if (DrugShaderHelper.setupCameraTransform())
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void renderBlockOverlay(RenderBlockOverlayEvent event)
    {
        if (!"Default".equals(DrugShaderHelper.currentRenderPass))
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void fixGLState(GLStateFixEvent event)
    {
        DrugShaderHelper.setUseScreenTexCoords(false);
        DrugShaderHelper.setTexture2DEnabled(OpenGlHelper.defaultTexUnit, true);
    }

    @SubscribeEvent
    public void glClear(GLClearEvent event)
    {
        event.currentMask = event.currentMask & DrugShaderHelper.getCurrentAllowedGLDataMask();
    }

    @SubscribeEvent
    public void renderSkyPre(RenderSkyEvent.Pre event)
    {
        DrugShaderHelper.preRenderSky(event.partialTicks);
    }
}
