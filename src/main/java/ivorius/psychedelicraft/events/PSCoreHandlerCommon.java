/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ivorius.pscoreutils.events.WakeUpPlayerEvent;
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

    @SubscribeEvent
    public void wakeUpPlayer(WakeUpPlayerEvent event)
    {
        if (!event.unsuccessful)
        {
            DrugProperties drugProperties = DrugProperties.getDrugProperties(event.player);

            if (drugProperties != null)
            {
                drugProperties.wakeUp(event.player);
            }
        }
    }
}
