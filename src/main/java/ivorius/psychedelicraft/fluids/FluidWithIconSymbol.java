/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by lukas on 22.10.14.
 */
public interface FluidWithIconSymbol
{
    @SideOnly(Side.CLIENT)
    IIcon getIconSymbol(FluidStack fluidStack, int textureType);
}
