/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.crafting.ItemPouring;
import ivorius.psychedelicraft.fluids.DrinkableFluid;
import ivorius.psychedelicraft.fluids.FluidHelper;
import ivorius.psychedelicraft.fluids.PSFluids;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;

import java.util.List;

/**
 * Created by lukas on 20.10.14.
 */
public class ItemCup extends ItemFluidContainer implements ItemPouring
{
    public static final int FLUID_PER_DRINKING = FluidHelper.MILLIBUCKETS_PER_LITER / 4;

    public ItemCup(int capacity)
    {
        super(0, capacity);
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return getFluid(stack) == null ? maxStackSize : 1;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        FluidHelper.drink(stack, player, FLUID_PER_DRINKING, true);

        return super.onEaten(stack, world, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (FluidHelper.drink(stack, player, FLUID_PER_DRINKING, false) != null)
            player.setItemInUse(stack, getMaxItemUseDuration(stack));

        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);

        if (fluidStack != null)
        {
            String fluidName = fluidStack.getLocalizedName();
            return StatCollector.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".full.name", fluidName);
        }

        return super.getItemStackDisplayName(stack);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        super.getSubItems(item, tab, list);

        for (FluidStack fluidStack : FluidHelper.allFluids(DrinkableFluid.SUBTYPE, capacity))
            list.add(PSFluids.filledStack(this, fluidStack));
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

    @Override
    public boolean canPour(ItemStack stack, ItemStack dst)
    {
        return false;
    }

    @Override
    public boolean canReceivePour(ItemStack stack, ItemStack src)
    {
        return true;
    }
}
