/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class LightmapSwitchEvent extends Event
{
    public final boolean enable;

    public LightmapSwitchEvent(boolean enable)
    {
        this.enable = enable;
    }
}
