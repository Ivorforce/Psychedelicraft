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
    public static final int WARMTH_STEPS = 3;

    public FluidCoffee(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);

        float warmth = getCoffeeTemperature(fluidStack);
        list.add(new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.25f + warmth * 0.05f));
        list.add(new DrugInfluence("Warmth", 0, 0.00, 0.1, 0.8f * warmth));
    }

    @Override
    public void addCreativeSubtypes(List<FluidStack> list)
    {
        super.addCreativeSubtypes(list);

        for (int warmth = 1; warmth < WARMTH_STEPS; warmth++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            fluidStack.tag = new NBTTagCompound();
            fluidStack.tag.setFloat("temperature", warmth / (float)(WARMTH_STEPS - 1));
            list.add(fluidStack);
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
        float warmth = getCoffeeTemperature(stack);
        return super.getUnlocalizedName(stack) + ".temperature" + (MathHelper.floor_float(warmth * (WARMTH_STEPS - 1) + 0.5f));
    }

    public float getCoffeeTemperature(FluidStack stack)
    {
        return stack.tag != null ? IvMathHelper.clamp(0.0f, stack.tag.getFloat("temperature"), 1.0f) : 0.0f;
    }
}
