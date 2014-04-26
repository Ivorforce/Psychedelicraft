package net.ivorius.psychedelicraftcore;

import net.ivorius.psychedelicraftcore.toolkit.IvClassTransformerManager;
import net.ivorius.psychedelicraftcore.toolkit.IvDevRemapper;
import net.ivorius.psychedelicraftcore.transformers.*;

/**
 * Created by lukas on 21.02.14.
 */
public class PsychedelicraftClassTransformer extends IvClassTransformerManager
{
    public PsychedelicraftClassTransformer()
    {
        PsycheDevRemapper.setUp();

        registerTransformer("net.minecraft.client.renderer.EntityRenderer", new EntityRendererTransformer());
        registerTransformer("net.minecraft.client.renderer.RenderGlobal", new RenderGlobalTransformer());
        registerTransformer("net.minecraft.client.renderer.OpenGlHelper", new OpenGLHelperTransformer());
        registerTransformer("net.minecraft.client.renderer.RenderHelper", new RenderHelperTransformer());
        registerTransformer("net.minecraft.entity.player.EntityPlayer", new EntityPlayerTransformer());
        registerTransformer("net.minecraft.client.audio.SoundManager", new SoundManagerTransformer());

        registerTransformer(new OpenGLTransfomer());
    }
}
