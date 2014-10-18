/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

/**
 * Created by lukas on 18.10.14.
 */
public class ItemFoodSpecial extends ItemFood
{
    public int eatSpeed;

    public ItemFoodSpecial(int healAmount, float saturation, boolean canFeedWolves, int eatSpeed)
    {
        super(healAmount, saturation, canFeedWolves);
        this.eatSpeed = eatSpeed;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return eatSpeed;
    }
}
