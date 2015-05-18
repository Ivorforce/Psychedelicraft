package ivorius.psychedelicraft.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by lukas on 18.05.15.
 */
public class FluidDrinkEvent extends LivingEvent
{
    public final FluidStack drunken;

    public FluidDrinkEvent(EntityLivingBase entity, FluidStack drunken)
    {
        super(entity);
        this.drunken = drunken;
    }
}
