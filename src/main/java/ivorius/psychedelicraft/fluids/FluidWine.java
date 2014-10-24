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
            float wineStrength = getFermentation(fluidStack);
            list.add(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.3 * wineStrength));
        }
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(FluidStack fluidStack)
    {
        if (isVinegar(fluidStack))
            return null;

        float wineStrength = getFermentation(fluidStack);

        if (wineStrength < 0.3f)
        {
            int foodLevel = MathHelper.floor_float(IvMathHelper.zeroToOne(wineStrength, 0.0f, 0.3f) * 3.0f + 0.5f);
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
            fluidStack.tag.setFloat("fermentation", strength / (float)(FERMENTATION_STEPS - 1));
            list.add(fluidStack);
        }

        FluidStack fluidStack = new FluidStack(this, 1);
        fluidStack.tag = new NBTTagCompound();
        fluidStack.tag.setFloat("fermentation", 1.0f);
        fluidStack.tag.setFloat("acetification", 1.0f);
        list.add(fluidStack);
    }

    @Override
    public void updateFermenting(FluidStack stack)
    {
        if (stack.tag == null)
            stack.tag = new NBTTagCompound();

        float fermentation = getFermentation(stack);
        if (fermentation < 1.0f)
        {
            fermentation = IvMathHelper.clamp(0.0f, fermentation + 1.0f / PSConfig.ticksForFullWineFermentation, 1.0f);
            stack.tag.setFloat("fermentation", fermentation);
        }
        else
        {
            float acetification = stack.tag.getFloat("acetification");
            if (acetification < 1.0f)
            {
                acetification = IvMathHelper.clamp(0.0f, acetification + 1.0f / PSConfig.ticksUntilWineAcetification, 1.0f);
                stack.tag.setFloat("acetification", acetification);
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

        float wineStrength = getFermentation(stack);
        return super.getUnlocalizedName(stack) + ".quality" + (MathHelper.floor_float(wineStrength * (FERMENTATION_STEPS - 1) + 0.5f));
    }

    public boolean isVinegar(FluidStack stack)
    {
        return stack.tag != null && stack.tag.getFloat("acetification") >= 1.0f;
    }

    public float getFermentation(FluidStack stack)
    {
        return stack.tag != null ? IvMathHelper.clamp(0.0f, stack.tag.getFloat("fermentation"), 1.0f) : 0.0f;
    }
}
