/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.PSConfig;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lukas on 22.10.14.
 */
public class FluidWine extends FluidDrug implements FluidFermentable
{
    public static final int FERMENTATION_STEPS = 7;

    public static double zeroToOne(double value, double min, double max)
    {
        return IvMathHelper.clamp(0.0, (value - min) / (max - min), 1.0);
    }

    public FluidWine(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);

        if (!isVinegar(fluidStack))
        {
            double fermentation = (double) getFermentation(fluidStack) / (double) (FERMENTATION_STEPS - 1);
            list.add(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.3 * fermentation));
        }
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(FluidStack fluidStack)
    {
        if (isVinegar(fluidStack))
            return null;

        double fermentation = (double) getFermentation(fluidStack) / (double) (FERMENTATION_STEPS - 1);
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
        for (int fermentation = 0; fermentation < FERMENTATION_STEPS; fermentation++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, fermentation);
            list.add(fluidStack);
        }

        FluidStack fluidStack = new FluidStack(this, 1);
        setFermentation(fluidStack, FERMENTATION_STEPS - 1);
        setIsVinegar(fluidStack, true);
        list.add(fluidStack);
    }

    @Override
    public int fermentationTime(FluidStack stack)
    {
        if (getFermentation(stack) < FERMENTATION_STEPS - 1)
            return PSConfig.ticksPerWineFermentation;
        else if (PSConfig.ticksUntilWineAcetification >= 0 && !isVinegar(stack))
            return PSConfig.ticksUntilWineAcetification;

        return UNFERMENTABLE;
    }

    @Override
    public void fermentStep(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        if (fermentation < FERMENTATION_STEPS - 1)
            setFermentation(stack, fermentation + 1);
        else if (!isVinegar(stack))
            setIsVinegar(stack, true);
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
        return super.getUnlocalizedName(stack) + ".quality" + fermentation;
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
        return stack.tag != null ? MathHelper.clamp_int(stack.tag.getInteger("fermentation"), 0, FERMENTATION_STEPS - 1) : 0;
    }

    public void setFermentation(FluidStack stack, int fermentation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("fermentation", fermentation);
    }
}
