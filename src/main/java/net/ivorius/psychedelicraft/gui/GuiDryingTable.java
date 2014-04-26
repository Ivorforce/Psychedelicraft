/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.gui;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.blocks.PSBlocks;
import net.ivorius.psychedelicraft.blocks.SlotDryingTableResult;
import net.ivorius.psychedelicraft.blocks.TileEntityDryingTable;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiDryingTable extends GuiContainer
{
    public static ResourceLocation dryingTableBG = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "guiDryingTable.png");

    public GuiDryingTable(InventoryPlayer par1InventoryPlayer, World par2World, TileEntityDryingTable tileEntityDryingTable)
    {
        super(new ContainerDryingTable(par1InventoryPlayer, par2World, tileEntityDryingTable));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString("Drying Table", 28, 6, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(dryingTableBG);

        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        if (((ContainerDryingTable) this.inventorySlots).tileEntityDryingTable.dryingProgress > 0.0f)
        {
            this.drawTexturedModalRect(var5 + 88, var6 + 34, 176, 59, 25, 16);
        }

        int var7 = (int) (((ContainerDryingTable) this.inventorySlots).tileEntityDryingTable.dryingProgress * 24f); //Max 24, progress
        this.drawTexturedModalRect(var5 + 88, var6 + 34, 176, 42, var7 + 1, 16);

        int var8 = (int) (((ContainerDryingTable) this.inventorySlots).tileEntityDryingTable.heatRatio * 20f); //Max 20, sun
        this.drawTexturedModalRect(var5 + 148, var6 + 6 + (20 - var8), 176, 21 + (20 - var8), 20, var8);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public static class ContainerDryingTable extends Container
    {
        /**
         * The crafting matrix inventory (3x3).
         */
        private World worldObj;
        public TileEntityDryingTable tileEntityDryingTable;

        public ContainerDryingTable(InventoryPlayer par1InventoryPlayer, World par2World, TileEntityDryingTable tileEntityDryingTable)
        {
            this.worldObj = par2World;
            this.tileEntityDryingTable = tileEntityDryingTable;
            this.addSlotToContainer(new SlotDryingTableResult(par1InventoryPlayer.player, tileEntityDryingTable, 0, 124, 35));

            for (int x = 0; x < 3; ++x)
            {
                for (int y = 0; y < 3; ++y)
                {
                    this.addSlotToContainer(new Slot(tileEntityDryingTable, 1 + x * 3 + y, 30 + x * 18, 17 + y * 18));
                }
            }

            int var3;

            for (var3 = 0; var3 < 3; ++var3)
            {
                for (int var4 = 0; var4 < 9; ++var4)
                {
                    this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
                }
            }

            for (var3 = 0; var3 < 9; ++var3)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
            }
        }

        @Override
        public void onContainerClosed(EntityPlayer par1EntityPlayer)
        {
            super.onContainerClosed(par1EntityPlayer);

            if (!this.worldObj.isRemote)
            {
                for (int var2 = 0; var2 < 9; ++var2)
                {
                    ItemStack var3 = this.tileEntityDryingTable.getStackInSlotOnClosing(var2);

                    if (var3 != null)
                    {
                        par1EntityPlayer.dropPlayerItemWithRandomChoice(var3, false);
                    }
                }
            }
        }

        @Override
        public boolean canInteractWith(EntityPlayer par1EntityPlayer)
        {
            Block id = this.worldObj.getBlock(tileEntityDryingTable.xCoord, tileEntityDryingTable.yCoord, tileEntityDryingTable.zCoord);
            return id != PSBlocks.blockDryingTable ? false : par1EntityPlayer.getDistanceSq(tileEntityDryingTable.xCoord + 0.5D, tileEntityDryingTable.yCoord + 0.5D, tileEntityDryingTable.zCoord + 0.5D) <= 64.0D;
        }

        /**
         * Called to transfer a stack from one inventory to the other eg. when shift clicking.
         */
        @Override
        public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
        {
            ItemStack var2 = null;
            Slot var3 = (Slot) this.inventorySlots.get(par2);

            if (var3 != null && var3.getHasStack())
            {
                ItemStack var4 = var3.getStack();
                var2 = var4.copy();

                if (par2 < 10)
                {
                    if (!this.mergeItemStack(var4, 10, 46, false))
                    {
                        return null;
                    }

                    var3.onSlotChange(var4, var2);
                }
                else if (par2 >= 10 && par2 < 37)
                {
                    if (!this.mergeItemStack(var4, 37, 46, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 37 && par2 < 46)
                {
                    if (!this.mergeItemStack(var4, 10, 37, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(var4, 10, 37, false))
                {
                    return null;
                }

                if (var4.stackSize == 0)
                {
                    var3.putStack((ItemStack) null);
                }
                else
                {
                    var3.onSlotChanged();
                }

                if (var4.stackSize == var2.stackSize)
                {
                    return null;
                }

                var3.onPickupFromSlot(par1EntityPlayer, var2);
            }

            return var2;
        }
    }
}
