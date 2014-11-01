/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fluids.FluidStack;

/**
 * A fluid that is possible to be consumed.
 */
public interface InjectableFluid
{
    public static final String SUBTYPE = "injectable";

    /**
     * Indicates if the entity can inject this fluid.
     *
     * @param fluidStack The fluid stack.
     * @param entity     The entity about to inject the fluid.
     * @return True if the entity can inject this fluid, at this point in time.
     */
    boolean canInject(FluidStack fluidStack, EntityLivingBase entity);

    /**
     * Called when the entity has injected the fluid.
     *
     * @param fluidStack The fluid stack.
     * @param entity     The entity injecting the fluid.
     */
    void inject(FluidStack fluidStack, EntityLivingBase entity);
}
