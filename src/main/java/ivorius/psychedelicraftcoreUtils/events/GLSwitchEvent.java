/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcoreUtils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class GLSwitchEvent extends Event
{
    public final int cap;

    public GLSwitchEvent(int cap)
    {
        this.cap = cap;
    }

    public static class Enable extends GLSwitchEvent
    {
        public Enable(int cap)
        {
            super(cap);
        }
    }

    public static class Disable extends GLSwitchEvent
    {
        public Disable(int cap)
        {
            super(cap);
        }
    }
}
