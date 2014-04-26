package net.ivorius.psychedelicraftcoreUtils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class RenderHeldItemEvent extends Event
{
    public final float partialTicks;

    public RenderHeldItemEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }
}
