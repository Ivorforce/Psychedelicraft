/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 *
 * You are free to:
 *
 * Share — copy and redistribute the material in any medium or format
 * Adapt — remix, transform, and build upon the material
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 *
 * Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * NonCommercial — You may not use the material for commercial purposes, unless you have a permit by the creator.
 * ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.
 */

package ivorius.psychedelicraft.ivToolkit;

import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.*;

/**
 * Created by lukas on 26.02.14.
 */
public class IvDepthBuffer
{
    public Logger logger;

    private boolean setUp;

    private int depthTextureIndex;
    private int depthFB;

    private int parentFB;

    private int textureWidth;
    private int textureHeight;

    public IvDepthBuffer(int width, int height, Logger logger)
    {
        setParentFB(0);
        setSize(width, height);

        this.logger = logger;
    }

    public boolean allocate()
    {
        deallocate();

        if (GLContext.getCapabilities().GL_EXT_framebuffer_object && textureWidth > 0 && textureHeight > 0)
        {
            depthTextureIndex = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, depthTextureIndex);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_DEPTH_TEXTURE_MODE, GL_INTENSITY);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_R_TO_TEXTURE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, textureWidth, textureHeight, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);

            //-------------------------
            depthFB = glGenFramebuffersEXT();
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, depthFB);
            //Attach
            glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, depthTextureIndex, 0);
            glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT24, textureWidth, textureHeight); // get the data space for it
            //-------------------------
            //Does the GPU support current FBO configuration?
            //Before checking the configuration, you should call these 2 according to the spec.
            //At the very least, you need to call glDrawBuffer(GL_NONE)
            glDrawBuffer(GL_NONE);
            glReadBuffer(GL_NONE);

            int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
            if (status != GL_FRAMEBUFFER_COMPLETE_EXT)
            {
                logger.error("Framebuffer object failed setting up! (" + status + ")");
            }
            else
            {
                setUp = true;
            }

            unbind();
            glDrawBuffer(GL_BACK);
            glReadBuffer(GL_BACK);
        }

        return setUp;
    }

    public void deallocate()
    {
        setUp = false;

        if (depthTextureIndex > 0)
        {
            glDeleteTextures(depthTextureIndex);
            depthTextureIndex = 0;
        }
        if (depthFB > 0)
        {
            glDeleteFramebuffersEXT(depthFB);
            depthFB = 0;
        }
    }

    public int getDepthFBObject()
    {
        return isAllocated() ? depthFB : 0;
    }

    public int getDepthTextureIndex()
    {
        return isAllocated() ? depthTextureIndex : 0;
    }

    public void bind()
    {
        if (isAllocated())
        {
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, depthFB);
        }
    }

    public void unbind()
    {
        if (isAllocated())
        {
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, parentFB);
        }
    }

    public boolean isAllocated()
    {
        return setUp;
    }

    public void setSize(int width, int height)
    {
        if (textureWidth != width || textureHeight != height)
        {
            textureWidth = width;
            textureHeight = height;

            if (isAllocated())
            {
                allocate();
            }
        }
    }

    public int getParentFB()
    {
        return parentFB;
    }

    public void setParentFB(int parentFB)
    {
        this.parentFB = parentFB > 0 ? parentFB : 0;
    }

    public static void bindTextureForSource(int glTexture, int textureIndex)
    {
        glBindTexture(GL_TEXTURE_2D, textureIndex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_NONE);
        glTexParameteri(GL_TEXTURE_2D, GL_DEPTH_TEXTURE_MODE, GL_LUMINANCE);

        OpenGlHelper.setActiveTexture(glTexture);
        glBindTexture(GL_TEXTURE_2D, textureIndex);
        OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
    }

    public static void bindTextureForDestination(int textureIndex)
    {
        glBindTexture(GL_TEXTURE_2D, textureIndex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_R_TO_TEXTURE);
        glTexParameteri(GL_TEXTURE_2D, GL_DEPTH_TEXTURE_MODE, GL_INTENSITY);
    }

    public void bindTextureForSource(int glTexture)
    {
        bindTextureForSource(glTexture, getDepthTextureIndex());
    }

    public void bindTextureForDestination()
    {
        bindTextureForDestination(getDepthTextureIndex());
    }

    public int getTextureWidth()
    {
        return textureWidth;
    }

    public int getTextureHeight()
    {
        return textureHeight;
    }
}
