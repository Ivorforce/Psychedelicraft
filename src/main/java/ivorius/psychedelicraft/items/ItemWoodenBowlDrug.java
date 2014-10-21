/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by lukas on 21.10.14.
 */
public class ItemWoodenBowlDrug extends ItemDrinkable
{
    public ItemWoodenBowlDrug(int maxFillings)
    {
        super(maxFillings);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return getDrinkInfo(stack) != null;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return new ItemStack(Items.bowl);
    }
}
