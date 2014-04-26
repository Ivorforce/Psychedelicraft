/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import net.minecraft.world.World;

public interface IvBonemealCompatibleBlock
{
    public void growStep(World par1World, int x, int y, int z, boolean bonemeal);

    public boolean canGrow(World par1World, int x, int y, int z);
}