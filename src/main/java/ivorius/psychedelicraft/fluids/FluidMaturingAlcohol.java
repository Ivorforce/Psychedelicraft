/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by lukas on 22.10.14.
 */
public class FluidMaturingAlcohol extends FluidDrug implements FluidFermentable
{
    public int fermentationSteps;
    public int maturationSteps;

    public double fermentationAlcohol;
    public double maturationAlcohol;

    public MaturationInfo config;

    public static double zeroToOne(double value, double min, double max)
    {
        return IvMathHelper.clamp(0.0, (value - min) / (max - min), 1.0);
    }

    public FluidMaturingAlcohol(String fluidName, int fermentationSteps, int maturationSteps, double fermentationAlcohol, double maturationAlcohol, MaturationInfo config)
    {
        super(fluidName);
        this.fermentationSteps = fermentationSteps;
        this.maturationSteps = maturationSteps;
        this.fermentationAlcohol = fermentationAlcohol;
        this.maturationAlcohol = maturationAlcohol;
        this.config = config;
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);

        if (!isVinegar(fluidStack))
        {
            int fermentation = getFermentation(fluidStack);
            int maturation = getMaturation(fluidStack);

            double alcohol = (double) fermentation / (double) fermentationSteps * fermentationAlcohol
                    + (double) maturation / (double) maturationSteps * maturationAlcohol;

            list.add(new DrugInfluence("Alcohol", 20, 0.002, 0.001, alcohol));
        }
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(FluidStack fluidStack)
    {
        if (isVinegar(fluidStack))
            return null;

        double fermentation = (double) getFermentation(fluidStack) / (double) (fermentationSteps - 1);
        if (fermentation < 0.3f)
        {
            int foodLevel = MathHelper.floor_double(zeroToOne(fermentation, 0.0, 0.3) * 3.0 + 0.5);
            return new MutablePair<>(foodLevel, foodLevel * 0.1f);
        }

        return null;
    }

    @Override
    public void addCreativeSubtypes(List<FluidStack> list)
    {
        super.addCreativeSubtypes(list);

        for (int fermentation = 1; fermentation <= fermentationSteps; fermentation++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, fermentation);
            list.add(fluidStack);
        }

        for (int maturation = 1; maturation <= maturationSteps; maturation++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, fermentationSteps);
            setMaturation(fluidStack, maturation);
            list.add(fluidStack);
        }

        FluidStack fluidStack = new FluidStack(this, 1);
        setFermentation(fluidStack, fermentationSteps);
        setIsVinegar(fluidStack, true);
        list.add(fluidStack);
    }

    @Override
    public int fermentationTime(FluidStack stack, boolean openContainer)
    {
        int fermentation = getFermentation(stack);
        int maturation = getMaturation(stack);

        if (fermentation < fermentationSteps)
        {
            if (openContainer)
                return config.ticksPerFermentation;
        }
        else if (openContainer)
            return config.ticksUntilAcetification;
        else if (maturation < maturationSteps)
            return config.ticksPerMaturation;

        return UNFERMENTABLE;
    }

    @Override
    public void fermentStep(FluidStack stack, boolean openContainer)
    {
        int fermentation = getFermentation(stack);
        int maturation = getMaturation(stack);

        if (fermentation < fermentationSteps)
        {
            if (openContainer)
                setFermentation(stack, fermentation + 1);
        }
        else if (openContainer)
            setIsVinegar(stack, true);
        else if (maturation < maturationSteps)
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
        if (isVinegar(stack))
            return super.getUnlocalizedName(stack) + ".vinegar";

        int fermentation = getFermentation(stack);
        int maturation = getMaturation(stack);

        if (maturation == 0)
            return super.getUnlocalizedName(stack) + ".ferment" + fermentation;
        else
            return super.getUnlocalizedName(stack) + ".mature" + maturation;
    }

    public boolean isVinegar(FluidStack stack)
    {
        return stack.tag != null && stack.tag.getBoolean("isVinegar");
    }

    public void setIsVinegar(FluidStack stack, boolean isVinegar)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setBoolean("isVinegar", isVinegar);
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

    public int getMaturation(FluidStack stack)
    {
        return stack.tag != null ? MathHelper.clamp_int(stack.tag.getInteger("maturation"), 0, maturationSteps) : 0;
    }

    public void setMaturation(FluidStack stack, int maturation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("maturation", maturation);
    }

    public static class MaturationInfo
    {
        public int ticksPerFermentation;
        public int ticksPerMaturation;
        public int ticksUntilAcetification;
    }
}
