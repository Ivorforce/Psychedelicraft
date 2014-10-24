/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.blocks.TileEntityBarrel;
import ivorius.psychedelicraft.fluids.DrinkableFluid;
import ivorius.psychedelicraft.fluids.FluidHelper;
import ivorius.psychedelicraft.fluids.FluidWithIconSymbol;
import ivorius.psychedelicraft.fluids.FluidWithIconSymbolRegistering;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ItemBarrel extends ItemBlockFluidContainer
{
    private IIcon spruceIcon;
    private IIcon darkOakIcon;

    public ItemBarrel(Block block)
    {
        super(block, TileEntityBarrel.BARREL_CAPACITY);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);

        if (fluidStack != null)
        {
            String fluidName = fluidStack.getLocalizedName();
            return I18n.format(this.getUnlocalizedNameInefficiently(stack) + ".full.name", fluidName);
        }

        return super.getItemStackDisplayName(stack);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        super.registerIcons(iconRegister);

        spruceIcon = iconRegister.registerIcon(this.field_150939_a.getItemIconName() + "_spruce");
        darkOakIcon = iconRegister.registerIcon(this.field_150939_a.getItemIconName() + "_darkOak");
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 2;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        if (pass == 1)
        {
            FluidStack fluidStack = getFluid(stack);
            if (fluidStack != null && fluidStack.getFluid() instanceof FluidWithIconSymbol)
            {
                IIcon iconSymbol = ((FluidWithIconSymbol) fluidStack.getFluid()).getIconSymbol(fluidStack, FluidWithIconSymbolRegistering.TEXTURE_TYPE_ITEM);
                if (iconSymbol != null)
                    return iconSymbol;
            }
        }

        if (stack.getItemDamage() == 1)
            return spruceIcon;
        else if (stack.getItemDamage() == 5)
            return darkOakIcon;

        return super.getIcon(stack, pass);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        super.getSubItems(item, tab, list);

        getSubItems(item, tab, list, 0); // Oak
        getSubItems(item, tab, list, 1); // Spruce
        getSubItems(item, tab, list, 5); // Dark oak
    }

    public void getSubItems(Item item, CreativeTabs tab, List list, int woodType)
    {
        list.add(new ItemStack(item, 1, woodType));

        for (FluidStack fluidStack : FluidHelper.allFluids(DrinkableFluid.SUBTYPE, capacity))
        {
            ItemStack stack = new ItemStack(item, 1, woodType);
            fill(stack, fluidStack, true);
            list.add(stack);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);
        return fluidStack != null && fluidStack.amount < capacity;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);
        return fluidStack != null ? 1.0 - ((double) fluidStack.amount / (double) capacity) : 0.0;
    }
}
