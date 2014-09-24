/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collections;
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
//		return "/drug <optional: player> <drug> <value: 0.0 - 1.0> <optional:lock/unlock> -- Adds the specified amount to the drug\nLock: Locks the value at the amount specified\nUnlock: Unlocks the drug, and sets the value to amount specified";
        return "/drug <drug> <value: 0.0 - 1.0> <optional:lock/unlock> -- Adds the specified amount to the drug\nLock: Locks the value at the amount specified\nUnlock: Unlocks the drug, and sets the value to amount specified";
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 2)
        {
            EntityPlayer var3 = getPlayer(par1ICommandSender, par2ArrayOfStr[0]);
            int startIndex = 0;

            if (var3 != null && par2ArrayOfStr.length < 3)
            {
                throw new WrongUsageException(getCommandUsage(par1ICommandSender));
            }

            if (var3 == null)
            {
                var3 = getCommandSenderAsPlayer(par1ICommandSender);
            }
            else
            {
                startIndex++;
            }

            if (DrugHelper.getDrugHelper(var3).doesDrugExist(par2ArrayOfStr[startIndex]))
            {
                float amount = 0;
                String drugName = par2ArrayOfStr[startIndex];

                try
                {
                    amount = Float.valueOf(par2ArrayOfStr[startIndex + 1]);
                }
                catch (Exception e)
                {
                    throw new WrongUsageException(getCommandUsage(par1ICommandSender));
                }

                boolean lock = DrugHelper.getDrugHelper(var3).getDrug(drugName).isLocked();
                if (par2ArrayOfStr.length > startIndex + 2)
                {
                    lock = par2ArrayOfStr[startIndex + 2].equals("lock");
                }

                DrugHelper.getDrugHelper(var3).getDrug(drugName).setLocked(false);

                if (lock)
                {
                    DrugHelper.getDrugHelper(var3).setDrugValue(drugName, amount);
                }
                else
                {
                    DrugHelper.getDrugHelper(var3).addToDrug(drugName, amount);
                }

                DrugHelper.getDrugHelper(var3).getDrug(drugName).setLocked(lock);
            }
        }
        else
        {
            throw new WrongUsageException(getCommandUsage(par1ICommandSender));
        }
    }

//    public void processCommandClient(EntityPlayer player, String[] par2ArrayOfStr)
//    {
//        if (par2ArrayOfStr.length >= 2)
//        {
////			EntityPlayer var3 = this.getTargetPlayer(par2ArrayOfStr[0]);
//            EntityPlayer var3 = null;
//            int startIndex = 0;
//
//            if (var3 == null)
//                var3 = player;
//            else
//                startIndex++;
//
//            if (DrugHelper.getDrugHelper(var3).doesDrugExist(par2ArrayOfStr[startIndex]))
//            {
//                float amount = 0;
//
//                try
//                {
//                    amount = Float.valueOf(par2ArrayOfStr[startIndex + 1]);
//                }
//                catch (Exception e)
//                {
//
//                }
//
//                if (par2ArrayOfStr.length > startIndex + 2)
//                {
//                    Drug drug = DrugHelper.getDrugHelper(var3).getDrug(par2ArrayOfStr[startIndex]);
//
//                    drug.setLocked(par2ArrayOfStr[startIndex + 2].equals("lock"));
//                    DrugHelper.getDrugHelper(var3).setDrugValue(par2ArrayOfStr[startIndex], amount);
//                }
//                else
//                {
//                    DrugHelper.getDrugHelper(var3).addToDrug(par2ArrayOfStr[startIndex], amount);
//                }
//            }
//        }
//        else
//        {
//
//        }
//    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
//		if (par2ArrayOfStr.length == 1)
//			return getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getPlayers());

//		EntityPlayer var3 = this.getTargetPlayer(par2ArrayOfStr[0]);
//		int startIndex = 0;
//
//		if (var3 == null)
//			var3 = getCommandSenderAsPlayer(par1ICommandSender);
//		else
//			startIndex++;

        int startIndex = 0;
        EntityPlayer var3 = getCommandSenderAsPlayer(par1ICommandSender);

        if (par2ArrayOfStr.length == startIndex + 1)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, DrugHelper.getDrugHelper(var3).getAllVisibleDrugNames());
        }
        if (par2ArrayOfStr.length == startIndex + 2)
        {
            String[] values = new String[]{"1.0", "0.5", "-0.5", "-1.0"};
            ArrayList list = new ArrayList<String>();
            Collections.addAll(list, values);

            return list;
        }
        if (par2ArrayOfStr.length == startIndex + 3)
        {
            String[] values = new String[]{"lock", "unlock"};
            ArrayList list = new ArrayList<String>();
            Collections.addAll(list, values);

            return list;
        }

        return null;
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }
}
