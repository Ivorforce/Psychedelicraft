/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.psychedelicraft.items.ItemBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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
    public BarrelEntry[] entries = new BarrelEntry[50];

    public BlockBarrel()
    {
        super(Material.wood);

        setStepSound(soundTypeWood);
    }

    public static void registerBarrelEntry(int id, Block barrel, BarrelEntry entry, ItemBarrel.BarrelEntry itemEntry)
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

            if (par5EntityPlayer.getHeldItem() != null && par5EntityPlayer.getHeldItem().getItem() == tileEntityBarrel.getBarrelType().requiredItem && par5EntityPlayer.getHeldItem().getItemDamage() == tileEntityBarrel.getBarrelType().requiredItemDamage)
            {
                if (!par1World.isRemote && tileEntityBarrel.currentContainedItems > 0)
                {
                    int rotation = tileEntityBarrel.getBlockRotation();
                    float xPlus = rotation == 1 ? 1.0f : (rotation == 3 ? -1.0f : 0.0f);
                    float zPlus = rotation == 0 ? -1.0f : (rotation == 2 ? 1.0f : 0.0f);

                    EntityItem var13 = new EntityItem(par1World, par2 + xPlus + 0.5f, par3 + 0.5f, par4 + zPlus + 0.5f, new ItemStack(tileEntityBarrel.getBarrelType().containedItem, 1, MathHelper.floor_double(tileEntityBarrel.currentItemDamage)));
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

    public static class BarrelEntry
    {
        public ResourceLocation barrelTexture;

        public Item requiredItem;
        public int requiredItemDamage;

        public Item containedItem;
        public int containedItemQuantity;
        public int maxMetadata;
        public int startMetadata;

        public BarrelEntry(ResourceLocation texture, Item reqItem, int reqItemDamage, Item containedItem, int containedItemQuantity, int maxMetadata, int startMetadata)
        {
            this.barrelTexture = texture;

            this.requiredItem = reqItem;
            this.requiredItemDamage = reqItemDamage;

            this.containedItem = containedItem;
            this.containedItemQuantity = containedItemQuantity;

            this.maxMetadata = maxMetadata;
            this.startMetadata = startMetadata;
        }
    }
}
