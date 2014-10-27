/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by lukas on 22.10.14.
 */
public class FluidBeer extends FluidDrug implements FluidFermentable
{
    public static final int FERMENTATION_STEPS = 6;

    public FluidBeer(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);
        double fermentation = (double) getFermentation(fluidStack) / (double) (FERMENTATION_STEPS - 1);
        list.add(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.2f * fermentation));
    }

    @Override
    public void addCreativeSubtypes(List<FluidStack> list)
    {
        for (int strength = 0; strength < FERMENTATION_STEPS; strength++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, strength);
            list.add(fluidStack);
        }
    }

    @Override
    public int fermentationTime(FluidStack stack)
    {
        if (getFermentation(stack) < FERMENTATION_STEPS - 1)
            return PSConfig.ticksPerWineFermentation;

        return UNFERMENTABLE;
    }

    @Override
    public void fermentStep(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        if (fermentation < FERMENTATION_STEPS - 1)
            setFermentation(stack, fermentation + 1);
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
        return super.getUnlocalizedName(stack) + ".quality" + fermentation;
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
