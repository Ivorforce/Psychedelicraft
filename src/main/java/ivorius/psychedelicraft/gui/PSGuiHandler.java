/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import ivorius.psychedelicraft.blocks.TileEntityDryingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by lukas on 17.02.14.
 */
public class PSGuiHandler implements IGuiHandler
{
    public static final int dryingTableContainerID = 0;

    public static final int fluidHandlerContainerID_DOWN = 1;
    public static final int fluidHandlerContainerID_UP = 2;
    public static final int fluidHandlerContainerID_NORTH = 3;
    public static final int fluidHandlerContainerID_SOUTH = 4;
    public static final int fluidHandlerContainerID_WEST = 5;
    public static final int fluidHandlerContainerID_EAST = 6;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        if (id == dryingTableContainerID)
            return new ContainerDryingTable(player.inventory, world, (TileEntityDryingTable) world.getTileEntity(x, y, z));
        else if (id >= fluidHandlerContainerID_UP && id <= fluidHandlerContainerID_SOUTH)
        {
            ForgeDirection forgeDirection = ForgeDirection.getOrientation(id - 1);

            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof IFluidHandler)
                return new ContainerFluidHandler(player.inventory, tileEntity, (IFluidHandler) tileEntity, forgeDirection);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        if (id == dryingTableContainerID)
            return new GuiDryingTable(player.inventory, world, (TileEntityDryingTable) world.getTileEntity(x, y, z));
        else if (id >= fluidHandlerContainerID_UP && id <= fluidHandlerContainerID_SOUTH)
        {
            ForgeDirection forgeDirection = ForgeDirection.getOrientation(id - 1);

            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof IFluidHandler)
                return new GuiFluidHandler(player.inventory, tileEntity, (IFluidHandler) tileEntity, forgeDirection);
        }

        return null;
    }
}
