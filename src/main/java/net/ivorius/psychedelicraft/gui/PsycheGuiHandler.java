/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.blocks.TileEntityDryingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by lukas on 17.02.14.
 */
public class PsycheGuiHandler implements IGuiHandler
{
    private int currentContainerID = 0;

    public int acquireUniqueContainerID()
    {
        currentContainerID++;

        return currentContainerID - 1;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == Psychedelicraft.dryingTableContainerID)
        {
            return new GuiDryingTable.ContainerDryingTable(player.inventory, world, (TileEntityDryingTable) world.getTileEntity(x, y, z));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == Psychedelicraft.dryingTableContainerID)
        {
            return new GuiDryingTable(player.inventory, world, (TileEntityDryingTable) world.getTileEntity(x, y, z));
        }

        return null;
    }
}
