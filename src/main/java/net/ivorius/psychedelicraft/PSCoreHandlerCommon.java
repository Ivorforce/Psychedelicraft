package net.ivorius.psychedelicraft;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraftcore.PsycheCoreBusCommon;
import net.ivorius.psychedelicraftcore.events.WakeUpPlayerEvent;

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
            DrugHelper drugHelper = DrugHelper.getDrugHelper(event.player);

            if (drugHelper != null)
            {
                drugHelper.wakeUp(event.player);
            }
        }
    }
}
