/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.ivToolkit;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class IvOpenGLHelper
{
    public static int genStandardTexture()
    {
        int textureIndex = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureIndex);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        return textureIndex;
    }

    public static void setUpOpenGLStandard2D(int screenWidth, int screenHeight)
    {
        glClear(GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0D, screenWidth, screenHeight, 0.0D, 1000.0D, 3000.0D);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0.0F, 0.0F, -2000.0F);
    }
}
