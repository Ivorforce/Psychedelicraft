/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import io.netty.buffer.ByteBuf;
import ivorius.ivtoolkit.blocks.IvTileEntityHelper;
import ivorius.ivtoolkit.network.IvNetworkHelperServer;
import ivorius.ivtoolkit.network.PartialUpdateHandler;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.config.PSConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class TileEntityDryingTable extends TileEntity implements ISidedInventory, PartialUpdateHandler
{
    public float heatRatio;
    public float dryingProgress;
    public ItemStack plannedResult;

    public ItemStack[] dryingTableItems = new ItemStack[10];

    public int ticksAlive;

    public static Map<Item, ItemStack> dryingList = new HashMap<>();

    public static void addDryingResult(Item itemID, ItemStack result)
    {
        dryingList.put(itemID, result);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        ticksAlive++;

        if (ticksAlive % 30 == 5 && !worldObj.isRemote)
        {
            calculateHeatRatio();

            if (worldObj.getRainStrength(1.0f) > 0.0f && worldObj.getPrecipitationHeight(xCoord, yCoord) == yCoord + 1)
            {
                dryingProgress = 0;
            }

            IvNetworkHelperServer.sendTileEntityUpdatePacket(this, "dryingProgress", Psychedelicraft.network);
        }

        if (plannedResult != null && (dryingTableItems[0] == null || (dryingTableItems[0] == plannedResult && dryingTableItems[0].getItemDamage() == plannedResult.getItemDamage() && plannedResult.isStackable() && plannedResult.stackSize + plannedResult.stackSize < plannedResult.getMaxStackSize())))
        {
            dryingProgress += heatRatio / (float)PSConfig.dryingTableTickDuration;

            if (dryingProgress >= 1.0f && !worldObj.isRemote)
            {
                dryingProgress = 0;

                for (int i = 1; i < 10; i++)
                {
                    dryingTableItems[i] = null;
                }

                if (dryingTableItems[0] == null)
                {
                    dryingTableItems[0] = plannedResult;
                }
                else
                {
                    dryingTableItems[0].stackSize += plannedResult.stackSize;
                }

                onInventoryChanged();
            }
        }
        else
        {
            dryingProgress = 0;
        }
    }

    public ItemStack getResult()
    {
        if (dryingTableItems[1] == null)
        {
            return null;
        }

        Item id = dryingTableItems[1].getItem();
        int damage = dryingTableItems[1].getItemDamage();

        for (int i = 2; i < dryingTableItems.length; i++)
        {
            if (dryingTableItems[i] == null || dryingTableItems[i].getItem() != id || dryingTableItems[i].getItemDamage() != damage)
            {
                return null;
            }
        }

        if (dryingList.containsKey(id))
        {
            return (dryingList.get(id)).copy();
        }

        return null;
    }

    public void calculateHeatRatio()
    {
        float l = worldObj.getBlockLightValue(xCoord, yCoord + 1, zCoord) / 15f;
        float h = 0;

        if (worldObj.blockExists(xCoord, yCoord, zCoord))
        {
            Chunk var48 = worldObj.getChunkFromBlockCoords(xCoord, zCoord);
            h = var48.getBiomeGenForWorldCoords(xCoord & 15, zCoord & 15, worldObj.getWorldChunkManager()).getFloatTemperature(xCoord, yCoord, zCoord) * 0.75F + 0.25F;
        }

        float t = (l * l * h) * (l * l * h);
        if (t > 1.0f)
        {
            t = 1.0f;
        }
        if (t < 0.0f)
        {
            t = 0.0f;
        }

        heatRatio = t;

        if (!worldObj.isRemote)
            IvNetworkHelperServer.sendTileEntityUpdatePacket(this, "heatRatio", Psychedelicraft.network);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.dryingTableItems.length; ++var3)
        {
            if (this.dryingTableItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.dryingTableItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        if (plannedResult != null)
        {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte) 20);
            plannedResult.writeToNBT(var4);
            var2.appendTag(var4);
        }

        par1NBTTagCompound.setTag("Items", var2);

        par1NBTTagCompound.setFloat("heatRatio", heatRatio);
        par1NBTTagCompound.setFloat("dryingProgress", dryingProgress);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        NBTTagList var2 = par1NBTTagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        this.dryingTableItems = new ItemStack[this.dryingTableItems.length];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 == 20)
            {
                this.plannedResult = ItemStack.loadItemStackFromNBT(var4);
            }
            else if (var5 >= 0 && var5 < this.dryingTableItems.length)
            {
                this.dryingTableItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        heatRatio = par1NBTTagCompound.getFloat("heatRatio");
        dryingProgress = par1NBTTagCompound.getFloat("dryingProgress");
    }

    @Override
    public int getSizeInventory()
    {
        return this.dryingTableItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? null : this.dryingTableItems[par1];
    }

    @Override
    public String getInventoryName()
    {
        return "container.dryingTable";
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.dryingTableItems[par1] != null)
        {
            ItemStack var3;

            if (this.dryingTableItems[par1].stackSize <= par2)
            {
                var3 = this.dryingTableItems[par1];
                this.dryingTableItems[par1] = null;
                onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.dryingTableItems[par1].splitStack(par2);

                if (this.dryingTableItems[par1].stackSize == 0)
                {
                    this.dryingTableItems[par1] = null;
                }

                onInventoryChanged();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.dryingTableItems[par1] = par2ItemStack;
        onInventoryChanged();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    public void onInventoryChanged()
    {
        plannedResult = getResult();

        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2)
    {
        return dryingTableItems[var1] == null;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return IvTileEntityHelper.getStandardDescriptionPacket(this);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return var1 == 0 ? new int[]{0} : new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override
    public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return this.isItemValidForSlot(par1, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3)
    {
        return true;
    }

    @Override
    public void writeUpdateData(ByteBuf buffer, String context, Object... params)
    {
        if ("heatRatio".equals(context))
        {
            buffer.writeFloat(heatRatio);
        }
        else if ("dryingProgress".equals(context))
        {
            buffer.writeFloat(dryingProgress);
        }
    }

    @Override
    public void readUpdateData(ByteBuf buffer, String context)
    {
        if ("heatRatio".equals(context))
        {
            heatRatio = buffer.readFloat();
        }
        else if ("dryingProgress".equals(context))
        {
            dryingProgress = buffer.readFloat();
        }
    }
}
