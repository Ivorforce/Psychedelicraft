/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.ivToolkit;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * Created by lukas on 11.02.14.
 */
public class IvBlockCollection
{
    private Block[] blocks;
    private byte[] metas;
    public int width;
    public int height;
    public int length;

    public IvBlockCollection(int width, int height, int length)
    {
        this(new Block[width * height * length], new byte[width * height * length], width, height, length);
    }

    public IvBlockCollection(Block[] blocks, byte[] metas, int width, int height, int length)
    {
        if (blocks.length != width * height * length || metas.length != blocks.length)
        {
            throw new IllegalArgumentException();
        }

        this.blocks = blocks;
        this.metas = metas;
        this.width = width;
        this.height = height;
        this.length = length;
    }

    public Block getBlock(int x, int y, int z)
    {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= length)
        {
            return Blocks.air;
        }

        return blocks[((z * height) + y) * width + x];
    }

    public byte getMeta(int x, int y, int z)
    {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= length)
        {
            return 0;
        }

        return metas[((z * height) + y) * width + x];
    }

    public void setBlock(int x, int y, int z, Block block)
    {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= length)
        {
            return;
        }

        blocks[((z * height) + y) * width + x] = block;
    }

    public void setMeta(int x, int y, int z, byte meta)
    {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= length)
        {
            return;
        }

        metas[((z * height) + y) * width + x] = meta;
    }

    public boolean renderSide(int x, int y, int z, int side)
    {
        if (side == 0)
        {
            y--;
        }
        else if (side == 1)
        {
            y++;
        }
        else if (side == 2)
        {
            z--;
        }
        else if (side == 3)
        {
            z++;
        }
        else if (side == 4)
        {
            x--;
        }
        else if (side == 5)
        {
            x++;
        }

        Block block = getBlock(x, y, z);
        return !block.isOpaqueCube();
    }
}
