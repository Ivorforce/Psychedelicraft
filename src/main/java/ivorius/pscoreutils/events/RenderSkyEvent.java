package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 17.11.14.
 */
public class RenderSkyEvent extends Event
{
    public final float partialTicks;

    public RenderSkyEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public static class Pre extends RenderSkyEvent
    {
        public Pre(float partialTicks)
        {
            super(partialTicks);
        }
    }
}
