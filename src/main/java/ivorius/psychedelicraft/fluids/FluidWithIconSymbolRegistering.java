/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

/**
 * Created by lukas on 22.10.14.
 */
public interface FluidWithIconSymbolRegistering extends FluidWithIconSymbol
{
    public static final int TEXTURE_TYPE_ITEM = 1;
    public static final int TEXTURE_TYPE_BLOCK = 0;

    @SideOnly(Side.CLIENT)
    void registerIcons(IIconRegister iconRegister, int textureType);
}
