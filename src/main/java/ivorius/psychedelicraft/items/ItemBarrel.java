/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.blocks.TileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class ItemBarrel extends ItemBlock
{
    public static final int DEFAULT_FILLINGS = 16;

    public ItemStack createBarrel(String drinkID, int fillings)
    {
        ItemStack stack = new ItemStack(this);
        stack.setTagInfo("drinkID", new NBTTagString(drinkID));
        stack.setTagInfo("drinkFillings", new NBTTagInt(fillings));
        return stack;
    }

    public static String containedDrinkID(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("drinkID", Constants.NBT.TAG_STRING) ? stack.getTagCompound().getString("drinkID") : null;
    }

    public static IDrink containedDrink(ItemStack stack)
    {
        String id = containedDrinkID(stack);
        return id != null ? DrinkRegistry.getDrink(id) : null;
    }

    public static int containedFillings(ItemStack stack)
    {
        return stack.hasTagCompound() ? stack.getTagCompound().getInteger("drinkFillings") : 0;
    }

    public static NBTTagCompound getDrinkInfo(ItemStack stack)
    {
        return stack.getTagCompound() != null ? stack.getTagCompound().getCompoundTag("drinkInfo") : new NBTTagCompound();
    }

    private IIcon baseIcon;

    public ItemBarrel(Block block)
    {
        super(block);

        maxStackSize = 16;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par3World.getBlock(par4, par5, par6) == Blocks.snow)
        {
            par5--;
        }

        if (par7 != 1)
        {
            return false;
        }

        par5++;

        int direction = MathHelper.floor_double((par2EntityPlayer.rotationYaw * 4F) / 360F + 0.5D) & 3;

        par3World.setBlock(par4, par5, par6, field_150939_a, direction, 3);

        TileEntity tileEntity = par3World.getTileEntity(par4, par5, par6);
        if (tileEntity != null && tileEntity instanceof TileEntityBarrel)
        {
            TileEntityBarrel tileEntityBarrel = (TileEntityBarrel) tileEntity;
            tileEntityBarrel.containedDrink = containedDrinkID(par1ItemStack);
            tileEntityBarrel.containedDrinkInfo = getDrinkInfo(par1ItemStack);
            tileEntityBarrel.containedFillings = containedFillings(par1ItemStack);
        }

        par1ItemStack.stackSize--;

        return true;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        
        for (IDrink drink : DrinkRegistry.getAllDrinks())
            drink.registerItemIcons(par1IconRegister);
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
        if (pass == 0)
            return super.getIcon(stack, pass);

        IDrink drink = containedDrink(stack);
        if (drink != null)
        {
            IIcon icon = drink.getDrinkIcon(getDrinkInfo(stack));
            if (icon != null)
                return icon;
        }

        return super.getIcon(stack, pass);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        super.getSubItems(item, tab, list);

        for (IDrink drink : DrinkRegistry.getAllDrinks())
        {
            for (NBTTagCompound compound : drink.creativeTabInfos(item, tab))
            {
                ItemStack stack = createBarrel(DrinkRegistry.getDrinkID(drink), DEFAULT_FILLINGS);
                if (compound != null)
                    stack.setTagInfo("drinkInfo", compound);
                list.add(stack);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List par3List, boolean par4)
    {
        super.addInformation(stack, player, par3List, par4);

        IDrink drink = containedDrink(stack);

        if (drink != null)
        {
            par3List.add(StatCollector.translateToLocal(DrinkRegistry.getDrinkTranslationKey(drink, getDrinkInfo(stack))).trim());
        }
    }
}
