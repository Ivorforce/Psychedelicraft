/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

import io.netty.buffer.ByteBuf;

/**
 * A interface for Entities that need extra information to be communicated
 * between the server and client when their values are updated.
 */
public interface IEntityUpdateData
{
    /**
     * Called by the server when constructing the update packet.
     * Data should be added to the provided stream.
     *
     * @param buffer The packet data stream
     */
    public void writeUpdateData(ByteBuf buffer, String context);

    /**
     * Called by the client when it receives a Entity update packet.
     * Data should be read out of the stream in the same way as it was written.
     *
     * @param buffer The packet data stream
     */
    public void readUpdateData(ByteBuf buffer, String context);
}
