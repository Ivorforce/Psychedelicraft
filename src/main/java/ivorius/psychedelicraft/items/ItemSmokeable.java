/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
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
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(player);

        if (drugProperties != null)
        {
            for (DrugInfluence drugInfluence : drugEffects)
                drugProperties.addToDrug(drugInfluence.clone());

            drugProperties.startBreathingSmoke(10 + world.rand.nextInt(10), smokeColor);
        }

        if (stack.getItemDamage() < textures.length - 1)
            stack.setItemDamage(stack.getItemDamage() + 1);
        else
            stack.stackSize--;

        return super.onEaten(stack, world, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (DrugProperties.getDrugProperties(player).timeBreathingSmoke == 0)
        {
            player.setItemInUse(stack, getMaxItemUseDuration(stack));
        }

        return stack;
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

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < textureNames.length; i++)
        {
            textures[i] = iconRegister.registerIcon(getIconString() + "_" + textureNames[i]);
        }

        for (int i = 0; i < inUseTextureNames.length; i++)
        {
            if (inUseTextureNames[i] != null)
            {
                inUseTextures[i] = iconRegister.registerIcon(getIconString() + "_" + inUseTextureNames[i]);
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
    public IIcon getIconFromDamage(int damage)
    {
        if (damage < textures.length)
        {
            return textures[damage];
        }

        return textures[0];
    }
}
