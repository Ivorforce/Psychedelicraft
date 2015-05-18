package ivorius.psychedelicraft.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by lukas on 18.05.15.
 */
public class ItemDriedEvent extends PlayerEvent
{
    public final ItemStack dried;

    public ItemDriedEvent(EntityPlayer player, ItemStack dried)
    {
        super(player);
        this.dried = dried;
    }
}
