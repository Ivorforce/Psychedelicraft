/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class SetPlayerAnglesEvent extends Event
{
    public final float partialTicks;

    public SetPlayerAnglesEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }
}
