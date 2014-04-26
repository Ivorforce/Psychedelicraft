package net.ivorius.psychedelicraft.entities;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.ivToolkit.IvNBTPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by lukas on 25.02.14.
 */
public class DrugInfoPacket extends IvNBTPacket
{
    public DrugInfoPacket()
    {

    }

    public DrugInfoPacket(NBTTagCompound compound)
    {
        super(compound);
    }

    public static void sendDrugDataToAllClients(EntityPlayerMP entity)
    {
        Psychedelicraft.packetPipeline.sendTo(createPacketFromEntity(entity), entity);
    }

    public static DrugInfoPacket createPacketFromEntity(Entity entity)
    {
        DrugHelper drugHelper = DrugHelper.getDrugHelper(entity);

        if (drugHelper != null)
        {
            return new DrugInfoPacket(drugHelper.createNBTTagCompound());
        }

        return null;
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        DrugHelper.getDrugHelper(player).readFromNBT(compound, false);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        DrugHelper.getDrugHelper(player).readFromNBT(compound, false);
    }
}
