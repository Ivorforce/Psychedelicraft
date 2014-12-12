/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.ivtoolkit.tools.IvInventoryHelper;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ItemSmokingPipe extends Item
{
    public ArrayList<ItemSmokingPipeConsumable> consumables = new ArrayList<>();

    public ItemSmokingPipe()
    {
        super();

        setMaxDamage(50);
        setMaxStackSize(1);
    }

    public void addConsumable(ItemSmokingPipeConsumable consumable)
    {
        consumables.add(consumable);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        ItemSmokingPipeConsumable usedConsumable = getUsedConsumable(player);

        if (usedConsumable != null)
        {
            if (IvInventoryHelper.consumeInventoryItem(player.inventory, usedConsumable.consumedItem))
            {
                DrugProperties drugProperties = DrugProperties.getDrugProperties(player);

                if (drugProperties != null)
                {
                    for (DrugInfluence influence : usedConsumable.drugInfluences)
                    {
                        drugProperties.addToDrug(influence.clone());
                    }

                    stack.damageItem(1, player);

                    drugProperties.startBreathingSmoke(10 + world.rand.nextInt(10), usedConsumable.smokeColor);
                }
            }
        }

        return super.onEaten(stack, world, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(player);

        if (drugProperties != null && drugProperties.timeBreathingSmoke <= 0)
        {
            if (getUsedConsumable(player) != null)
            {
                player.setItemInUse(stack, getMaxItemUseDuration(stack));
            }
        }

        return stack;
    }

    public ItemSmokingPipeConsumable getUsedConsumable(EntityPlayer player)
    {
        for (ItemSmokingPipeConsumable consumable : consumables)
        {
            if (player.inventory.hasItemStack(consumable.consumedItem))
            {
                return consumable;
            }
        }

        return null;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 25;
    }

    @Override
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    public static class ItemSmokingPipeConsumable
    {
        public ItemStack consumedItem;

        public DrugInfluence[] drugInfluences;
        public float[] smokeColor;

        public ItemSmokingPipeConsumable(ItemStack consumedItem, DrugInfluence[] drugInfluences)
        {
            this(consumedItem, drugInfluences, new float[]{1.0f, 1.0f, 1.0f});
        }

        public ItemSmokingPipeConsumable(ItemStack consumedItem, DrugInfluence[] drugInfluences, float[] smokeColor)
        {
            this.consumedItem = consumedItem;

            this.drugInfluences = drugInfluences;
            this.smokeColor = smokeColor;
        }
    }
}
