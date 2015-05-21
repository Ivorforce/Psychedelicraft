/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraftcore.PsycheCoreBusCommon;

/**
 * Created by lukas on 04.03.14.
 */
public class PSCoreHandlerCommon
{
    public void register()
    {
        PsycheCoreBusCommon.EVENT_BUS.register(this);
    }
}
