/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import com.sun.tools.javac.util.Pair;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.DrugHelper;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public class ItemDrinkHolder extends Item
{
    public Hashtable<String, IIcon> registeredSpecialIcons = new Hashtable<String, IIcon>();

    public boolean addEmptySelfToCreativeMenu;

    public ItemDrinkHolder()
    {
        setHasSubtypes(true);
        setMaxStackSize(16);

        addEmptySelfToCreativeMenu = true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        IDrink drink = DrinkRegistry.getDrinkFromStack(par1ItemStack);

        if (drink != null)
        {
            DrugHelper drugHelper = DrugHelper.getDrugHelper(par3EntityPlayer);

            if (drugHelper != null)
            {
                List<DrugInfluence> drugInfluences = drink.getDrugInfluences(par1ItemStack);
                for (DrugInfluence influence : drugInfluences)
                {
                    drugHelper.addToDrug(influence.clone());
                }
            }

            Pair<Integer, Float> foodLevel = drink.getFoodLevel(par1ItemStack);
            if (foodLevel != null)
            {
                par3EntityPlayer.getFoodStats().addStats(foodLevel.fst, foodLevel.snd);
            }

            drink.applyToEntity(par1ItemStack, par2World, par3EntityPlayer);
        }

        par1ItemStack.stackSize--;
        par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(this));

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        IDrink drink = DrinkRegistry.getDrinkFromStack(par1ItemStack);

        if (drink != null)
        {
            if (drink.getFoodLevel(par1ItemStack) == null || par3EntityPlayer.getFoodStats().needFood())
                par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
        }

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
        super.registerIcons(par1IconRegister);

        for (String iconKey : DrinkRegistry.getSpecialIcons(this))
        {
            registeredSpecialIcons.put(iconKey, par1IconRegister.registerIcon(iconKey));
        }
    }

    ////////////////////////////////
    // Hotfix: Required so getIcon is called with stack as param...
    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 1;
    }
    ////////////////////////////////

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        String specialIcon = DrinkRegistry.getDrinkSpecialIcon(stack);

        if (specialIcon != null && registeredSpecialIcons.containsKey(specialIcon))
            return registeredSpecialIcons.get(specialIcon);

        return super.getIcon(stack, pass);
    }

    @Override
    public IIcon getIconIndex(ItemStack par1ItemStack)
    {
        String specialIcon = DrinkRegistry.getDrinkSpecialIcon(par1ItemStack);

        if (specialIcon != null && registeredSpecialIcons.containsKey(specialIcon))
            return registeredSpecialIcons.get(specialIcon);

        return super.getIconIndex(par1ItemStack);
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return super.getIconFromDamage(par1);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

        String specialKey = DrinkRegistry.getDrinkTranslationKey(par1ItemStack);

        if (specialKey != null)
            par3List.add(StatCollector.translateToLocal(specialKey).trim());
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (addEmptySelfToCreativeMenu)
            super.getSubItems(par1, par2CreativeTabs, par3List);

        for (String iconKey : DrinkRegistry.getAllDrinks())
        {
            par3List.add(DrinkRegistry.createDrinkStack(this, 1, iconKey));
        }
    }
}
