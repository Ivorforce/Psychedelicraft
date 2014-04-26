package net.ivorius.psychedelicraftcoreUtils.events;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
@Cancelable
public class RenderBlockOverlayEvent extends Event
{
    public final float partialTicks;

    public RenderBlockOverlayEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }
}
