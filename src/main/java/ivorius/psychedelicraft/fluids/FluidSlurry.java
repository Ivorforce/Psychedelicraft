/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import net.minecraftforge.fluids.Fluid;

/**
 * Created by lukas on 27.10.14.
 */
public class FluidSlurry extends Fluid implements TranslucentFluid
{
    public FluidSlurry(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public int getColor()
    {
        return 0xcc704E21;
    }
}
