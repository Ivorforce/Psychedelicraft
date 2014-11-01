/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs;

import ivorius.ivtoolkit.logic.IvChatBot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public abstract class DrugHallucination
{
    public EntityPlayer playerEntity;

    public int entityTicksAlive;

    public IvChatBot chatBot;

    public DrugHallucination(EntityPlayer playerEntity)
    {
        this.playerEntity = playerEntity;
    }

    public void update()
    {
        entityTicksAlive++;

        if (this.chatBot != null)
        {
            String sendString = this.chatBot.update();

            if (sendString != null)
            {
                playerEntity.addChatMessage(new ChatComponentText(sendString));
            }
        }
    }

    public void receiveChatMessage(String message, EntityLivingBase entity)
    {
        if (this.chatBot != null)
        {
            this.chatBot.receiveChatMessage(message);
        }
    }

    public abstract void render(float par1, float dAlpha);

    public abstract boolean isDead();

    public abstract int getMaxHallucinations();
}
