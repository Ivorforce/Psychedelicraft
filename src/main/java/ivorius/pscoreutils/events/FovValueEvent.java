/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class FovValueEvent extends Event
{
    public final float fov;
    public final boolean worldFOV;

    public FovValueEvent(float fov, boolean worldFOV)
    {
        this.fov = fov;
        this.worldFOV = worldFOV;
    }
}
