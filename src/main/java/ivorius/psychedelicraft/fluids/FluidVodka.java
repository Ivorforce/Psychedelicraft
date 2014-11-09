package ivorius.psychedelicraft.fluids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.psychedelicraft.client.rendering.MCColorHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by lukas on 09.11.14.
 */
public class FluidVodka extends FluidDistillingAlcohol
{
    @SideOnly(Side.CLIENT)
    private IIcon iconWortStill;

    @SideOnly(Side.CLIENT)
    private IIcon iconWortFlow;

    @SideOnly(Side.CLIENT)
    private IIcon iconSemiWortStill;

    @SideOnly(Side.CLIENT)
    private IIcon iconSemiWortFlow;

    public FluidVodka(String fluidName, int fermentationSteps, int distillationSteps, float fermentationAlcohol, float distillationAlcohol, DistillationInfo distillationInfo)
    {
        super(fluidName, fermentationSteps, distillationSteps, fermentationAlcohol, distillationAlcohol, distillationInfo);
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
            iconWortStill = iconRegister.registerIcon("slurry_still");
            iconWortFlow = iconRegister.registerIcon("slurry_flow");
            iconSemiWortStill = iconRegister.registerIcon("semi_slurry_still");
            iconSemiWortFlow = iconRegister.registerIcon("semi_slurry_flow");
        }
    }

    @Override
    public int getColor(FluidStack stack)
    {
        int slurryColor = 0xcc704E21;
        int clearColor = super.getColor(stack);
        int distillation = getDistillation(stack);

        return MCColorHelper.mixColors(slurryColor, clearColor, (float) distillation / (float) distillationSteps);
    }
}
