/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class RenderWorldEvent extends Event
{
    public final float partialTicks;

    public RenderWorldEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public static class Pre extends RenderWorldEvent
    {
        public Pre(float partialTicks)
        {
            super(partialTicks);
        }
    }

    public static class Post extends RenderWorldEvent
    {
        public Post(float partialTicks)
        {
            super(partialTicks);
        }
    }
}
