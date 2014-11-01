/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * An interface for pre-defining liquids in containers.
 */
public interface FluidWithTypes
{
    /**
     * Adds the desired fluid stacks to the list for the given type.
     *
     * @param listType A string identifier for a type.
     * @param list     The list to add the stacks to.
     */
    void addCreativeSubtypes(String listType, List<FluidStack> list);
}
