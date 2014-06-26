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
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemHarmonium extends Item
{
    public IIcon iconGlowstoneLayer;

    public ItemHarmonium()
    {
        super();
        setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);

        iconGlowstoneLayer = par1IconRegister.registerIcon(getIconString() + "Glowstone");
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 16; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);

        return super.getUnlocalizedName() + ".dye" + var2;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 2;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        if (par2 == 0)
        {
            int damage = par1ItemStack.getItemDamage();

            if (damage < 16)
            {
                return ItemDye.field_150922_c[damage];
            }
        }

        return 0xffffff;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        if (pass == 0)
        {
            return super.getIconFromDamageForRenderPass(damage, pass);
        }

        return iconGlowstoneLayer;
    }
}
