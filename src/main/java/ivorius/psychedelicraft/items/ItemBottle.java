/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by lukas on 21.10.14.
 */
public class ItemBottle extends ItemDrinkHolder
{
    private IIcon corkIcon;

    public ItemBottle()
    {
        maxStackSize = 16;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public int getMaxDrinkFilling()
    {
        return 4;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        corkIcon = par1IconRegister.registerIcon(getIconString() + "_cork");
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 2;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return pass == 0 ? super.getIcon(stack, pass) : corkIcon;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        return pass == 0 ? ItemDye.field_150922_c[stack.getItemDamage() % ItemDye.field_150922_c.length] : super.getColorFromItemStack(stack, pass);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return new ItemStack(this, 1, itemStack.getItemDamage());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < 16; i++)
            getSubItems(item, tab, list, i);
    }
}
