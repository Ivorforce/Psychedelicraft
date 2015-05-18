/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Created by lukas on 22.10.14.
 */
public class PSFluids
{
    public static FluidAlcohol alcWheatHop;
    public static FluidAlcohol alcWheat;
    public static FluidAlcohol alcPotato;
    public static FluidAlcohol alcRedGrapes;
    public static FluidAlcohol alcRice;
    public static FluidAlcohol alcJuniper;
    public static FluidAlcohol alcHoney;
    public static FluidAlcohol alcSugarCane;
    public static FluidAlcohol alcCorn;
    public static FluidAlcohol alcApple;
    public static FluidAlcohol alcPineapple;
    public static FluidAlcohol alcBanana;
    public static FluidAlcohol alcMilk;

    public static FluidDrug coffee;
    public static FluidDrug cocaTea;
    public static FluidDrug cannabisTea;
    public static FluidDrug peyoteJuice;

    public static FluidDrug cocaineFluid;
    public static FluidDrug caffeineFluid;

    public static FluidSimple slurry;

    public static ItemStack filledStack(IFluidContainerItem container, FluidStack fluidStack)
    {
        ItemStack stack = new ItemStack((Item) container);
        container.fill(stack, fluidStack, true);
        return stack;
    }

    public static boolean containsFluid(ItemStack stack, Fluid fluid)
    {
        FluidStack contained = getFluid(stack);
        return contained != null && contained.getFluid() == fluid;
    }

    public static FluidStack getFluid(ItemStack stack)
    {
        return stack.getItem() instanceof IFluidContainerItem
                ? ((IFluidContainerItem) stack.getItem()).drain(stack, ((IFluidContainerItem) stack.getItem()).getCapacity(stack), false)
                : null;
    }
}
