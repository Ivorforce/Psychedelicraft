/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.DrugHelper;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Hashtable;
import java.util.List;

public class ItemWoodenBowlDrug extends Item
{
    public Hashtable<Integer, DrugInfluence[]> effects;
    public Hashtable<Integer, String> translationNames;
    public Hashtable<Integer, String> iconFiles;
    public Hashtable<Integer, IIcon> icons;

    public ItemWoodenBowlDrug()
    {
        super();

        setHasSubtypes(true);
        setMaxStackSize(4);

        effects = new Hashtable<Integer, DrugInfluence[]>();
        translationNames = new Hashtable<Integer, String>();
        iconFiles = new Hashtable<Integer, String>();
        icons = new Hashtable<Integer, IIcon>();
    }

    public int addEffect(int damage, DrugInfluence[] effects, String translationName, String iconFile)
    {
        this.effects.put(damage, effects);
        this.translationNames.put(damage, translationName);
        this.iconFiles.put(damage, iconFile);

        return damage;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        int dmg = par1ItemStack.getItemDamage();

        if (effects.containsKey(dmg))
        {
            for (DrugInfluence influence : effects.get(dmg))
            {
                DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(influence.clone());
            }
        }

        par1ItemStack.stackSize--;

        par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.bowl));

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

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("bowl");

        for (Integer damage : iconFiles.keySet())
        {
            icons.put(damage, par1IconRegister.registerIcon(iconFiles.get(damage)));
        }
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        if (icons.containsKey(par1))
        {
            return icons.get(par1);
        }

        return super.getIconFromDamage(par1);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (translationNames.containsKey(par1ItemStack.getItemDamage()))
        {
            return super.getUnlocalizedName(par1ItemStack) + "." + translationNames.get(par1ItemStack.getItemDamage());
        }

        return super.getUnlocalizedName(par1ItemStack);
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (Integer damage : translationNames.keySet())
        {
            par3List.add(new ItemStack(par1, 1, damage));
        }
    }
}
