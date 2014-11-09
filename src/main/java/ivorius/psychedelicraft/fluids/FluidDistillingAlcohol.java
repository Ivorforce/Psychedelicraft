/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by lukas on 27.10.14.
 */
public class FluidDistillingAlcohol extends FluidDrug implements FluidFermentable, FluidDistillable
{
    public int fermentationSteps;
    public int distillationSteps;

    public float fermentationAlcohol;
    public float distillationAlcohol;

    public TickInfo tickInfo;

    public FluidDistillingAlcohol(String fluidName, int fermentationSteps, int distillationSteps, float fermentationAlcohol, float distillationAlcohol, TickInfo tickInfo)
    {
        super(fluidName);
        this.fermentationSteps = fermentationSteps;
        this.distillationSteps = distillationSteps;
        this.fermentationAlcohol = fermentationAlcohol;
        this.distillationAlcohol = distillationAlcohol;
        this.tickInfo = tickInfo;
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);

        int fermentation = getFermentation(fluidStack);
        int distillation = getDistillation(fluidStack);

        double alcohol = (double) fermentation / (double) fermentationSteps * fermentationAlcohol
                + (double) distillation / (double) distillationSteps * distillationAlcohol;

        list.add(new DrugInfluence("Alcohol", 20, 0.003, 0.002, alcohol));
    }

    @Override
    public void addCreativeSubtypes(String listType, List<FluidStack> list)
    {
        if (listType.equals(FluidFermentable.SUBTYPE_OPEN))
        {
            for (int fermentation = 0; fermentation <= fermentationSteps; fermentation++)
            {
                FluidStack fluidStack = new FluidStack(this, 1);
                setFermentation(fluidStack, fermentation);
                list.add(fluidStack);
            }
        }

        if (listType.equals(DrinkableFluid.SUBTYPE) || listType.equals(FluidFermentable.SUBTYPE_CLOSED))
        {
            for (int distillation = 1; distillation <= distillationSteps; distillation++)
            {
                FluidStack fluidStack = new FluidStack(this, 1);
                setFermentation(fluidStack, fermentationSteps);
                setDistillation(fluidStack, distillation);
                list.add(fluidStack);
            }
        }

        if (listType.equals(ExplodingFluid.SUBTYPE))
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, fermentationSteps);
            setDistillation(fluidStack, distillationSteps);
            list.add(fluidStack);
        }
    }

    @Override
    public int fermentationTime(FluidStack stack, boolean openContainer)
    {
        if (getFermentation(stack) < fermentationSteps)
        {
            if (openContainer)
                return tickInfo.ticksPerFermentation;
        }

        return UNFERMENTABLE;
    }

    @Override
    public void fermentStep(FluidStack stack, boolean openContainer)
    {
        int fermentation = getFermentation(stack);

        if (fermentation < fermentationSteps)
        {
            if (openContainer)
                setFermentation(stack, fermentation + 1);
        }
    }

    @Override
    public int distillationTime(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        int distillation = getDistillation(stack);

        if (fermentation < fermentationSteps)
            return UNDISTILLABLE;
        else if (distillation < distillationSteps)
            return tickInfo.ticksPerDistillation;

        return UNDISTILLABLE;
    }

    @Override
    public FluidStack distillStep(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        int distillation = getDistillation(stack);

        if (fermentation < fermentationSteps)
            return null;
        else if (distillation < distillationSteps)
        {
            setDistillation(stack, distillation + 1);
            int distilledAmount = MathHelper.floor_float(stack.amount * (1.0f - 0.5f / ((float) distillation + 1.0f)));

            FluidStack slurry = new FluidStack(PSFluids.slurry, stack.amount - distilledAmount);
            stack.amount = distilledAmount;
            return slurry.amount > 0 ? slurry : null;
        }

        return null;
    }

    @Override
    public String getLocalizedName(FluidStack stack)
    {
        String s = this.getUnlocalizedName(stack);

        int distillation = getDistillation(stack);

        if (distillation > 0)
            return I18n.format(super.getUnlocalizedName(stack) + ".distill", distillation);

        return s == null ? "" : StatCollector.translateToLocal(s);
    }

    @Override
    public String getUnlocalizedName(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        int distillation = getDistillation(stack);

        if (distillation == 0)
            return super.getUnlocalizedName(stack) + ".ferment" + fermentation;

        return super.getUnlocalizedName(stack);
    }

    public int getFermentation(FluidStack stack)
    {
        return stack.tag != null ? MathHelper.clamp_int(stack.tag.getInteger("fermentation"), 0, fermentationSteps) : 0;
    }

    public void setFermentation(FluidStack stack, int fermentation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("fermentation", fermentation);
    }

    public int getDistillation(FluidStack stack)
    {
        return stack.tag != null ? MathHelper.clamp_int(stack.tag.getInteger("distillation"), 0, distillationSteps) : 0;
    }

    public void setDistillation(FluidStack stack, int maturation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("distillation", maturation);
    }

    public static class TickInfo
    {
        public int ticksPerFermentation;
        public int ticksPerDistillation;
    }
}
