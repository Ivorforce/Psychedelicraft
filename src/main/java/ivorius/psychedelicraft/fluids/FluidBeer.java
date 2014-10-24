/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.fluids;

import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lukas on 22.10.14.
 */
public class FluidBeer extends FluidDrug implements FluidFermentable
{
    public FluidBeer(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public List<DrugInfluence> getDrugInfluences(FluidStack fluidStack)
    {
        return Arrays.asList(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.25f));
    }

    @Override
    public void updateFermenting(FluidStack stack)
    {

    }
}
