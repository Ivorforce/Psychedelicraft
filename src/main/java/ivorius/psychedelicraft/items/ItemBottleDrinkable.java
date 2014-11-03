/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.crafting.ItemPouring;
import ivorius.psychedelicraft.fluids.DrinkableFluid;
import ivorius.psychedelicraft.fluids.FluidHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by lukas on 23.10.14.
 */
public class ItemBottleDrinkable extends ItemBottle implements ItemPouring
{
    public ItemBottleDrinkable(int capacity)
    {
        super(capacity);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        FluidHelper.drink(stack, player, ItemCup.FLUID_PER_DRINKING, true);

        return super.onEaten(stack, world, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (FluidHelper.drink(stack, player, ItemCup.FLUID_PER_DRINKING, false) != null)
            player.setItemInUse(stack, getMaxItemUseDuration(stack));

        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int dmg = 0; dmg < 16; dmg++)
        {
            list.add(new ItemStack(item, 1, dmg));

            for (FluidStack fluidStack : FluidHelper.allFluids(DrinkableFluid.SUBTYPE, capacity))
            {
                ItemStack stack = new ItemStack(item, 1, dmg);
                fill(stack, fluidStack, true);
                list.add(stack);
            }
        }
    }

    @Override
    public boolean canPour(ItemStack stack, ItemStack dst)
    {
        return true;
    }

    @Override
    public boolean canReceivePour(ItemStack stack, ItemStack src)
    {
        return false;
    }
}
