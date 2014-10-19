/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.psychedelicraft.items.DrinkRegistry;
import ivorius.psychedelicraft.items.IDrink;
import ivorius.psychedelicraft.items.ItemDrinkHolder;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBarrel extends BlockContainer
{
    public BlockBarrel()
    {
        super(Material.wood);

        setStepSound(soundTypeWood);
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
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        if (willHarvest)
        {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityBarrel)
            {
                TileEntityBarrel tileEntityBarrel = (TileEntityBarrel) tileEntity;
                String drinkID = tileEntityBarrel.containedDrink;
                if (drinkID != null && tileEntityBarrel.containedFillings > 0)
                {
                    ItemStack barrel = PSItems.itemBarrel.createBarrel(drinkID, tileEntityBarrel.containedFillings);

                    if (tileEntityBarrel.containedDrinkInfo != null)
                        barrel.setTagInfo("drinkInfo", tileEntityBarrel.containedDrinkInfo);

                    dropBlockAsItem(world, x, y, z, barrel);
                }
                else
                {
                    dropBlockAsItem(world, x, y, z, new ItemStack(PSItems.itemBarrel));
                }
            }
        }

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_)
    {

    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityBarrel)
        {
            TileEntityBarrel tileEntityBarrel = (TileEntityBarrel) tileEntity;

            ItemStack heldItem = player.getHeldItem();

            if (heldItem != null && heldItem.getItem() instanceof ItemDrinkHolder)
            {
                IDrink drink = tileEntityBarrel.containedDrink != null ? DrinkRegistry.getDrink(tileEntityBarrel.containedDrink) : null;

                if (!world.isRemote && drink != null && tileEntityBarrel.containedFillings > 0)
                {
                    int rotation = tileEntityBarrel.getBlockRotation();
                    float xPlus = rotation == 1 ? 1.0f : (rotation == 3 ? -1.0f : 0.0f);
                    float zPlus = rotation == 0 ? -1.0f : (rotation == 2 ? 1.0f : 0.0f);

                    ItemStack itemStack = drink.createItemStack((ItemDrinkHolder) heldItem.getItem(), tileEntityBarrel.containedDrinkInfo, tileEntityBarrel.ticksExisted);

                    EntityItem entityItem = new EntityItem(world, x + xPlus + 0.5f, y + 0.5f, z + zPlus + 0.5f, itemStack);
                    entityItem.delayBeforeCanPickup = 10;
                    world.spawnEntityInWorld(entityItem);

                    tileEntityBarrel.containedFillings--;

                    if (player.getHeldItem().stackSize > 1)
                    {
                        player.getHeldItem().stackSize--;
                    }
                    else
                    {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
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
    public String getItemIconName()
    {
        return getTextureName();
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityBarrel();
    }
}
