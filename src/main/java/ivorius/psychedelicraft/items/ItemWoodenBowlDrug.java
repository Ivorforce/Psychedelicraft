/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.fluids.DrinkableFluid;
import ivorius.psychedelicraft.fluids.FluidHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by lukas on 21.10.14.
 */
public class ItemWoodenBowlDrug extends ItemCupWithLiquid
{
    public ItemWoodenBowlDrug(int maxFillings)
    {
        super(maxFillings);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("bowl");
        this.liquidIcon = iconRegister.registerIcon(this.getIconString() + "_liquid");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        // Don't add empty bowl
        for (FluidStack fluidStack : FluidHelper.allFluids(DrinkableFluid.SUBTYPE, capacity))
        {
            ItemStack stack = new ItemStack(item);
            fill(stack, fluidStack, true);
            list.add(stack);
        }
    }
}
