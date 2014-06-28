/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class ItemLightingEvent extends Event
{
    public ItemLightingEvent()
    {

    }

    public static class Enable extends ItemLightingEvent
    {
        public Enable()
        {
            super();
        }
    }

    public static class Disable extends ItemLightingEvent
    {
        public Disable()
        {
            super();
        }
    }
}
