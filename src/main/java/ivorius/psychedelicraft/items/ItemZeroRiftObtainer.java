/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by lukas on 04.03.14.
 */
public class ItemZeroRiftObtainer extends Item
{
    public ItemZeroRiftObtainer()
    {
        super();

        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        super.getSubItems(p_150895_1_, p_150895_2_, p_150895_3_);

        p_150895_3_.add(new ItemStack(this, 1, 1));
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return super.getUnlocalizedName(par1ItemStack) + (par1ItemStack.getItemDamage() == 0 ? ".empty" : ".filled");
    }
}
