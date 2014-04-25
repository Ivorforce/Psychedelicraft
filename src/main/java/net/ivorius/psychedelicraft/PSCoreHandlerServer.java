package net.ivorius.psychedelicraft;

import net.ivorius.psychedelicraftcore.PsycheCoreBusCommon;

/**
 * Created by lukas on 04.03.14.
 */
public class PSCoreHandlerServer
{
    public void register()
    {
        PsycheCoreBusCommon.EVENT_BUS.register(this);
    }
}
