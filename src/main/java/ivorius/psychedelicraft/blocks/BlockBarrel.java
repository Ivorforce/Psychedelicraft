/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.psychedelicraft.items.DrinkRegistry;
import ivorius.psychedelicraft.items.DrinkWine;
import ivorius.psychedelicraft.items.ItemBarrel;
import ivorius.psychedelicraft.items.ItemDrinkHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlockBarrel extends BlockContainer
{
    public IBarrelEntry[] entries = new IBarrelEntry[50];

    public BlockBarrel()
    {
        super(Material.wood);

        setStepSound(soundTypeWood);
    }

    public static void registerBarrelEntry(int id, Block barrel, IBarrelEntry entry, ItemBarrel.BarrelEntry itemEntry)
    {
        ((BlockBarrel) barrel).entries[id] = entry;
        ((ItemBarrel) Item.getItemFromBlock(barrel)).entries[id] = itemEntry;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        return Blocks.planks.getIcon(0, 0);
    }

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            for (int i = 0; i < 2; ++i)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(Blocks.planks, 1, 0));
            }
        }
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);
        if (tileEntity != null && tileEntity instanceof TileEntityBarrel)
        {
            TileEntityBarrel tileEntityBarrel = (TileEntityBarrel) tileEntity;

            IBarrelEntry barrelType = tileEntityBarrel.getBarrelType();
            ItemStack heldItem = par5EntityPlayer.getHeldItem();

            if (heldItem != null && heldItem.getItem() instanceof ItemDrinkHolder)
            {
                if (!par1World.isRemote && tileEntityBarrel.currentContainedItems > 0)
                {
                    int rotation = tileEntityBarrel.getBlockRotation();
                    float xPlus = rotation == 1 ? 1.0f : (rotation == 3 ? -1.0f : 0.0f);
                    float zPlus = rotation == 0 ? -1.0f : (rotation == 2 ? 1.0f : 0.0f);

                    ItemStack itemStack = barrelType.createItemStack(par5EntityPlayer, heldItem, tileEntityBarrel);
                    EntityItem var13 = new EntityItem(par1World, par2 + xPlus + 0.5f, par3 + 0.5f, par4 + zPlus + 0.5f, itemStack);
                    var13.delayBeforeCanPickup = 10;
                    par1World.spawnEntityInWorld(var13);

                    tileEntityBarrel.currentContainedItems--;

                    if (par5EntityPlayer.getHeldItem().stackSize > 1)
                    {
                        par5EntityPlayer.getHeldItem().stackSize--;
                    }
                    else
                    {
                        par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
                    }
                }

                tileEntityBarrel.timeLeftTapOpen = 20;

                return true;
            }
        }

        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {

    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityBarrel();
    }

    public static interface IBarrelEntry
    {
        public ItemStack createItemStack(EntityLivingBase entity, ItemStack usedItem, TileEntityBarrel tileEntity);

        public ResourceLocation getTexture(TileEntityBarrel tileEntity);

        public int getContainedItems();
    }

    public static class BarrelEntry implements IBarrelEntry
    {
        public ResourceLocation barrelTexture;

        public String containedDrink;
        public int containedItemQuantity;

        public BarrelEntry(ResourceLocation barrelTexture, String containedDrink, int containedItemQuantity)
        {
            this.barrelTexture = barrelTexture;
            this.containedDrink = containedDrink;
            this.containedItemQuantity = containedItemQuantity;
        }

        @Override
        public ItemStack createItemStack(EntityLivingBase entity, ItemStack usedItem, TileEntityBarrel tileEntity)
        {
            return DrinkRegistry.createDrinkStack(usedItem.getItem(), 1, containedDrink);
        }

        @Override
        public ResourceLocation getTexture(TileEntityBarrel tileEntity)
        {
            return barrelTexture;
        }

        @Override
        public int getContainedItems()
        {
            return containedItemQuantity;
        }
    }

    public static class BarrelEntryWine extends BarrelEntry
    {
        public BarrelEntryWine(ResourceLocation barrelTexture, String containedDrink, int containedItemQuantity)
        {
            super(barrelTexture, containedDrink, containedItemQuantity);
        }

        @Override
        public ItemStack createItemStack(EntityLivingBase entity, ItemStack usedItem, TileEntityBarrel tileEntity)
        {
            return DrinkWine.createWineStack(usedItem.getItem(), 1, tileEntity.ticksExisted / (20 * 60 * 15));
        }
    }
}
