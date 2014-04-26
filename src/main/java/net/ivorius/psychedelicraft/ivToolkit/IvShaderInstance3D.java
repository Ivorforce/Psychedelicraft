/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.ivToolkit;

import org.apache.logging.log4j.Logger;

/**
 * Created by lukas on 18.02.14.
 */
public abstract class IvShaderInstance3D extends IvShaderInstance
{
    public IvShaderInstance3D(Logger logger)
    {
        super(logger);
    }

    public abstract boolean activate(float partialTicks, float ticks);

    public abstract void deactivate();
}
