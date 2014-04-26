/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.items;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWoodenMugColdCoffee extends Item
{
    public DrugInfluence[] drugInfluences;

    public ItemWoodenMugColdCoffee()
    {
        super();

        setHasSubtypes(true);
        setMaxStackSize(16);

        drugInfluences = new DrugInfluence[]{new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.25f)};
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        for (DrugInfluence influence : drugInfluences)
        {
            DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(influence.clone());
        }

        par1ItemStack.stackSize--;

        par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(this, 1, 0));

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
        return 64;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "woodenMugCoffee");
    }
}
