/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import cpw.mods.fml.common.event.FMLInterModComms;
import ivorius.psychedelicraft.entities.DrugHelper;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by Lukas Tenbrink on 17.03.14.
 * <p/>
 * Receives an IMCMessage. Most messages use the keys 'worldID' for the dimension ID and 'entityID' for the entity ID, if drug values are involved.
 * Also note that messages that expect a return value set the value as their appropriate tag, and otherwise send back the whole NBTTagCompound to the sender, with the key set as 'replyKey'.
 * Messages are read at Client / Server tick END Forge event.
 * If you need any more messages added, feel free to contact me about it.
 */
public class PSCommunicationHandler
{
    public void onIMCMessage(FMLInterModComms.IMCMessage message, boolean server, boolean runtime)
    {
        try
        {
            if (isMessage("drugAddValue", message, NBTTagCompound.class))
            {
                NBTTagCompound cmp = message.getNBTValue();
                getDrugHelper(cmp, server).addToDrug(cmp.getString("drugName"), cmp.getFloat("drugValue"));
            }
            else if (isMessage("drugAddInfluence", message, NBTTagCompound.class))
            {
                NBTTagCompound cmp = message.getNBTValue();
                DrugInfluence influence = new DrugInfluence(cmp.getString("drugName"), cmp.getInteger("drugInfluenceDelay"), cmp.getDouble("drugInfluenceSpeed"), cmp.getDouble("drugInfluenceSpeedAdd"), cmp.getDouble("drugTotalEffect"));
                getDrugHelper(cmp, server).addToDrug(influence);
            }
            else if (isMessage("drugSetValue", message, NBTTagCompound.class))
            {
                NBTTagCompound cmp = message.getNBTValue();
                getDrugHelper(cmp, server).setDrugValue(cmp.getString("drugName"), cmp.getFloat("drugValue"));
            }
            else if (isMessage("drugSetLocked", message, NBTTagCompound.class))
            {
                NBTTagCompound cmp = message.getNBTValue();
                getDrugHelper(cmp, server).getDrug(cmp.getString("drugName")).setLocked(cmp.getBoolean("drugLocked"));
            }
            else if (isMessage("drugGetValue", message, NBTTagCompound.class))
            {
                NBTTagCompound cmp = message.getNBTValue();
                NBTTagCompound response = (NBTTagCompound) cmp.copy();
                response.setFloat("drugValue", getDrugHelper(cmp, server).getDrugValue(cmp.getString("drugName")));
                sendReply(message, response);
            }
            else if (isMessage("drugIsLocked", message, NBTTagCompound.class))
            {
                NBTTagCompound cmp = message.getNBTValue();
                NBTTagCompound response = (NBTTagCompound) cmp.copy();
                response.setBoolean("drugLocked", getDrugHelper(cmp, server).getDrug(cmp.getString("drugName")).isLocked());
                sendReply(message, response);
            }
        }
        catch (Exception ex)
        {
            Psychedelicraft.logger.error("Message error! Exception on message with key '" + message.key + "' of type '" + message.getMessageType().getName() + "'");
            ex.printStackTrace();
        }
    }

    private boolean isMessage(String key, FMLInterModComms.IMCMessage message, Class expectedType)
    {
        if (key.equals(message.key))
        {
            if (message.getMessageType().isAssignableFrom(expectedType))
            {
                return true;
            }

            faultyMessage(message, expectedType);
        }

        return false;
    }

    private Entity getEntity(NBTTagCompound compound, boolean server)
    {
        return getEntity(compound, "worldID", "entityID", server);
    }

    private Entity getEntity(NBTTagCompound compound, String worldKey, String entityKey, boolean server)
    {
        if (!server)
        {
            return Minecraft.getMinecraft().theWorld.getEntityByID(compound.getInteger(entityKey));
        }
        else
        {
            return DimensionManager.getWorld(compound.getInteger(worldKey)).getEntityByID(compound.getInteger(entityKey));
        }
    }

    private DrugHelper getDrugHelper(NBTTagCompound compound, boolean server)
    {
        return DrugHelper.getDrugHelper(getEntity(compound, server));
    }

    private boolean sendReply(FMLInterModComms.IMCMessage message, String value)
    {
        if (message.getSender() == null)
        {
            return false;
        }

        NBTTagCompound cmp = message.getNBTValue();
        FMLInterModComms.sendRuntimeMessage(Psychedelicraft.MODID, message.getSender(), cmp.getString("replyKey"), value);
        return true;
    }

    private boolean sendReply(FMLInterModComms.IMCMessage message, NBTTagCompound value)
    {
        if (message.getSender() == null)
        {
            return false;
        }

        NBTTagCompound cmp = message.getNBTValue();
        FMLInterModComms.sendRuntimeMessage(Psychedelicraft.MODID, message.getSender(), cmp.getString("replyKey"), value);
        return true;
    }

    private boolean sendReply(FMLInterModComms.IMCMessage message, ItemStack value)
    {
        if (message.getSender() == null)
        {
            Psychedelicraft.logger.error("Message error! Could not reply to message with key '" + message.key + "' - No sender found");
            return false;
        }

        NBTTagCompound cmp = message.getNBTValue();
        FMLInterModComms.sendRuntimeMessage(Psychedelicraft.MODID, message.getSender(), cmp.getString("replyKey"), value);
        return true;
    }

    private void faultyMessage(FMLInterModComms.IMCMessage message, Class expectedType)
    {
        Psychedelicraft.logger.error("Message error! Got message with key '" + message.key + "' of type '" + message.getMessageType().getName() + "'; Expected type: '" + expectedType.getName() + "'");
    }
}
