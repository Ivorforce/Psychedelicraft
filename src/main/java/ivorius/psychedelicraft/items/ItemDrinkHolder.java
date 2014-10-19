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
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukas on 14.05.14.
 */
public class ItemDrinkHolder extends Item
{
    public static DrinkInformation getDrinkInfo(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("drinkInfo", Constants.NBT.TAG_COMPOUND) ? new DrinkInformation(stack.getTagCompound().getCompoundTag("drinkInfo")) : null;
    }

    public static ItemStack createDrinkStack(Item item, int stackSize, DrinkInformation drinkInformation)
    {
        ItemStack stack = new ItemStack(item, stackSize);
        stack.setTagInfo("drinkInfo", drinkInformation.writeToNBT());
        return stack;
    }

    public Map<String, IIcon> registeredSpecialIcons = new HashMap<>();

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
    public ItemStack onEaten(ItemStack stack, World par2World, EntityPlayer player)
    {
        DrinkInformation drinkInfo = getDrinkInfo(stack);

        if (drinkInfo != null)
        {
            DrugHelper drugHelper = DrugHelper.getDrugHelper(player);

            if (drugHelper != null)
            {
                List<DrugInfluence> drugInfluences = drinkInfo.getDrugInfluences();
                for (DrugInfluence influence : drugInfluences)
                {
                    drugHelper.addToDrug(influence.clone());
                }
            }

            Pair<Integer, Float> foodLevel = drinkInfo.getFoodLevel();
            if (foodLevel != null)
            {
                player.getFoodStats().addStats(foodLevel.getLeft(), foodLevel.getRight());
            }

            drinkInfo.applyToEntity(player, par2World);
        }

        stack.stackSize--;
        player.inventory.addItemStackToInventory(new ItemStack(this));

        return super.onEaten(stack, par2World, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        DrinkInformation drinkInfo = getDrinkInfo(stack);

        if (drinkInfo != null)
        {
            if (drinkInfo.getFoodLevel() == null || player.getFoodStats().needFood())
            {
                player.setItemInUse(stack, getMaxItemUseDuration(stack));
            }
        }

        return stack;
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
        DrinkInformation drinkInfo = getDrinkInfo(stack);

        if (drinkInfo != null)
        {
            String specialIcon = drinkInfo.getSpecialIcon(this);
            if (specialIcon != null && registeredSpecialIcons.containsKey(specialIcon))
                return registeredSpecialIcons.get(specialIcon);
        }

        return super.getIcon(stack, pass);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack)
    {
        DrinkInformation drinkInfo = getDrinkInfo(stack);

        if (drinkInfo != null)
        {
            String specialIcon = drinkInfo.getSpecialIcon(this);
            if (specialIcon != null && registeredSpecialIcons.containsKey(specialIcon))
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

        DrinkInformation drinkInfo = getDrinkInfo(itemStack);

        if (drinkInfo != null)
        {
            String translationKey = drinkInfo.getFullTranslationKey();
            if (translationKey != null)
                list.add(StatCollector.translateToLocal(translationKey).trim());
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
                ItemStack stack = createDrinkStack(item, 1, new DrinkInformation(DrinkRegistry.getDrinkID(drink), 1, compound));
                list.add(stack);
            }
        }
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return getDrinkInfo(stack) != null;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return new ItemStack(this);
    }
}
