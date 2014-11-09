package ivorius.psychedelicraft.fluids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.MCColorHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by lukas on 09.11.14.
 */
public class FluidRum extends FluidDistillingMaturingAlcohol
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

    public FluidRum(String fluidName, int fermentationSteps, int distillationSteps, int maturationSteps, float fermentationAlcohol, float distillationAlcohol, float maturationAlcohol, TickInfo tickInfo)
    {
        super(fluidName, fermentationSteps, distillationSteps, maturationSteps, fermentationAlcohol, distillationAlcohol, maturationAlcohol, tickInfo);
    }

    @Override
    public IIcon getIcon(FluidStack stack)
    {
        int distillation = getDistillation(stack);
        int maturation = getMaturation(stack);

        if (maturation > 0)
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
}
