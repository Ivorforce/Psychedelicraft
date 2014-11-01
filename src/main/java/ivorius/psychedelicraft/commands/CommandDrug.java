/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.commands;

import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

import java.util.List;

public class CommandDrug extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "drug";
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.drug.usage";
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] arguments)
    {
        if (arguments.length < 3)
            throw new WrongUsageException(getCommandUsage(par1ICommandSender));

        EntityPlayer player = getPlayer(par1ICommandSender, arguments[0]);

        String drugName = arguments[1];

        DrugHelper drugHelper = DrugHelper.getDrugHelper(player);
        if (drugHelper == null)
            return;

        if (drugHelper.doesDrugExist(drugName))
        {
            String method = arguments[2];
            int lock;
            if (method.equalsIgnoreCase("set"))
                lock = 0;
            else if (method.equalsIgnoreCase("lock"))
                lock = 1;
            else if (method.equalsIgnoreCase("unlock"))
                lock = -1;
            else
                throw new WrongUsageException(getCommandUsage(par1ICommandSender));

            if (lock != 0)
                drugHelper.getDrug(drugName).setLocked(lock == 1);

            if (arguments.length >= 4)
            {
                double amount = parseDouble(par1ICommandSender, arguments[3]);
                drugHelper.setDrugValue(drugName, amount);

                if (lock == 0)
                    par1ICommandSender.addChatMessage(new ChatComponentTranslation("commands.drug.success", player.getCommandSenderName(), drugName, String.valueOf(amount)));
                else if (lock == 1)
                    par1ICommandSender.addChatMessage(new ChatComponentTranslation("commands.drug.success.lock", player.getCommandSenderName(), drugName, String.valueOf(amount)));
                else if (lock == 2)
                    par1ICommandSender.addChatMessage(new ChatComponentTranslation("commands.drug.success.unlock", player.getCommandSenderName(), drugName, String.valueOf(amount)));
            }
            else if (lock == 0)
                throw new CommandException(getCommandUsage(par1ICommandSender), drugName); // Didn't actually do anything
            else if (lock == 1)
                par1ICommandSender.addChatMessage(new ChatComponentTranslation("commands.drug.success.lock", player.getCommandSenderName(), drugName, drugHelper.getDrugValue(drugName)));
            else if (lock == 2)
                par1ICommandSender.addChatMessage(new ChatComponentTranslation("commands.drug.success.unlock", player.getCommandSenderName(), drugName, drugHelper.getDrugValue(drugName)));
        }
        else
        {
            throw new CommandException("commands.drug.nodrug", drugName);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] arguments)
    {
        if (arguments.length == 1)
            return getListOfStringsMatchingLastWord(arguments, getPlayers());
        else if (arguments.length == 2)
        {
            try
            {
                DrugHelper drugHelper = DrugHelper.getDrugHelper(getPlayer(par1ICommandSender, arguments[0]));
                if (drugHelper != null)
                    return getListOfStringsMatchingLastWord(arguments, drugHelper.getAllVisibleDrugNames());
                else
                    return null;
            }
            catch (CommandException ex)
            {
                return null;
            }
        }
        else if (arguments.length == 3)
            return getListOfStringsMatchingLastWord(arguments, "set", "lock", "unlock");
        else if (arguments.length == 4)
            return getListOfStringsMatchingLastWord(arguments, "0.0", "0.5", "1.0");

        return null;
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }
}
