/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import net.minecraftforge.fluids.FluidStack;

/**
 * A fluid that can distill in the correct container, e.g. distillery.
 */
public interface FluidDistillable
{
    /**
     * Tick value indicating that the fluid is currently not distillable.
     */
    public static final int UNDISTILLABLE = -1;

    /**
     * The creative subtype for FluidWithTypes.
     */
    public static final String SUBTYPE = "distillable";

    /**
     * Returns the ticks needed for the fluid to distill. Return {@link #UNDISTILLABLE} if the fluid is curently not distillable.
     *
     * @param stack The fluid currently being distilled.
     * @return The time it needs to distill, in ticks.
     */
    int distillationTime(FluidStack stack);

    /**
     * Notifies the fluid that the stack has distilled, and is expected to apply this change to the stack.
     *
     * @param stack The fluid currently being distilled.
     * @return The stack left over in the distillery.
     */
    FluidStack distillStep(FluidStack stack);
}
