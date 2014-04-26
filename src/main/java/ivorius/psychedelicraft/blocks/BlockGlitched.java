/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by lukas on 06.03.14.
 */
public class BlockGlitched extends Block
{
    public BlockGlitched()
    {
        super(Material.circuits);

        setHardness(0.0f);
        setStepSound(soundTypeSand);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return null;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5Player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        par1World.setBlock(par2, par3, par4, Blocks.air);

        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        super.onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);

        if (!par1World.isRemote)
        {
            par1World.setBlock(par2, par3, par4, Blocks.air);
        }
    }

    @Override
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        super.onEntityWalking(par1World, par2, par3, par4, par5Entity);

        if (!par1World.isRemote)
        {
            par1World.setBlock(par2, par3, par4, Blocks.air);
        }
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess p_149677_1_, int p_149677_2_, int p_149677_3_, int p_149677_4_)
    {
        return 61680; // Full light
    }

    @Override
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        return true;
    }

    @Override
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
        return true;
    }
}
