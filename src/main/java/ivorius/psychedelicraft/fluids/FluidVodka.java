package ivorius.psychedelicraft.fluids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.MCColorHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by lukas on 09.11.14.
 */
public class FluidVodka extends FluidDistillingMaturingAlcohol
{
    @SideOnly(Side.CLIENT)
    private IIcon iconWortStill;

    @SideOnly(Side.CLIENT)
    private IIcon iconWortFlow;

    @SideOnly(Side.CLIENT)
    private IIcon iconSemiWortStill;

    @SideOnly(Side.CLIENT)
    private IIcon iconSemiWortFlow;

    @SideOnly(Side.CLIENT)
    private IIcon iconSemiMatureStill;

    @SideOnly(Side.CLIENT)
    private IIcon iconSemiMatureFlow;

    @SideOnly(Side.CLIENT)
    private IIcon iconMatureStill;

    @SideOnly(Side.CLIENT)
    private IIcon iconMatureFlow;

    @SideOnly(Side.CLIENT)
    private IIcon iconKorn;
    @SideOnly(Side.CLIENT)
    private IIcon iconWhisky;

    public FluidVodka(String fluidName, int fermentationSteps, int distillationSteps, int maturationSteps, float fermentationAlcohol, float distillationAlcohol, float maturationAlcohol, TickInfo tickInfo)
    {
        super(fluidName, fermentationSteps, distillationSteps, maturationSteps, fermentationAlcohol, distillationAlcohol, maturationAlcohol, tickInfo);
    }

    @Override
    public int fermentationTime(FluidStack stack, boolean openContainer)
    {
        int distillation = getDistillation(stack);
        if (distillation > 4) // No Whisky / Korn is distilled over 4 times
            return UNFERMENTABLE;

        return super.fermentationTime(stack, openContainer);
    }

    @Override
    public void addCreativeSubtypes(String listType, List<FluidStack> list)
    {
        // Wort
        if (listType.equals(FluidFermentable.SUBTYPE_OPEN))
        {
            for (int fermentation = 0; fermentation <= fermentationSteps; fermentation++)
            {
                FluidStack fluidStack = new FluidStack(this, 1);
                setFermentation(fluidStack, fermentation);
                list.add(fluidStack);
            }
        }

        // Vodka
        if (listType.equals(DrinkableFluid.SUBTYPE) || listType.equals(FluidFermentable.SUBTYPE_CLOSED))
        {
            for (int distillation = 1; distillation <= distillationSteps; distillation++)
            {
                FluidStack fluidStack = new FluidStack(this, 1);
                setFermentation(fluidStack, fermentationSteps);
                setDistillation(fluidStack, distillation);
                list.add(fluidStack);
            }
        }

        // Whisky
        if (listType.equals(DrinkableFluid.SUBTYPE) || listType.equals(FluidFermentable.SUBTYPE_CLOSED))
        {
            for (int maturation = 1; maturation <= maturationSteps; maturation++)
            {
                FluidStack fluidStack = new FluidStack(this, 1);
                setFermentation(fluidStack, fermentationSteps);
                setDistillation(fluidStack, 2);
                setMaturation(fluidStack, maturation);
                list.add(fluidStack);
            }
        }

        if (listType.equals(ExplodingFluid.SUBTYPE))
        {
            FluidStack fluidStack = new FluidStack(this, 1);
            setFermentation(fluidStack, fermentationSteps);
            setDistillation(fluidStack, distillationSteps);
            list.add(fluidStack);
        }
    }

    @Override
    public IIcon getIconSymbol(FluidStack fluidStack, int textureType)
    {
        if (textureType == TEXTURE_TYPE_ITEM)
        {
            if (isKorn(fluidStack))
                return iconKorn;
            if (isWhisky(fluidStack))
                return iconWhisky;
        }

        return super.getIconSymbol(fluidStack, textureType);
    }

    @Override
    public IIcon getIcon(FluidStack stack)
    {
        int distillation = getDistillation(stack);
        int maturation = getMaturation(stack);

        if (maturation > 1)
            return maturation > 2 ? iconMatureStill : iconSemiMatureStill;

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
            iconSemiMatureStill = iconRegister.registerIcon(Psychedelicraft.modBase + "rum_semi_mature_still");
            iconSemiMatureFlow = iconRegister.registerIcon(Psychedelicraft.modBase + "rum_semi_mature_flow");
            iconMatureStill = iconRegister.registerIcon(Psychedelicraft.modBase + "rum_mature_still");
            iconMatureFlow = iconRegister.registerIcon(Psychedelicraft.modBase + "rum_mature_flow");
        }
        else if (textureType == TEXTURE_TYPE_ITEM)
        {
            iconKorn = iconRegister.registerIcon(Psychedelicraft.modBase + "drinkKorn");
            iconWhisky = iconRegister.registerIcon(Psychedelicraft.modBase + "drinkWhisky");
        }
    }

    @Override
    public int getColor(FluidStack stack)
    {
        int slurryColor = 0xcc704E21;
        int semiMatureColor = 0x88f4a100;
        int matureColor = 0xcc592518;
        int clearColor = super.getColor(stack);

        int maturation = getMaturation(stack);
        if (maturation > 0)
            return MCColorHelper.mixColors(semiMatureColor, matureColor, (float) maturation / (float) maturationSteps);

        int distillation = getDistillation(stack);
        if (distillation > 0)
            return MCColorHelper.mixColors(slurryColor, clearColor, (float) distillation / (float) distillationSteps);

        return slurryColor;
    }

    @Override
    public String getLocalizedName(FluidStack stack)
    {
        String s = this.getUnlocalizedName(stack);

        int distillation = getDistillation(stack);
        int maturation = getMaturation(stack);
        String kornPart = isKorn(stack) ? ".korn" : "";

        if (maturation > 0)
            return I18n.format(super.getUnlocalizedName(stack) + ".mature" + maturation + kornPart, distillation);
        else if (distillation > 0)
            return I18n.format(super.getUnlocalizedName(stack) + ".distill" + kornPart, distillation);

        return s == null ? "" : StatCollector.translateToLocal(s);
    }

    @Override
    public String getUnlocalizedName(FluidStack stack)
    {
        int fermentation = getFermentation(stack);
        int distillation = getDistillation(stack);
        String potatoPart = isMadeFromPotato(stack) ? ".potato" : "";

        if (distillation == 0)
            return getUnlocalizedName() + potatoPart + ".ferment" + fermentation;

        return getUnlocalizedName() + potatoPart;
    }

    public boolean isMadeFromPotato(FluidStack stack)
    {
        return stack.tag != null && stack.tag.getBoolean("madeFromPotato");
    }

    public void setMadeFromPotato(FluidStack stack, boolean madeFromPotato)
    {
        FluidHelper.ensureTag(stack);
        stack.tag.setBoolean("madeFromPotato", madeFromPotato);
    }

    public boolean isWhisky(FluidStack fluidStack)
    {
        return getMaturation(fluidStack) > 0 && !isKorn(fluidStack);
    }

    public boolean isKorn(FluidStack stack)
    {
        int distillation = getDistillation(stack);
        int maturation = getMaturation(stack);

        return !isMadeFromPotato(stack) && maturation <= 1 && distillation >= 1 && distillation < 4;
    }
}
