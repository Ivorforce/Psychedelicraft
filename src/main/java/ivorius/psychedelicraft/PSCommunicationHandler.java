/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import cpw.mods.fml.common.event.FMLInterModComms;
import ivorius.ivtoolkit.tools.IvFMLIntercommHandler;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Logger;

/**
 * Created by Lukas Tenbrink on 17.03.14.
 * <p/>
 * Receives an IMCMessage. Most messages use the keys 'worldID' for the dimension ID and 'entityID' for the entity ID, if drug values are involved.
 * Also note that messages that expect a return value set the value as their appropriate tag, and otherwise send back the whole NBTTagCompound to the sender, with the key set as 'replyKey'.
 * Messages are read at Client / Server tick END Forge event.
 * If you need any more messages added, feel free to contact me about it.
 */
public class PSCommunicationHandler extends IvFMLIntercommHandler
{
    protected PSCommunicationHandler(Logger logger, String modOwnerID, Object modInstance)
    {
        super(logger, modOwnerID, modInstance);
    }

    @Override
    protected boolean handleMessage(FMLInterModComms.IMCMessage message, boolean server, boolean runtime)
    {
        if (isMessage("drugAddValue", message, NBTTagCompound.class))
        {
            NBTTagCompound cmp = message.getNBTValue();
            getDrugHelper(cmp, server).addToDrug(cmp.getString("drugName"), cmp.getFloat("drugValue"));
            return true;
        }
        else if (isMessage("drugAddInfluence", message, NBTTagCompound.class))
        {
            NBTTagCompound cmp = message.getNBTValue();
            DrugInfluence influence = new DrugInfluence(cmp.getString("drugName"), cmp.getInteger("drugInfluenceDelay"), cmp.getDouble("drugInfluenceSpeed"), cmp.getDouble("drugInfluenceSpeedAdd"), cmp.getDouble("drugTotalEffect"));
            getDrugHelper(cmp, server).addToDrug(influence);
            return true;
        }
        else if (isMessage("drugSetValue", message, NBTTagCompound.class))
        {
            NBTTagCompound cmp = message.getNBTValue();
            getDrugHelper(cmp, server).setDrugValue(cmp.getString("drugName"), cmp.getFloat("drugValue"));
            return true;
        }
        else if (isMessage("drugSetLocked", message, NBTTagCompound.class))
        {
            NBTTagCompound cmp = message.getNBTValue();
            getDrugHelper(cmp, server).getDrug(cmp.getString("drugName")).setLocked(cmp.getBoolean("drugLocked"));
            return true;
        }
        else if (isMessage("drugGetValue", message, NBTTagCompound.class))
        {
            NBTTagCompound cmp = message.getNBTValue();
            NBTTagCompound response = (NBTTagCompound) cmp.copy();
            response.setFloat("drugValue", getDrugHelper(cmp, server).getDrugValue(cmp.getString("drugName")));
            sendReply(message, response);
            return true;
        }
        else if (isMessage("drugIsLocked", message, NBTTagCompound.class))
        {
            NBTTagCompound cmp = message.getNBTValue();
            NBTTagCompound response = (NBTTagCompound) cmp.copy();
            response.setBoolean("drugLocked", getDrugHelper(cmp, server).getDrug(cmp.getString("drugName")).isLocked());
            sendReply(message, response);
            return true;
        }

        return false;
    }

    private DrugHelper getDrugHelper(NBTTagCompound compound, boolean server)
    {
        return DrugHelper.getDrugHelper(getEntity(compound, server));
    }
}
