/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import net.minecraftforge.fluids.FluidStack;

/**
 * Created by lukas on 22.10.14.
 */
public interface FluidFermentable
{
    void updateFermenting(FluidStack stack);
}
