/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class GLBlendFuncEvent extends Event
{
    public final int sFactor;
    public final int dFactor;
    public final int sfactorAlpha;
    public final int dfactorAlpha;

    public GLBlendFuncEvent(int sFactor, int dFactor, int sfactorAlpha, int dfactorAlpha)
    {
        this.sFactor = sFactor;
        this.dFactor = dFactor;
        this.sfactorAlpha = sfactorAlpha;
        this.dfactorAlpha = dfactorAlpha;
    }
}
