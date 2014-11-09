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
public interface DrinkableFluid
{
    /**
     * The creative subtype for FluidWithTypes.
     */
    public static final String SUBTYPE = "drinkable";

    /**
     * Indicates if the player can drink this fluid.
     *
     * @param fluidStack The fluid stack.
     * @param entity     The entity about to drink the fluid.
     * @return True if the entity can drink this fluid, at this point in time.
     */
    boolean canDrink(FluidStack fluidStack, EntityLivingBase entity);

    /**
     * Called when the entity has drunk one unit of the fluid.
     *
     * @param fluidStack The fluid stack.
     * @param entity     The entity drinking the fluid.
     */
    void drink(FluidStack fluidStack, EntityLivingBase entity);
}
