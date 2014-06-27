/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.ivtoolkit.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

/**
 * Created by lukas on 24.02.14.
 */
public abstract class IvNBTPacket extends IvPacket
{
    public NBTTagCompound compound;

    public IvNBTPacket()
    {

    }

    public IvNBTPacket(NBTTagCompound compound)
    {
        this.compound = compound;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        try
        {
            CompressedStreamTools.write(compound, new ByteBufOutputStream(buffer));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        try
        {
            compound = CompressedStreamTools.read(new ByteBufInputStream(buffer));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
