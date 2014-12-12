/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEdibleDrug extends Item
{
    public DrugInfluence influence;

    public ItemEdibleDrug(DrugInfluence influence)
    {
        super();
        this.influence = influence;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.eat;
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(player);
        if (drugProperties != null)
            drugProperties.addToDrug(influence.clone());

        itemStack.stackSize--;

        return super.onEaten(itemStack, world, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));

        return par1ItemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }
}
