/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import net.minecraftforge.fluids.FluidStack;

/**
 * A fluid that can explode.
 */
public interface ExplodingFluid
{
    public static final String SUBTYPE = "exploding";

    /**
     * Determines the flame distance of the explosion.
     *
     * @param fluidStack The fluid stack.
     * @return The flame distance in blocks.
     */
    float fireStrength(FluidStack fluidStack);

    /**
     * Determines the explosion size.
     *
     * @param fluidStack The fluid stack.
     * @return The explosion size in blocks.
     */
    float explosionStrength(FluidStack fluidStack);
}
