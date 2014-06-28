/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
@Cancelable
public class SetupCameraTransformEvent extends Event
{
    public final float partialTicks;

    public SetupCameraTransformEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }
}
