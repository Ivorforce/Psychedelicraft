/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class GLSwitchEvent extends Event
{
    public final int cap;
    public final boolean enable;

    public GLSwitchEvent(int cap, boolean enable)
    {
        this.cap = cap;
        this.enable = enable;
    }
}
