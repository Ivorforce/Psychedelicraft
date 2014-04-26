/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcoreUtils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class LightmapSwitchEvent extends Event
{
    public LightmapSwitchEvent()
    {

    }

    public static class Enable extends LightmapSwitchEvent
    {
        public Enable()
        {
            super();
        }
    }

    public static class Disable extends LightmapSwitchEvent
    {
        public Disable()
        {
            super();
        }
    }
}
