/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.items;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class ItemSyringe extends Item
{
    public Hashtable<Integer, DrugInfluence[]> effects;
    public Hashtable<Integer, Integer> liquidColors;
    public Hashtable<Integer, String> names;

    public IIcon iconLiquid;

    public ItemSyringe()
    {
        super();

        this.effects = new Hashtable<Integer, DrugInfluence[]>();
        this.liquidColors = new Hashtable<Integer, Integer>();
        this.names = new Hashtable<Integer, String>();

        this.setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    public int addEffect(int damage, DrugInfluence[] effects, int color, String name)
    {
        this.effects.put(damage, effects);
        this.liquidColors.put(damage, color);
        this.names.put(damage, name);

        return damage;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (effects.containsKey(par1ItemStack.getItemDamage()))
        {
            DrugInfluence[] influences = effects.get(par1ItemStack.getItemDamage());

            for (DrugInfluence influence : influences)
            {
                DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(influence.clone());
            }
        }

        par1ItemStack.setItemDamage(0);

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.getItemDamage() > 0)
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
        return false;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);

        iconLiquid = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "syringeLiquid");
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int par1, int par2)
    {
        if (par1 > 0 && par2 == 0)
        {
            return iconLiquid;
        }

        return super.getIconFromDamageForRenderPass(par1, par2);
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        if (par2 == 0 && liquidColors.containsKey(par1ItemStack.getItemDamage()))
        {
            return liquidColors.get(par1ItemStack.getItemDamage());
        }

        return super.getColorFromItemStack(par1ItemStack, par2);
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        super.getSubItems(par1, par2CreativeTabs, par3List);

        Set<Integer> keys = liquidColors.keySet();

        for (Integer i : keys)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (names.containsKey(par1ItemStack.getItemDamage()))
        {
            return super.getUnlocalizedName(par1ItemStack) + "." + names.get(par1ItemStack.getItemDamage());
        }

        return super.getUnlocalizedName(par1ItemStack);
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        if (par1ItemStack.getItemDamage() > 0)
        {
            if (!par2EntityLivingBase.worldObj.isRemote && par2EntityLivingBase.getRNG().nextFloat() < 0.5f)
            {
                DrugHelper drugHelper = DrugHelper.getDrugHelper(par2EntityLivingBase);

                if (drugHelper != null && effects.containsKey(par1ItemStack.getItemDamage()))
                {
                    DrugInfluence[] influences = effects.get(par1ItemStack.getItemDamage());

                    for (DrugInfluence influence : influences)
                    {
                        drugHelper.addToDrug(influence.clone());
                    }
                }

                par1ItemStack.setItemDamage(0);
            }
        }

        return false;
    }
}
