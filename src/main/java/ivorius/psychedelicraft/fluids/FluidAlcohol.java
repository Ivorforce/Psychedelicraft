package ivorius.psychedelicraft.fluids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.ivtoolkit.gui.IntegerRange;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.MCColorHelper;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukas on 25.11.14.
 */
public class FluidAlcohol extends FluidDrug implements FluidFermentable, FluidDistillable
{
    public int fermentationSteps;

    public double fermentationAlcohol;
    public double distillationAlcohol;
    public double maturationAlcohol;

    private int matureColor;
    private int distilledColor;

    public TickInfo tickInfo;

    public final List<NamedAlcohol> names = new ArrayList<>();
    public final List<AlcoholIcon> alcIcons = new ArrayList<>();

    public FluidAlcohol(String fluidName, int fermentationSteps, double fermentationAlcohol, double distillationAlcohol, double maturationAlcohol, TickInfo tickInfo)
    {
        super(fluidName);
        this.fermentationSteps = fermentationSteps;
        this.fermentationAlcohol = fermentationAlcohol;
        this.distillationAlcohol = distillationAlcohol;
        this.maturationAlcohol = maturationAlcohol;
        this.tickInfo = tickInfo;

        setDrinkable(true);

        matureColor = 0xcc592518;
        distilledColor = 0x33ffffff;

        setStillIconName(Psychedelicraft.modBase + "slurry_still");
        setFlowingIconName(Psychedelicraft.modBase + "slurry_flow");
    }

    public void addName(String iconName, IntegerRange maturationRange, IntegerRange distillationRange)
    {
        names.add(new NamedAlcohol(iconName, maturationRange, distillationRange));
    }

    public void addIcon(IntegerRange fermentationRange, IntegerRange maturationRange, IntegerRange distillationRange, String stillIconName, String flowingIconName)
    {
        alcIcons.add(new AlcoholIcon(fermentationRange, maturationRange, distillationRange, stillIconName, flowingIconName));
    }

    public int getMatureColor()
    {
        return matureColor;
    }

    public void setMatureColor(int matureColor)
    {
        this.matureColor = matureColor;
    }

    public int getDistilledColor()
    {
        return distilledColor;
    }

    public void setDistilledColor(int distilledColor)
    {
        this.distilledColor = distilledColor;
    }

    public <M extends AlcoholMatcher> M getMatchedValue(FluidStack stack, List<M> values)
    {
        int fermentation = getFermentation(stack);
        int distillation = getDistillation(stack);
        int maturation = getMaturation(stack);

        for (M alc : values)
        {
            if (alc.matches(fermentation, fermentationSteps, distillation, maturation))
                return alc;
        }

        return null;
    }

    public NamedAlcohol getSpecialName(FluidStack stack)
    {
        return getMatchedValue(stack, names);
    }

    public AlcoholIcon getSpecialIcon(FluidStack stack)
    {
        return getMatchedValue(stack, alcIcons);
    }

    public FluidStack fermentedFluidStack(int amount, int distillation, int maturation)
    {
        FluidStack fluidStack = new FluidStack(this, amount);
        setFermentation(fluidStack, fermentationSteps);
        setDistillation(fluidStack, distillation);
        setMaturation(fluidStack, maturation);
        return fluidStack;
    }

    public FluidStack fermentingFluidStack(int amount, int fermentation)
    {
        FluidStack fluidStack = new FluidStack(this, amount);
        setFermentation(fluidStack, fermentation);
        return fluidStack;
    }

    @Override
    public void getDrugInfluencesPerLiter(FluidStack fluidStack, List<DrugInfluence> list)
    {
        super.getDrugInfluencesPerLiter(fluidStack, list);

        int fermentation = getFermentation(fluidStack);
        int distillation = getDistillation(fluidStack);
        int maturation = getMaturation(fluidStack);

        double alcohol = (double) fermentation / (double) fermentationSteps * fermentationAlcohol
                + distillationAlcohol * (1.0 - 1.0 / (1.0 + (double) distillation))
                + maturationAlcohol * (1.0 - 1.0 / (1.0 + (double) maturation * 0.2));

        list.add(new DrugInfluence("Alcohol", 20, 0.003, 0.002, alcohol));
    }

    @Override
    public void addCreativeSubtypes(String listType, List<FluidStack> list)
    {
        if (listType.equals(FluidFermentable.SUBTYPE_OPEN))
        {
            for (int fermentation = 0; fermentation <= fermentationSteps; fermentation++)
                list.add(fermentingFluidStack(1, fermentation));
        }

        if (listType.equals(DrinkableFluid.SUBTYPE) || listType.equals(FluidFermentable.SUBTYPE_CLOSED))
        {
            for (int maturation = 0; maturation <= 1; maturation++)
                list.add(fermentedFluidStack(1, 0, maturation));

            for (int maturationW = 0; maturationW <= 2; maturationW++)
                list.add(fermentedFluidStack(1, 2, maturationW * 7));
        }

        if (listType.equals(ExplodingFluid.SUBTYPE))
        {
            FluidStack fluidStack = fermentedFluidStack(1, 2, 3);
            list.add(fluidStack);
        }
    }

    @Override
    public int fermentationTime(FluidStack stack, boolean openContainer)
    {
        if (getFermentation(stack) < fermentationSteps)
            return openContainer ? tickInfo.ticksPerFermentation : UNFERMENTABLE;
        else
            return openContainer ? tickInfo.ticksUntilAcetification : tickInfo.ticksPerMaturation;
    }

    @Override
    public ItemStack fermentStep(FluidStack stack, boolean openContainer)
    {
        int fermentation = getFermentation(stack);

        if (openContainer)
        {
            if (fermentation < fermentationSteps)
                setFermentation(stack, fermentation + 1);
            else
                setIsVinegar(stack, true);
        }
        else
            setMaturation(stack, getMaturation(stack) + 1);

        return null;
    }

    @Override
    public int distillationTime(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        int maturation = getMaturation(stack);

        if (fermentation < fermentationSteps)
            return UNDISTILLABLE;
        else if (maturation == 0)
            return tickInfo.ticksPerDistillation;

        return UNDISTILLABLE;
    }

    @Override
    public FluidStack distillStep(FluidStack stack)
    {
        int fermentation = getFermentation(stack);

        if (fermentation < fermentationSteps)
            return null;
        else
        {
            int distillation = getDistillation(stack);

            setDistillation(stack, distillation + 1);
            int distilledAmount = MathHelper.floor_float(stack.amount * (1.0f - 0.5f / ((float) distillation + 1.0f)));

            FluidStack slurry = new FluidStack(PSFluids.slurry, stack.amount - distilledAmount);
            stack.amount = distilledAmount;
            return slurry.amount > 0 ? slurry : null;
        }
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
        return stack.tag != null ? Math.max(stack.tag.getInteger("distillation"), 0) : 0;
    }

    public void setDistillation(FluidStack stack, int distillation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("distillation", distillation);
    }

    public int getMaturation(FluidStack stack)
    {
        return stack.tag != null ? Math.max(stack.tag.getInteger("maturation"), 0) : 0;
    }

    public void setMaturation(FluidStack stack, int maturation)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setInteger("maturation", maturation);
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

    @Override
    public String getLocalizedName(FluidStack stack)
    {
        String baseName = this.getUnlocalizedName(stack);

        if (isVinegar(stack))
            return StatCollector.translateToLocalFormatted(String.format("%s.vinegar", baseName));

        int fermentation = this.getFermentation(stack);
        int distillation = this.getDistillation(stack);
        int maturation = this.getMaturation(stack);

        if (distillation == 0)
        {
            if (maturation > 0)
                return StatCollector.translateToLocalFormatted(String.format("%s.mature", baseName), maturation);
            else if (fermentation > 0)
                return StatCollector.translateToLocalFormatted(String.format("%s.ferment.%d", baseName, fermentation));
            else
                return StatCollector.translateToLocalFormatted(baseName);
        }
        else
        {
            if (maturation > 0)
                return StatCollector.translateToLocalFormatted(String.format("%s.dmature", baseName), maturation, distillation);
            else
                return StatCollector.translateToLocalFormatted(String.format("%s.distill", baseName), distillation);
        }
    }

    @Override
    public void registerIcons(IIconRegister iconRegister, int textureType)
    {
        super.registerIcons(iconRegister, textureType);

        if (textureType == TEXTURE_TYPE_ITEM)
        {
            for (NamedAlcohol specialName : names)
                specialName.registerIcons(iconRegister);
        }
        else if (textureType == TEXTURE_TYPE_BLOCK)
        {
            for (AlcoholIcon icon : alcIcons)
            {
                icon.stillIcon = icon.stillIconName != null ? iconRegister.registerIcon(icon.stillIconName) : null;
                icon.flowingIcon = icon.flowingIconName != null ? iconRegister.registerIcon(icon.flowingIconName) : null;
            }
        }
    }

    @Override
    public IIcon getIconSymbol(FluidStack fluidStack, int textureType)
    {
        if (textureType == TEXTURE_TYPE_ITEM)
        {
            NamedAlcohol specialName = getSpecialName(fluidStack);
            if (specialName != null)
                return specialName.icon;
        }

        return super.getIconSymbol(fluidStack, textureType);
    }

    @Override
    public IIcon getIcon(FluidStack stack)
    {
        AlcoholIcon specialIcon = getSpecialIcon(stack);
        if (specialIcon != null)
            return specialIcon.stillIcon;

        return super.getIcon(stack);
    }

    @Override
    public int getColor(FluidStack stack)
    {
        int slurryColor = getColor();
        int matureColor = getMatureColor(stack);
        int clearColor = getDistilledColor(stack);

        int distillation = getDistillation(stack);
        int maturation = getMaturation(stack);

        int baseFluidColor = MCColorHelper.mixColors(slurryColor, clearColor, (1.0f - 1.0f / (1.0f + (float) distillation)));
        return MCColorHelper.mixColors(baseFluidColor, matureColor, (1.0f - 1.0f / (1.0f + (float) maturation * 0.2f)));
    }

    protected int getDistilledColor(FluidStack stack)
    {
        return distilledColor;
    }

    protected int getMatureColor(FluidStack stack)
    {
        return matureColor;
    }

    public static class TickInfo
    {
        public int ticksPerFermentation;
        public int ticksPerDistillation;
        public int ticksPerMaturation;
        public int ticksUntilAcetification;
    }

    public static class AlcoholMatcher
    {
        public IntegerRange fermentationRange;
        public IntegerRange maturationRange;
        public IntegerRange distillationRange;

        public AlcoholMatcher(IntegerRange fermentationRange, IntegerRange maturationRange, IntegerRange distillationRange)
        {
            this.fermentationRange = fermentationRange;
            this.maturationRange = maturationRange;
            this.distillationRange = distillationRange;
        }

        public boolean matches(int fermentation, int maxFermentation, int distillation, int maturation)
        {
            return (fermentationRange.getMin() < 0 ? fermentation >= maxFermentation : rangeContains(fermentationRange, fermentation))
                    && rangeContains(distillationRange, distillation)
                    && rangeContains(maturationRange, maturation);
        }

        private static boolean rangeContains(IntegerRange range, int value)
        {
            return value >= range.getMin() && (range.getMax() < 0 || value <= range.getMax());
        }
    }

    public static class NamedAlcohol extends AlcoholMatcher
    {
        public String iconName;

        @SideOnly(Side.CLIENT)
        public IIcon icon;

        public NamedAlcohol(String iconName, IntegerRange maturationRange, IntegerRange distillationRange)
        {
            super(new IntegerRange(-1, -1), maturationRange, distillationRange);
            this.iconName = iconName;
        }

        public void registerIcons(IIconRegister register)
        {
            icon = iconName != null ? register.registerIcon(iconName) : null;
        }
    }

    public static class AlcoholIcon extends AlcoholMatcher
    {
        public String stillIconName;
        @SideOnly(Side.CLIENT)
        public IIcon stillIcon;

        public String flowingIconName;
        @SideOnly(Side.CLIENT)
        public IIcon flowingIcon;

        public AlcoholIcon(IntegerRange fermentationRange, IntegerRange maturationRange, IntegerRange distillationRange, String stillIconName, String flowingIconName)
        {
            super(fermentationRange, maturationRange, distillationRange);
            this.stillIconName = stillIconName;
            this.flowingIconName = flowingIconName;
        }
    }
}
