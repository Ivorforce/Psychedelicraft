/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.ivToolkit;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;

/**
 * Created by lukas on 13.04.14.
 */
@ChannelHandler.Sharable
public class ChannelHandlerEntityData extends SimpleChannelInboundHandler<FMLProxyPacket>
{
    public static String packetChannel;

    /**
     * Sends an update packet for the entity to every watching player's client, if the current active side is the server.
     * Only use this method for complex updates; if you have a primitive to sync, use the entity's data watcher instead.
     *
     * @param entity  The entity, both extending Entity and implementing IEntityUpdateData.
     * @param context The update context for the packet. Will be passed in writeUpdateData and readUpdateData. Keep in mind you can also add your own additional context information to the buffer.
     */
    public static <UpdatableEntity extends Entity & IEntityUpdateData> void sendUpdatePacketSafe(UpdatableEntity entity, String context)
    {
        if (!entity.worldObj.isRemote && entity.worldObj instanceof WorldServer)
        {
            sendUpdatePacket(entity, context);
        }
    }

    /**
     * Sends an update packet for the entity to every watching player's client.
     * Only use this method for complex updates; if you have a primitive to sync, use the entity's data watcher instead.
     * Do not invoke this method directly, always use {@link #sendUpdatePacketSafe(net.minecraft.entity.Entity, String)} instead.
     *
     * @param entity  The entity, both extending Entity and implementing IEntityUpdateData.
     * @param context The update context for the packet. Will be passed in writeUpdateData and readUpdateData. Keep in mind you can also add your own additional context information to the buffer.
     */
    public static <UpdatableEntity extends Entity & IEntityUpdateData> void sendUpdatePacket(UpdatableEntity entity, String context)
    {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(entity.getEntityId());
        ByteBufUtils.writeUTF8String(buffer, context);
        entity.writeUpdateData(buffer, context);

        FMLProxyPacket packet = new FMLProxyPacket(buffer, packetChannel);
        ((WorldServer) entity.worldObj).getEntityTracker().func_151247_a(entity, packet);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception
    {
        ByteBuf buffer = msg.payload();
        int entityID = buffer.readInt();
        String context = ByteBufUtils.readUTF8String(buffer);

        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);

        if (entity instanceof IEntityUpdateData)
        {
            ((IEntityUpdateData) entity).readUpdateData(buffer, context);
        }
    }
}
