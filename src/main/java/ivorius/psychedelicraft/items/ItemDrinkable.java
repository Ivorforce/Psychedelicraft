/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.DrugHelper;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by lukas on 20.10.14.
 */
public class ItemDrinkable extends ItemDrinkHolder
{
    private int maxFillings;

    public ItemDrinkable(int maxFillings)
    {
        setMaxStackSize(16);
        this.maxFillings = maxFillings;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World par2World, EntityPlayer player)
    {
        DrinkInformation drinkInfo = getDrinkInfo(stack);

        if (drinkInfo != null)
        {
            DrugHelper drugHelper = DrugHelper.getDrugHelper(player);

            if (drugHelper != null)
            {
                List<DrugInfluence> drugInfluences = drinkInfo.getDrugInfluences();
                for (DrugInfluence influence : drugInfluences)
                {
                    drugHelper.addToDrug(influence.clone());
                }
            }

            Pair<Integer, Float> foodLevel = drinkInfo.getFoodLevel();
            if (foodLevel != null)
            {
                player.getFoodStats().addStats(foodLevel.getLeft(), foodLevel.getRight());
            }

            drinkInfo.applyToEntity(player, par2World);

            drinkInfo.decrementFillings(1);
            if (drinkInfo.getFillings() <= 0)
                stack.stackSize --;
        }

        if (hasContainerItem(stack))
            player.inventory.addItemStackToInventory(getContainerItem(stack));

        return super.onEaten(stack, par2World, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        DrinkInformation drinkInfo = getDrinkInfo(stack);

        if (drinkInfo != null)
        {
            if (drinkInfo.getFoodLevel() == null || player.getFoodStats().needFood())
            {
                player.setItemInUse(stack, getMaxItemUseDuration(stack));
            }
        }

        return stack;
    }

    @Override
    public int getMaxDrinkFilling()
    {
        return maxFillings;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }
}
