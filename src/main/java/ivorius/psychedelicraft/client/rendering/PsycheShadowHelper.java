/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import ivorius.ivtoolkit.math.IvMatrixHelper;
import ivorius.psychedelicraft.client.rendering.shaders.ShaderShadows;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by lukas on 24.03.14.
 */
public class PsycheShadowHelper
{
    public static void setupSunGLTransform()
    {
        Minecraft mc = Minecraft.getMinecraft();
        float farPlaneDistance = (float) (mc.gameSettings.renderDistanceChunks * 16);

        int shadowPixels = ShaderShadows.getShadowPixels();
        GL11.glViewport(0, 0, shadowPixels, shadowPixels);

        GL11.glMatrixMode(GL11.GL_PROJECTION);

        FloatBuffer floatBufferProj = BufferUtils.createFloatBuffer(4 * 4);
        getSunProjectionMatrix().store(floatBufferProj);
        floatBufferProj.position(0);
        GL11.glLoadMatrix(floatBufferProj);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        FloatBuffer floatBufferCam = BufferUtils.createFloatBuffer(4 * 4);
        getSunViewMatrix().store(floatBufferCam);
        floatBufferCam.position(0);
        GL11.glLoadMatrix(floatBufferCam);

//        GL11.glMatrixMode(GL11.GL_PROJECTION);
//        GL11.glLoadIdentity();
//        Project.gluPerspective(90.0f, 1 / 1, 0.05F, farPlaneDistance + 300.0f);

//        GL11.glMatrixMode(GL11.GL_MODELVIEW);
//        GL11.glLoadIdentity();
//        float sunRadians = mc.theWorld.getCelestialAngleRadians(1.0f);
//        GL11.glTranslatef(0.0f, 0.0f, -100.0f);
//        GL11.glRotatef(-sunRadians / 3.1415926f * 180.0f + 90.0f, 1.0f, 0.0f, 0.0f);
//        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
    }

    public static Matrix4f getSunMatrix()
    {
        Matrix4f projMatrix = getSunProjectionMatrix();
        Matrix4f sunMatrix = getSunViewMatrix();

        Matrix4f.mul(projMatrix, sunMatrix, sunMatrix);

        return sunMatrix;
    }

    public static Matrix4f getSunProjectionMatrix()
    {
        // Fix for projection rotation. Y-Axis loses info
        float farPlaneDistance = (float) (Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16);
        return IvMatrixHelper.getOrthographicMatrix(-farPlaneDistance, farPlaneDistance, -farPlaneDistance, farPlaneDistance, getSunZNear(), getSunZFar());
//        return IvMatrixHelper.getProjectionMatrix(0.4f, 1 / 1, getSunZNear(), getSunZFar());
    }

    public static Matrix4f getSunViewMatrix()
    {
        Matrix4f sunMatrix = new Matrix4f();
        Matrix4f.setIdentity(sunMatrix);

        float sunRadians = Minecraft.getMinecraft().theWorld.getCelestialAngleRadians(1.0f);
        Matrix4f.translate(new Vector3f(0.0f, 0.0f, -300.0f), sunMatrix, sunMatrix);
        Matrix4f.rotate(-sunRadians + 3.1415926f * 0.5f, new Vector3f(1.0f, 0.0f, 0.0f), sunMatrix, sunMatrix);
        Matrix4f.rotate(3.1415926f * 0.5f, new Vector3f(0.0f, 1.0f, 0.0f), sunMatrix, sunMatrix);

        return sunMatrix;
    }

    public static Matrix4f getInverseViewMatrix(float partialTicks)
    {
//        return (Matrix4f) getSunMatrix().invert();

        EntityLivingBase renderEntity = Minecraft.getMinecraft().renderViewEntity;
        return (Matrix4f) PsycheMatrixHelper.getLookProjectionMatrix(PsycheMatrixHelper.getCurrentProjectionMatrix(partialTicks), renderEntity).invert();
    }

    public static float getSunZFar()
    {
        float farPlaneDistance = (float) (Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16);
        return farPlaneDistance + 300.0f;
    }

    public static float getSunZNear()
    {
        return 10.0f;
    }

    public static float getShadowBias()
    {
        return 0.001f;
    }
}
