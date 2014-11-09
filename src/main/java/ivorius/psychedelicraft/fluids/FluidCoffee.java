/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by lukas on 22.10.14.
 */
public class FluidCoffee extends FluidDrug
{
    public static final int WARMTH_STEPS = 2;

    public FluidCoffee(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);

        float warmth = (float)getCoffeeTemperature(fluidStack) / (float) WARMTH_STEPS;
        list.add(new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.25f + warmth * 0.05f));
        list.add(new DrugInfluence("Warmth", 0, 0.00, 0.1, 0.8f * warmth));
    }

    @Override
    public void addCreativeSubtypes(String listType, List<FluidStack> list)
    {
        super.addCreativeSubtypes(listType, list);

        if (listType.equals(DrinkableFluid.SUBTYPE) || listType.equals(FluidFermentable.SUBTYPE_CLOSED))
        {
            for (int temperature = 1; temperature <= WARMTH_STEPS; temperature++)
            {
                FluidStack fluidStack = new FluidStack(this, 1);
                setCoffeeTemperature(fluidStack, temperature);
                list.add(fluidStack);
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
        int warmth = getCoffeeTemperature(stack);
        return super.getUnlocalizedName(stack) + ".temperature" + warmth;
    }

    public void setCoffeeTemperature(FluidStack stack, int temperature)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("temperature", temperature);
    }

    public int getCoffeeTemperature(FluidStack stack)
    {
        return stack.tag != null ? stack.tag.getInteger("temperature") : 0;
    }
}
