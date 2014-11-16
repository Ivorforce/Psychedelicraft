/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore;

import ivorius.pscoreutils.events.*;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;

/**
 * Created by lukas on 21.02.14.
 */
public class PsycheCoreBusClient
{
    public static void preWorldRender(float partialTicks)
    {
        setPlayerAngles(partialTicks); // TODO Fix to allow cancellation later
        PsycheCoreBusCommon.EVENT_BUS.post(new RenderWorldEvent.Pre(partialTicks));
    }

    public static void postWorldRender(float partialTicks)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new RenderWorldEvent.Post(partialTicks));
    }

    public static void psycheGLEnable(int cap)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLSwitchEvent(cap, true));
    }

    public static void psycheGLDisable(int cap)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLSwitchEvent(cap, false));
    }

    public static void psycheGLBlendFunc(int sFactor, int dFactor, int sfactorAlpha, int dfactorAlpha)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLBlendFuncEvent(sFactor, dFactor, sfactorAlpha, dfactorAlpha));
    }

    public static void psycheGLActiveTexture(int texture)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLActiveTextureEvent(texture));
    }

    public static void psycheGLFogi(int pname, int param)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLFogiEvent(pname, param));
    }

    public static void psycheGLTranslatef(float x, float y, float z)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLTranslateEvent(x, y, z));
    }

    public static void psycheGLRotatef(float angle, float x, float y, float z)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLRotateEvent(angle, x, y, z));
    }

    public static void psycheGLScalef(float x, float y, float z)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLScaleEvent(x, y, z));
    }

    public static int psycheGLClear(int mask)
    {
        GLClearEvent event = new GLClearEvent(mask);
        PsycheCoreBusCommon.EVENT_BUS.post(event);
        return event.currentMask;
    }

    public static void enableStandardItemLighting()
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new ItemLightingEvent(true));
    }

    public static void disableStandardItemLighting()
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new ItemLightingEvent(false));
    }

    public static void orientCamera(float partialTicks)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new OrientCameraEvent(partialTicks));
    }

    public static void renderHeldItem(float partialTicks)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new RenderHeldItemEvent(partialTicks));
    }

    public static void renderEntities(float partialTicks)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new RenderEntitiesEvent(partialTicks));
    }

    public static boolean preRenderHand(float partialTicks)
    {
        return PsycheCoreBusCommon.EVENT_BUS.post(new RenderHandEvent.Pre(partialTicks));
    }

    public static void postRenderHand(float partialTicks)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new RenderHandEvent.Pre(partialTicks));
    }

    public static void setPlayerAngles(float partialTicks)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new SetPlayerAnglesEvent(partialTicks));
    }

    public static float getSoundVolume(float volume, ISound sound, SoundPoolEntry entry, SoundCategory category, SoundManager manager)
    {
        GetSoundVolumeEvent event = new GetSoundVolumeEvent(volume, sound, entry, category, manager);
        PsycheCoreBusCommon.EVENT_BUS.post(event);

        return event.volume;
    }

    public static boolean setupCameraTransform(float partialTicks)
    {
        return PsycheCoreBusCommon.EVENT_BUS.post(new SetupCameraTransformEvent(partialTicks));
    }

    public static boolean renderBlockOverlay(float partialTicks)
    {
        return PsycheCoreBusCommon.EVENT_BUS.post(new RenderBlockOverlayEvent(partialTicks));
    }

    public static void fixGLState()
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new GLStateFixEvent());
    }

    public static void enableLightmap()
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new LightmapSwitchEvent(true));
    }

    public static void disableLightmap()
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new LightmapSwitchEvent(false));
    }

    public static void preRenderSky(float partialTicks)
    {
        PsycheCoreBusCommon.EVENT_BUS.post(new RenderSkyEvent.Pre(partialTicks));
    }
}
