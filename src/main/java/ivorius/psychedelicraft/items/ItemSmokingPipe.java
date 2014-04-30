/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.DrugHelper;
import ivorius.psychedelicraft.entities.DrugInfluence;
import ivorius.psychedelicraft.ivToolkit.IvInventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ItemSmokingPipe extends Item
{
    public ArrayList<ItemSmokingPipeConsumable> consumables = new ArrayList<ItemSmokingPipeConsumable>();

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
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        ItemSmokingPipeConsumable usedConsumable = getUsedConsumable(par3EntityPlayer);

        if (usedConsumable != null)
        {
            if (IvInventoryHelper.consumeInventoryItem(par3EntityPlayer.inventory, usedConsumable.consumedItem))
            {
                for (DrugInfluence influence : usedConsumable.drugInfluences)
                {
                    DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(influence.clone());
                }

                par1ItemStack.damageItem(1, par3EntityPlayer);

                DrugHelper.getDrugHelper(par3EntityPlayer).startBreathingSmoke(10 + par2World.rand.nextInt(10), usedConsumable.smokeColor);
            }
        }

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(par3EntityPlayer);

        if (drugHelper != null && drugHelper.timeBreathingSmoke <= 0)
        {
            if (getUsedConsumable(par3EntityPlayer) != null)
            {
                par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
            }
        }

        return par1ItemStack;
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
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
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
