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
        double fermentation = getFermentation(fluidStack);
        list.add(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.2f * fermentation));
    }

    @Override
    public void addCreativeSubtypes(List<FluidStack> list)
    {
        super.addCreativeSubtypes(list);

        for (int strength = 1; strength < FERMENTATION_STEPS; strength++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            fluidStack.tag = new NBTTagCompound();
            fluidStack.tag.setDouble("fermentation", strength / (double) (FERMENTATION_STEPS - 1));
            list.add(fluidStack);
        }
    }

    @Override
    public void updateFermenting(FluidStack stack)
    {
        if (stack.tag == null)
            stack.tag = new NBTTagCompound();

        double fermentation = getFermentation(stack);
        if (fermentation < 1.0f)
        {
            double addFermentation = (1.0f - fermentation) * PSConfig.getBeerFermentationTickImprovement();
            fermentation = IvMathHelper.clamp(0.0f, fermentation + addFermentation, 1.0f);
            stack.tag.setDouble("fermentation", fermentation);
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
        double fermentation = getFermentation(stack);
        return super.getUnlocalizedName(stack) + ".quality" + (MathHelper.floor_double(fermentation * (FERMENTATION_STEPS - 1) + 0.5f));
    }

    public double getFermentation(FluidStack stack)
    {
        return stack.tag != null ? IvMathHelper.clamp(0.0f, stack.tag.getDouble("fermentation"), 1.0f) : 0.0f;
    }
}
