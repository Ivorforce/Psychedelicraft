/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import ivorius.psychedelicraft.blocks.TileEntityDryingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by lukas on 17.02.14.
 */
public class PSGuiHandler implements IGuiHandler
{
    public static final int dryingTableContainerID = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == dryingTableContainerID)
        {
            return new GuiDryingTable.ContainerDryingTable(player.inventory, world, (TileEntityDryingTable) world.getTileEntity(x, y, z));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == dryingTableContainerID)
        {
            return new GuiDryingTable(player.inventory, world, (TileEntityDryingTable) world.getTileEntity(x, y, z));
        }

        return null;
    }
}
