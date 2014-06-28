/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 21.03.14.
 */
public class GLRotateEvent extends Event
{
    public final float angle;
    public final float x;
    public final float y;
    public final float z;

    public GLRotateEvent(float angle, float x, float y, float z)
    {
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
