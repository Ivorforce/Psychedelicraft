/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.pscoreutils.events;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by lukas on 13.03.14.
 */
public class WakeUpPlayerEvent extends Event
{
    public final EntityPlayer player;

    public final boolean unsuccessful;
    public final boolean updateAllPlayersSleepingFlag;
    public final boolean setSpawnChunk;

    public WakeUpPlayerEvent(EntityPlayer player, boolean unsuccessful, boolean updateAllPlayersSleepingFlag, boolean setSpawnChunk)
    {
        this.player = player;
        this.unsuccessful = unsuccessful;
        this.updateAllPlayersSleepingFlag = updateAllPlayersSleepingFlag;
        this.setSpawnChunk = setSpawnChunk;
    }
}
