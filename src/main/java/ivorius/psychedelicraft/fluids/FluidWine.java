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
            double fermentation = getFermentation(fluidStack);
            list.add(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.3 * fermentation));
        }
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(FluidStack fluidStack)
    {
        if (isVinegar(fluidStack))
            return null;

        double fermentation = getFermentation(fluidStack);

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

        for (int strength = 1; strength < FERMENTATION_STEPS; strength++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            fluidStack.tag = new NBTTagCompound();
            fluidStack.tag.setDouble("fermentation", strength / (double)(FERMENTATION_STEPS - 1));
            list.add(fluidStack);
        }

        FluidStack fluidStack = new FluidStack(this, 1);
        fluidStack.tag = new NBTTagCompound();
        fluidStack.tag.setDouble("fermentation", 1.0);
        fluidStack.tag.setDouble("acetification", 1.0);
        list.add(fluidStack);
    }

    @Override
    public void updateFermenting(FluidStack stack)
    {
        if (stack.tag == null)
            stack.tag = new NBTTagCompound();

        double fermentation = getFermentation(stack);
        if (fermentation < 1.0)
        {
            fermentation = IvMathHelper.clamp(0.0, fermentation + 1.0 / PSConfig.ticksForFullWineFermentation, 1.0);
            stack.tag.setDouble("fermentation", fermentation);
        }
        else
        {
            double acetification = stack.tag.getDouble("acetification");
            if (acetification < 1.0)
            {
                acetification = IvMathHelper.clamp(0.0, acetification + 1.0 / PSConfig.ticksUntilWineAcetification, 1.0);
                stack.tag.setDouble("acetification", acetification);
            }
        }
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

        double fermentation = getFermentation(stack);
        return super.getUnlocalizedName(stack) + ".quality" + (MathHelper.floor_double(fermentation * (FERMENTATION_STEPS - 1) + 0.5f));
    }

    public boolean isVinegar(FluidStack stack)
    {
        return stack.tag != null && stack.tag.getDouble("acetification") >= 1.0f;
    }

    public double getFermentation(FluidStack stack)
    {
        return stack.tag != null ? IvMathHelper.clamp(0.0f, stack.tag.getDouble("fermentation"), 1.0f) : 0.0f;
    }
}
