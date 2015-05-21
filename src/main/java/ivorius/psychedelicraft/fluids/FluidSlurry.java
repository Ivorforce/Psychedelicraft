/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import ivorius.psychedelicraft.config.PSConfig;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Objects;

import static ivorius.psychedelicraft.fluids.FluidHelper.MILLIBUCKETS_PER_LITER;

/**
 * Created by lukas on 27.10.14.
 */
public class FluidSlurry extends FluidSimple implements FluidFermentable, FluidWithTypes
{
    public static final int FLUID_PER_DIRT = MILLIBUCKETS_PER_LITER * 4;

    public FluidSlurry(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public int fermentationTime(FluidStack stack, boolean openContainer)
    {
        return stack.amount >= FLUID_PER_DIRT ? PSConfig.slurryHardeningTime : UNFERMENTABLE;
    }

    @Override
    public ItemStack fermentStep(FluidStack stack, boolean openContainer)
    {
        return new ItemStack(Blocks.dirt, stack.amount / FLUID_PER_DIRT); // Round down
    }

    @Override
    public void addCreativeSubtypes(String listType, List<FluidStack> list)
    {
        if (Objects.equals(listType, SUBTYPE_OPEN))
        {
            list.add(new FluidStack(this, 1));
        }
    }
}
