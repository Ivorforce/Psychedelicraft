/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class GLActiveTextureEvent extends Event
{
    public final int texture;

    public GLActiveTextureEvent(int texture)
    {
        this.texture = texture;
    }
}
