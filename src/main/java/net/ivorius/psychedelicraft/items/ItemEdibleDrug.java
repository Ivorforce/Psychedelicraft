package net.ivorius.psychedelicraft.items;

import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.entities.DrugInfluence;
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
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(influence.clone());

        par1ItemStack.stackSize--;

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
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
