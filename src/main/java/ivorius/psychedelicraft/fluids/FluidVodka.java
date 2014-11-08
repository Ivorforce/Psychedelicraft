/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.MCColorHelper;
import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by lukas on 27.10.14.
 */
public class FluidVodka extends FluidDrug implements FluidFermentable, FluidDistillable
{
    public static final int FERMENTATION_STEPS = 2;
    public static final int DISTILLATION_STEPS = 8;

    @SideOnly(Side.CLIENT)
    private IIcon iconWortStill;
    @SideOnly(Side.CLIENT)
    private IIcon iconWortFlow;
    @SideOnly(Side.CLIENT)
    private IIcon iconSemiWortStill;
    @SideOnly(Side.CLIENT)
    private IIcon iconSemiWortFlow;

    public FluidVodka(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);

        int fermentation = getFermentation(fluidStack);
        int distillation = getDistillation(fluidStack);

        double alcohol = (double) fermentation / (double) FERMENTATION_STEPS * 0.55
                + (double) distillation / (double) DISTILLATION_STEPS * 1.7;

        list.add(new DrugInfluence("Alcohol", 20, 0.003, 0.002, alcohol));
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

        for (int distillation = 1; distillation <= DISTILLATION_STEPS; distillation++)
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, FERMENTATION_STEPS);
            setDistillation(fluidStack, distillation);
            list.add(fluidStack);
        }
    }

    @Override
    public IIcon getIcon(FluidStack stack)
    {
        int distillation = getDistillation(stack);
        return distillation > 3 ? super.getIcon(stack) : distillation > 0 ? iconSemiWortStill : iconWortStill;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister, int textureType)
    {
        super.registerIcons(iconRegister, textureType);

        if (textureType == TEXTURE_TYPE_BLOCK)
        {
            iconWortStill = iconRegister.registerIcon(Psychedelicraft.modBase + "slurry_still");
            iconWortFlow = iconRegister.registerIcon(Psychedelicraft.modBase + "slurry_flow");
            iconSemiWortStill = iconRegister.registerIcon(Psychedelicraft.modBase + "semi_slurry_still");
            iconSemiWortFlow = iconRegister.registerIcon(Psychedelicraft.modBase + "semi_slurry_flow");
        }
    }

    @Override
    public int fermentationTime(FluidStack stack, boolean openContainer)
    {
        if (getFermentation(stack) < FERMENTATION_STEPS)
        {
            if (openContainer)
                return PSConfig.ticksPerVodkaFermentation;
        }

        return UNFERMENTABLE;
    }

    @Override
    public void fermentStep(FluidStack stack, boolean openContainer)
    {
        int fermentation = getFermentation(stack);

        if (fermentation < FERMENTATION_STEPS)
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

        if (fermentation < FERMENTATION_STEPS)
            return UNDISTILLABLE;
        else if (distillation < DISTILLATION_STEPS)
            return
                    PSConfig.ticksPerVodkaDistillation;

        return UNDISTILLABLE;
    }

    @Override
    public FluidStack distillStep(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        int distillation = getDistillation(stack);

        if (fermentation < FERMENTATION_STEPS)
            return null;
        else if (distillation < DISTILLATION_STEPS)
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

    @Override
    public int getColor(FluidStack stack)
    {
        int slurryColor = 0xcc704E21;
        int clearColor = super.getColor(stack);
        int distillation = getDistillation(stack);

        return MCColorHelper.mixColors(slurryColor, clearColor, (float) distillation / (float) DISTILLATION_STEPS);
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

    public int getDistillation(FluidStack stack)
    {
        return stack.tag != null ? MathHelper.clamp_int(stack.tag.getInteger("distillation"), 0, DISTILLATION_STEPS) : 0;
    }

    public void setDistillation(FluidStack stack, int maturation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("distillation", maturation);
    }
}
