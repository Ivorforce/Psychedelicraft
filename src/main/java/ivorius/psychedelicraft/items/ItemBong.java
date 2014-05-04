package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.DrugHelper;
import ivorius.psychedelicraft.entities.DrugInfluence;
import ivorius.psychedelicraft.ivToolkit.IvInventoryHelper;
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
    public ArrayList<ItemBongConsumable> consumables = new ArrayList<ItemBongConsumable>();

    private IIcon empty;

    public ItemBong()
    {
        super();

        setMaxDamage(3);
        setMaxStackSize(1);
    }

    public void addConsumable(ItemBongConsumable consumable)
    {
        consumables.add(consumable);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.block;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        ItemBongConsumable usedConsumable = getUsedConsumable(par3EntityPlayer);

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
            if (getUsedConsumable(par3EntityPlayer) != null && par1ItemStack.getItemDamage() != 3)
            {
                par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
            }
        }

        return par1ItemStack;
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
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
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
    public IIcon getIconFromDamage(int i)
    {
        if (i == 3)
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
