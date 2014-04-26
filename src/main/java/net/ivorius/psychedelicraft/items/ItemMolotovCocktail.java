/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.items;

import net.ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemMolotovCocktail extends Item
{
    public ItemMolotovCocktail()
    {
        super();
        maxStackSize = 16;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par1ItemStack.stackSize--;
        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            EntityMolotovCocktail molotovCocktail = new EntityMolotovCocktail(par2World, par3EntityPlayer);
            molotovCocktail.fireStrength = par1ItemStack.getItemDamage();
            if (par1ItemStack.getItemDamage() == 14 || par1ItemStack.getItemDamage() == 15)
            {
                molotovCocktail.fireStrength = 0;
            }

            par2World.spawnEntityInWorld(molotovCocktail);
        }

        return par1ItemStack;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return super.getUnlocalizedName() + ".cocktail" + par1ItemStack.getItemDamage();
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 8; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4 * 2));
        }
    }
}
