/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.items;

import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemSmokeable extends Item
{
    DrugInfluence[] drugEffects;

    String[] textureNames;
    String[] inUseTextureNames;
    IIcon[] textures;
    IIcon[] inUseTextures;

    public float[] smokeColor;

    public ItemSmokeable(DrugInfluence[] drugEffects, String[] textureNames, String[] inUseTextures)
    {
        super();

        this.drugEffects = drugEffects;

        this.textureNames = textureNames;
        this.inUseTextureNames = inUseTextures;
        this.textures = new IIcon[textureNames.length];
        this.inUseTextures = new IIcon[inUseTextureNames.length];

        this.setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(textures.length < 2 ? 64 : 1);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        for (DrugInfluence drugInfluence : drugEffects)
        {
            DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(drugInfluence.clone());
        }

        if (par1ItemStack.getItemDamage() < textures.length - 1)
        {
            par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
        }
        else
        {
            par1ItemStack.stackSize--;
        }

        DrugHelper.getDrugHelper(par3EntityPlayer).startBreathingSmoke(10 + par2World.rand.nextInt(10), smokeColor);

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (DrugHelper.getDrugHelper(par3EntityPlayer).timeBreathingSmoke == 0)
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
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

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        for (int i = 0; i < textureNames.length; i++)
        {
            textures[i] = par1IconRegister.registerIcon(getIconString() + "_" + textureNames[i]);
        }

        for (int i = 0; i < inUseTextureNames.length; i++)
        {
            if (inUseTextureNames[i] != null)
            {
                inUseTextures[i] = par1IconRegister.registerIcon(getIconString() + "_" + inUseTextureNames[i]);
            }
        }
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        int damage = stack.getItemDamage();

        if (damage < inUseTextures.length && inUseTextures[damage] != null)
        {
            return inUseTextures[damage];
        }

        return super.getIcon(stack, renderPass, player, usingItem, useRemaining);
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        if (par1 < textures.length)
        {
            return textures[par1];
        }

        return textures[0];
    }
}
