/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore;

import ivorius.ivtoolkit.asm.IvClassTransformerManager;
import ivorius.psychedelicraftcore.transformers.*;
import org.apache.logging.log4j.Logger;

/**
 * Created by lukas on 21.02.14.
 */
public class PsychedelicraftClassTransformer extends IvClassTransformerManager
{
    public PsychedelicraftClassTransformer()
    {
        PsycheDevRemapper.setUp();
        Logger logger = PsychedelicraftCoreContainer.logger;

        registerTransformer("net.minecraft.client.renderer.EntityRenderer", new EntityRendererTransformer(logger));
        registerTransformer("net.minecraft.client.renderer.RenderGlobal", new RenderGlobalTransformer(logger));
        registerTransformer("net.minecraft.client.renderer.OpenGlHelper", new OpenGLHelperTransformer(logger));
        registerTransformer("net.minecraft.client.renderer.RenderHelper", new RenderHelperTransformer(logger));
        registerTransformer("net.minecraft.client.audio.SoundManager", new SoundManagerTransformer(logger));

        registerTransformer(new OpenGLTransfomer(logger));
    }
}
