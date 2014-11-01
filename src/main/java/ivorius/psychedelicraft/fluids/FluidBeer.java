/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by lukas on 22.10.14.
 */
public class FluidBeer extends FluidDrug implements FluidFermentable
{
    public static final int FERMENTATION_STEPS = 2;
    public static final int MATURATION_STEPS = 2;

    public FluidBeer(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);

        int fermentation = getFermentation(fluidStack);
        int maturation = getMaturation(fluidStack);

        double alcohol = (double) fermentation / (double) FERMENTATION_STEPS * 0.15
                + (double) maturation / (double) MATURATION_STEPS * 0.05;

        list.add(new DrugInfluence("Alcohol", 20, 0.002, 0.001, alcohol));
    }

    @Override
    public void addCreativeSubtypes(List<FluidStack> list)
    {
        super.addCreativeSubtypes(list);

        for (int fermentation = 1; fermentation <= FERMENTATION_STEPS; fermentation++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, fermentation);
            list.add(fluidStack);
        }

        for (int maturation = 1; maturation <= MATURATION_STEPS; maturation++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, FERMENTATION_STEPS);
            setMaturation(fluidStack, maturation);
            list.add(fluidStack);
        }
    }

    @Override
    public int fermentationTime(FluidStack stack, boolean openContainer)
    {
        if (getFermentation(stack) < FERMENTATION_STEPS)
        {
            if (openContainer)
                return PSConfig.ticksPerBeerFermentation;
        }
        else if (getMaturation(stack) < MATURATION_STEPS && !openContainer)
            return PSConfig.ticksPerBeerMaturation;

        return UNFERMENTABLE;
    }

    @Override
    public void fermentStep(FluidStack stack, boolean openContainer)
    {
        int fermentation = getFermentation(stack);
        int maturation = getMaturation(stack);

        if (fermentation < FERMENTATION_STEPS)
        {
            if (openContainer)
                setFermentation(stack, fermentation + 1);
        }
        else if (maturation < MATURATION_STEPS && !openContainer)
            setMaturation(stack, maturation + 1);
    }

    @Override
    public String getLocalizedName(FluidStack stack)
    {
        String s = this.getUnlocalizedName(stack);
        return s == null ? "" : StatCollector.translateToLocal(s);
    }

    @Override
    public String getUnlocalizedName(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        int maturation = getMaturation(stack);

        if (maturation == 0)
            return super.getUnlocalizedName(stack) + ".ferment" + fermentation;
        else
            return super.getUnlocalizedName(stack) + ".mature" + maturation;
    }

    public int getFermentation(FluidStack stack)
    {
        return stack.tag != null ? MathHelper.clamp_int(stack.tag.getInteger("fermentation"), 0, FERMENTATION_STEPS) : 0;
    }

    public void setFermentation(FluidStack stack, int fermentation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("fermentation", fermentation);
    }

    public int getMaturation(FluidStack stack)
    {
        return stack.tag != null ? MathHelper.clamp_int(stack.tag.getInteger("maturation"), 0, MATURATION_STEPS) : 0;
    }

    public void setMaturation(FluidStack stack, int maturation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("maturation", maturation);
    }
}
