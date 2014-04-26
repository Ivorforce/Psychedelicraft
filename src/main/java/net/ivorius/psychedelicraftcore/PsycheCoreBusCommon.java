package net.ivorius.psychedelicraftcore;

import cpw.mods.fml.common.eventhandler.EventBus;
import net.ivorius.psychedelicraftcoreUtils.events.WakeUpPlayerEvent;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by lukas on 21.02.14.
 */
public class PsycheCoreBusCommon
{
    public static final EventBus EVENT_BUS = new EventBus();

    public static void wakeUpPlayer(EntityPlayer player, boolean unsuccessful, boolean updateAllPlayersSleepingFlag, boolean setSpawnChunk)
    {
        EVENT_BUS.post(new WakeUpPlayerEvent(player, unsuccessful, updateAllPlayersSleepingFlag, setSpawnChunk));
    }
}
