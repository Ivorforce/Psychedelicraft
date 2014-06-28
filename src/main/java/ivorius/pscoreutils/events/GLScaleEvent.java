/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 21.03.14.
 */
public class GLScaleEvent extends Event
{
    public final float x;
    public final float y;
    public final float z;

    public GLScaleEvent(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
