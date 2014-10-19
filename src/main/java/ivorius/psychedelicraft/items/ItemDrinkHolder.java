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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public class ItemDrinkHolder extends Item
{
    public static NBTTagCompound getDrinkInfo(ItemStack stack)
    {
        return stack.getTagCompound() != null ? stack.getTagCompound().getCompoundTag("drinkInfo") : new NBTTagCompound();
    }

    public static String getDrinkIDFromStack(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            return null;
        }

        return stack.getTagCompound().getString("drinkID");
    }

    public static String getDrinkTranslationKey(ItemStack stack)
    {
        String drinkKey = getDrinkIDFromStack(stack);

        if (drinkKey != null)
        {
            IDrink drink = DrinkRegistry.getDrink(drinkKey);

            if (drink != null)
                return DrinkRegistry.getDrinkTranslationKey(drink, getDrinkInfo(stack));
        }

        return null;
    }

    public static ItemStack createDrinkStack(Item item, int stackSize, String drinkID)
    {
        ItemStack stack = new ItemStack(item, stackSize);
        stack.setTagInfo("drinkID", new NBTTagString(drinkID));
        return stack;
    }

    public static IDrink getDrinkFromStack(ItemStack stack)
    {
        String drinkID = getDrinkIDFromStack(stack);
        return drinkID != null ? DrinkRegistry.getDrink(drinkID) : null;
    }

    public static String getDrinkSpecialIcon(ItemStack stack)
    {
        String drinkID = getDrinkIDFromStack(stack);
        return drinkID != null ? DrinkRegistry.getDrinkSpecialIcon(drinkID, stack.getItem()) : null;
    }

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
        IDrink drink = getDrinkFromStack(par1ItemStack);

        if (drink != null)
        {
            NBTTagCompound drinkInfo = getDrinkInfo(par1ItemStack);

            DrugHelper drugHelper = DrugHelper.getDrugHelper(par3EntityPlayer);

            if (drugHelper != null)
            {
                List<DrugInfluence> drugInfluences = drink.getDrugInfluences(drinkInfo);
                for (DrugInfluence influence : drugInfluences)
                {
                    drugHelper.addToDrug(influence.clone());
                }
            }

            Pair<Integer, Float> foodLevel = drink.getFoodLevel(drinkInfo);
            if (foodLevel != null)
            {
                par3EntityPlayer.getFoodStats().addStats(foodLevel.getLeft(), foodLevel.getRight());
            }

            drink.applyToEntity(drinkInfo, par3EntityPlayer, par2World);
        }

        par1ItemStack.stackSize--;
        par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(this));

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        IDrink drink = getDrinkFromStack(par1ItemStack);

        if (drink != null)
        {
            if (drink.getFoodLevel(getDrinkInfo(par1ItemStack)) == null || par3EntityPlayer.getFoodStats().needFood())
            {
                par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
            }
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
        String specialIcon = getDrinkSpecialIcon(stack);

        if (specialIcon != null && registeredSpecialIcons.containsKey(specialIcon))
        {
            return registeredSpecialIcons.get(specialIcon);
        }

        return super.getIcon(stack, pass);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack)
    {
        String specialIcon = getDrinkSpecialIcon(stack);

        if (specialIcon != null && registeredSpecialIcons.containsKey(specialIcon))
        {
            return registeredSpecialIcons.get(specialIcon);
        }

        return super.getIconIndex(stack);
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return super.getIconFromDamage(par1);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4)
    {
        super.addInformation(itemStack, player, list, par4);

        String specialKey = getDrinkTranslationKey(itemStack);

        if (specialKey != null)
        {
            list.add(StatCollector.translateToLocal(specialKey).trim());
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        if (addEmptySelfToCreativeMenu)
            super.getSubItems(item, tab, list);

        for (IDrink drink : DrinkRegistry.getAllDrinks())
        {
            for (NBTTagCompound compound : drink.creativeTabInfos(item, tab))
            {
                ItemStack stack = createDrinkStack(item, 1, DrinkRegistry.getDrinkID(drink));
                if (compound != null)
                    stack.setTagInfo("drinkInfo", compound);
                list.add(stack);
            }
        }
    }
}
