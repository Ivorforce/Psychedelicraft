/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import ivorius.psychedelicraft.events.ItemDriedEvent;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;

public class SlotDryingTableResult extends Slot
{
    private EntityPlayer thePlayer;
    private int field_75228_b;

    public SlotDryingTableResult(EntityPlayer par1EntityPlayer, IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.thePlayer = par1EntityPlayer;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int par1)
    {
        if (this.getHasStack())
        {
            this.field_75228_b += Math.min(par1, this.getStack().stackSize);
        }

        return super.decrStackSize(par1);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        this.onCrafting(par2ItemStack);
        super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
    }

    @Override
    protected void onCrafting(ItemStack par1ItemStack, int par2)
    {
        this.field_75228_b += par2;
        this.onCrafting(par1ItemStack);
    }

    @Override
    protected void onCrafting(ItemStack par1ItemStack)
    {
        par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
        MinecraftForge.EVENT_BUS.post(new ItemDriedEvent(thePlayer, par1ItemStack));

        if (!this.thePlayer.worldObj.isRemote)
        {
            int var2 = this.field_75228_b;
            float var3 = 5;
            int var4;

            if (var3 == 0.0F)
            {
                var2 = 0;
            }
            else if (var3 < 1.0F)
            {
                var4 = MathHelper.floor_float(var2 * var3);

                if (var4 < MathHelper.ceiling_float_int(var2 * var3) && (float) Math.random() < var2 * var3 - var4)
                {
                    ++var4;
                }

                var2 = var4;
            }

            while (var2 > 0)
            {
                var4 = EntityXPOrb.getXPSplit(var2);
                var2 -= var4;
                this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, var4));
            }
        }

        this.field_75228_b = 0;
    }
}
