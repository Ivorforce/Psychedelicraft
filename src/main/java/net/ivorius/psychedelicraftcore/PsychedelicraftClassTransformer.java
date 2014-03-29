package net.ivorius.psychedelicraftcore;

import net.ivorius.psychedelicraftcore.transformers.*;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by lukas on 21.02.14.
 */
public class PsychedelicraftClassTransformer implements IClassTransformer
{
    private Hashtable<String, IvClassTransformer> transformers;
    private ArrayList<IvClassTransformer> generalTransformers;

    public PsychedelicraftClassTransformer()
    {
        IvDevRemapper.setUp();
        transformers = new Hashtable<String, IvClassTransformer>();
        generalTransformers = new ArrayList<IvClassTransformer>();

        registerTransformer("net.minecraft.client.renderer.EntityRenderer", new EntityRendererTransformer());
        registerTransformer("net.minecraft.client.renderer.RenderGlobal", new RenderGlobalTransformer());
        registerTransformer("net.minecraft.client.renderer.OpenGlHelper", new OpenGLHelperTransformer());
        registerTransformer("net.minecraft.client.renderer.RenderHelper", new RenderHelperTransformer());
        registerTransformer("net.minecraft.entity.player.EntityPlayer", new EntityPlayerTransformer());
        registerTransformer("net.minecraft.client.audio.SoundManager", new SoundManagerTransformer());

        registerTransformer(new OpenGLTransfomer());
    }

    private void registerTransformer(String clazz, IvClassTransformer transformer)
    {
        transformers.put(clazz, transformer);
    }

    private void registerTransformer(IvClassTransformer transformer)
    {
        generalTransformers.add(transformer);
    }

    @Override
    public byte[] transform(String arg0, String arg1, byte[] arg2)
    {
        if (arg2 != null)
        {
            byte[] result = arg2;

            IvClassTransformer transformer = transformers.get(arg1);
            if (transformer != null)
            {
                byte[] data = transformer.transform(arg0, arg1, result, arg0.equals(arg1));

                if (data != null)
                {
                    result = data;
                }
            }

            for (IvClassTransformer generalTransformer : generalTransformers)
            {
                byte[] data = generalTransformer.transform(arg0, arg1, result, arg0.equals(arg1));

                if (data != null)
                {
                    result = data;
                }
            }

            return result;
        }

        return arg2;
    }
}
