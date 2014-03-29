package net.ivorius.psychedelicraft.blocks;

import net.minecraft.world.World;

public interface IvBonemealCompatibleBlock
{
    public void growStep(World par1World, int x, int y, int z, boolean bonemeal);

    public boolean canGrow(World par1World, int x, int y, int z);
}