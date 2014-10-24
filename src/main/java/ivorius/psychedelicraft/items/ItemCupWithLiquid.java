/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.client.rendering.RenderPassesCustom;
import ivorius.psychedelicraft.fluids.FluidHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by lukas on 23.10.14.
 */
public class ItemCupWithLiquid extends ItemCup implements RenderPassesCustom
{
    protected IIcon liquidIcon;

    public ItemCupWithLiquid(int capacity)
    {
        super(capacity);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        this.liquidIcon = iconRegister.registerIcon(this.getIconString() + "_liquid");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return pass == 1 ? liquidIcon : super.getIcon(stack, pass);
    }

    // Required because otherwise, stack is not passed for icon query
    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 1;
    }

    @Override
    public boolean hasAlphaCustom(ItemStack stack, int pass)
    {
        return pass == 1;
    }

    @Override
    public int getRenderPassesCustom(ItemStack stack)
    {
        return getFluid(stack) != null ? 2 : 1;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        if (pass == 1)
            return FluidHelper.getTranslucentFluidColor(stack);
        else
            return super.getColorFromItemStack(stack, pass) | 0xff000000;
    }
}
