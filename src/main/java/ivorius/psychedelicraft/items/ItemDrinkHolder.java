/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukas on 14.05.14.
 */
public class ItemDrinkHolder extends Item
{
    public Map<String, IIcon> registeredSpecialIcons = new HashMap<>();

    public boolean addEmptySelfToCreativeMenu = true;

    public ItemDrinkHolder()
    {
        setHasSubtypes(true);
    }

    public DrinkInformation getDrinkInfo(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("drinkInfo", Constants.NBT.TAG_COMPOUND) ? new DrinkInformation(stack.getTagCompound().getCompoundTag("drinkInfo")) : null;
    }

    public ItemStack createDrinkStack(int stackSize, DrinkInformation drinkInformation)
    {
        ItemStack stack = new ItemStack(this, stackSize);
        stack.setTagInfo("drinkInfo", drinkInformation.writeToNBT());
        return stack;
    }

    public int getMaxDrinkFilling()
    {
        return 1;
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
                ItemStack stack = createDrinkStack(1, new DrinkInformation(DrinkRegistry.getDrinkID(drink), 1, compound));
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
