/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import ivorius.psychedelicraft.entities.EntityRealityRift;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CommandPsyche extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "psyche";
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/psyche <command> -- Performs a special Psychedelicraft-related command. Press tab for options.";
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1)
        {
            if ("spawnRift".equals(par2ArrayOfStr[0]))
            {
                EntityPlayer player = getCommandSenderAsPlayer(par1ICommandSender);

                if (player == null)
                {
                    throw new WrongUsageException(getCommandUsage(par1ICommandSender), new Object[0]);
                }
                else
                {
                    EntityRealityRift rift = new EntityRealityRift(par1ICommandSender.getEntityWorld());
                    Vec3 look = player.getLookVec();

                    rift.setPosition(player.posX + look.xCoord * 3.0f, player.posY + look.yCoord * 3.0f, player.posZ + look.zCoord * 3.0f);
                    par1ICommandSender.getEntityWorld().spawnEntityInWorld(rift);
                }
            }
        }
        else
        {
            throw new WrongUsageException(getCommandUsage(par1ICommandSender), new Object[0]);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 1)
        {
            String[] values = new String[]{"spawnRift"};

            ArrayList list = new ArrayList<String>();
            for (String s : values)
            {
                list.add(s);
            }

            return list;
        }

        return null;
    }
}
