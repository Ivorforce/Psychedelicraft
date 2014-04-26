/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraftcore.toolkit;

import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by lukas on 21.02.14.
 */
public class IvClassTransformerManager implements IClassTransformer
{
    public Hashtable<String, IvClassTransformer> transformers;
    public ArrayList<IvClassTransformer> generalTransformers;

    public IvClassTransformerManager()
    {
        transformers = new Hashtable<String, IvClassTransformer>();
        generalTransformers = new ArrayList<IvClassTransformer>();

        IvDevRemapper.setUp();
    }

    public void registerTransformer(String clazz, IvClassTransformer transformer)
    {
        transformers.put(clazz, transformer);
    }

    public void registerTransformer(IvClassTransformer transformer)
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
