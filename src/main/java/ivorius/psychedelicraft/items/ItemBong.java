/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.ivtoolkit.tools.IvInventoryHelper;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by calebmanley on 4/05/2014.
 */
public class ItemBong extends Item
{
    public ArrayList<ItemBongConsumable> consumables = new ArrayList<>();

    private IIcon empty;

    public ItemBong()
    {
        super();

        setMaxDamage(128);
        setMaxStackSize(1);
    }

    public void addConsumable(ItemBongConsumable consumable)
    {
        consumables.add(consumable);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.block;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        ItemBongConsumable usedConsumable = getUsedConsumable(player);

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

    public ItemBongConsumable getUsedConsumable(EntityPlayer player)
    {
        for (ItemBongConsumable consumable : consumables)
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
        return 30;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        empty = iconRegister.registerIcon(getIconString() + "_empty");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        int damage = stack.getItemDamage();

        if (damage == 3)
        {
            return empty;
        }

        return super.getIcon(stack, renderPass, player, usingItem, useRemaining);
    }

    @Override
    public IIcon getIconFromDamage(int damage)
    {
        if (damage == 3)
        {
            return empty;
        }

        return itemIcon;
    }

    public static class ItemBongConsumable
    {
        public ItemStack consumedItem;

        public DrugInfluence[] drugInfluences;
        public float[] smokeColor;

        public ItemBongConsumable(ItemStack consumedItem, DrugInfluence[] drugInfluences)
        {
            this(consumedItem, drugInfluences, new float[]{1.0f, 1.0f, 1.0f});
        }

        public ItemBongConsumable(ItemStack consumedItem, DrugInfluence[] drugInfluences, float[] smokeColor)
        {
            this.consumedItem = consumedItem;

            this.drugInfluences = drugInfluences;
            this.smokeColor = smokeColor;
        }
    }
}
