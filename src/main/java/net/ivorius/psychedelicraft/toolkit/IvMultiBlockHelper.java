/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 *
 * You are free to:
 *
 * Share — copy and redistribute the material in any medium or format
 * Adapt — remix, transform, and build upon the material
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 *
 * Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * NonCommercial — You may not use the material for commercial purposes, unless you have a permit by the creator.
 * ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.
 **************************************************************************************************/

package net.ivorius.psychedelicraft.toolkit;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class IvMultiBlockHelper implements Iterable<int[]>
{
    private Iterator<int[]> iterator;
    private IvTileEntityMultiBlock parentTileEntity = null;

    private World world;
    private Block block;
    private int metadata;

    private int direction;
    private float[] center;
    private float[] size;

    public IvMultiBlockHelper()
    {

    }

    public boolean beginPlacing(ArrayList<int[]> positions, World world, int x, int y, int z, int blockSide, ItemStack itemStack, EntityPlayer player, Block block, int metadata, int direction)
    {
        ArrayList<int[]> validLocations = IvMultiBlockHelper.getBestPlacement(positions, world, x, y, z, blockSide, itemStack, player, block);

        if (validLocations == null)
        {
            return false;
        }

        this.world = world;
        this.block = block;
        this.metadata = metadata;
        this.parentTileEntity = null;
        this.direction = direction;

        this.center = IvMultiBlockHelper.getTileEntityCenter(validLocations);
        this.size = IvMultiBlockHelper.getTileEntitySize(validLocations);

        this.iterator = validLocations.iterator();

        return true;
    }

    @Override
    public Iterator<int[]> iterator()
    {
        return iterator;
    }

    public IvTileEntityMultiBlock placeBlock(int[] blockCoords)
    {
        return placeBlock(blockCoords, this.parentTileEntity == null);
    }

    private IvTileEntityMultiBlock placeBlock(int[] blockCoords, boolean parent)
    {
        world.setBlock(blockCoords[0], blockCoords[1], blockCoords[2], block, metadata, 3);
        TileEntity tileEntity = world.getTileEntity(blockCoords[0], blockCoords[1], blockCoords[2]);

        if (tileEntity != null && tileEntity instanceof IvTileEntityMultiBlock)
        {
            IvTileEntityMultiBlock tileEntityMB = (IvTileEntityMultiBlock) tileEntity;

            if (parent)
            {
                this.parentTileEntity = tileEntityMB;
            }
            else
            {
                tileEntityMB.becomeChild(parentTileEntity);
            }

            tileEntityMB.direction = direction;
            tileEntityMB.centerCoords = center;
            tileEntityMB.centerCoordsSize = size;

            return tileEntityMB;
        }

        return null;
    }

    public static float[] getTileEntityCenter(ArrayList<int[]> positions)
    {
        float[] result = getCenter(positions);

        return new float[]{result[0] + 0.5f, result[1] + 0.5f, result[2] + 0.5f};
    }

    public static float[] getTileEntitySize(ArrayList<int[]> positions)
    {
        return getSize(positions);
    }

    public static float[] getCenter(ArrayList<int[]> positions)
    {
        if (positions.size() > 0)
        {
            int[] min = getPosition(positions, true);
            int[] max = getPosition(positions, false);

            float[] result = new float[min.length];
            for (int i = 0; i < min.length; i++)
            {
                result[i] = (min[i] + max[i]) * 0.5f;
            }

            return result;
        }

        return null;
    }

    public static float[] getSize(ArrayList<int[]> positions)
    {
        if (positions.size() > 0)
        {
            int[] min = getPosition(positions, true);
            int[] max = getPosition(positions, false);

            float[] result = new float[min.length];
            for (int i = 0; i < min.length; i++)
            {
                result[i] = (float) (max[i] - min[i] + 1) * 0.5f;
            }

            return result;
        }

        return null;
    }

    public static int[] getPosition(ArrayList<int[]> positions, boolean min)
    {
        if (positions.size() > 0)
        {
            int[] selectedPos = positions.get(0).clone();

            for (int n = 1; n < positions.size(); n++)
            {
                int[] position = positions.get(n);

                for (int i = 0; i < selectedPos.length; i++)
                {
                    selectedPos[i] = min ? Math.min(position[i], selectedPos[i]) : Math.max(position[i], selectedPos[i]);
                }
            }

            return selectedPos;
        }

        return null;
    }

    public static int[] getLengths(ArrayList<int[]> positions)
    {
        int[] min = getPosition(positions, true);
        int[] max = getPosition(positions, false);

        return new int[]{max[0] - min[0], max[1] - min[1], max[2] - min[2]};
    }

    public static boolean canPlace(World world, Block block, ArrayList<int[]> positions, Entity entity, ItemStack stack)
    {
        for (int[] position : positions)
        {
            if (!world.canPlaceEntityOnSide(block, position[0], position[1], position[2], false, 0, entity, stack))
            {
                return false;
            }
        }

        return true;
    }

    public static ArrayList<ArrayList<int[]>> getValidPlacements(ArrayList<int[]> positions, World world, int x, int y, int z, int blockSide, ItemStack itemStack, EntityPlayer player, Block block)
    {
        Block var11 = world.getBlock(x, y, z);

        if (var11 == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1)
        {
            blockSide = 1;
        }
        else if (var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush && !var11.isReplaceable(world, x, y, z))
        {
            if (blockSide == 0)
            {
                --y;
            }
            if (blockSide == 1)
            {
                ++y;
            }
            if (blockSide == 2)
            {
                --z;
            }
            if (blockSide == 3)
            {
                ++z;
            }
            if (blockSide == 4)
            {
                --x;
            }
            if (blockSide == 5)
            {
                ++x;
            }
        }

        if (!player.canPlayerEdit(x, y, z, blockSide, itemStack))
        {
            return new ArrayList<ArrayList<int[]>>();
        }
        else if (y == world.getHeight() && block.getMaterial().isSolid())
        {
            return new ArrayList<ArrayList<int[]>>();
        }
        else
        {
            int[] lengths = getLengths(positions);
            int[] min = getPosition(positions, true);

            // Run from min+length (maximimum) being the placed x, y, z to minimum being the x, y, z
            ArrayList<ArrayList<int[]>> validPlacements = new ArrayList<ArrayList<int[]>>();
            for (int xShift = min[0] - lengths[0]; xShift <= min[0]; xShift++)
            {
                for (int yShift = min[0] - lengths[1]; yShift <= min[1]; yShift++)
                {
                    for (int zShift = min[0] - lengths[2]; zShift <= min[2]; zShift++)
                    {
                        ArrayList<int[]> validPositions = new ArrayList<int[]>();

                        for (int[] position : positions)
                        {
                            validPositions.add(new int[]{position[0] + x + xShift, position[1] + y + yShift, position[2] + z + zShift});
                        }

                        if (canPlace(world, block, validPositions, null, itemStack))
                        {
                            validPlacements.add(validPositions);
                        }
                    }
                }
            }

            return validPlacements;
        }
    }

    public static ArrayList<int[]> getBestPlacement(ArrayList<int[]> positions, World world, int x, int y, int z, int blockSide, ItemStack itemStack, EntityPlayer player, Block block)
    {
        int[] lengths = getLengths(positions);

        ArrayList<ArrayList<int[]>> validPlacements = getValidPlacements(positions, world, x, y, z, blockSide, itemStack, player, block);

        if (validPlacements.size() > 0)
        {
            float[] center = new float[]{x - lengths[0] * 0.5f, y - lengths[1] * 0.5f, z - lengths[2] * 0.5f};
            ArrayList<int[]> preferredPositions = validPlacements.get(0);
            for (int i = 1; i < validPlacements.size(); i++)
            {
                int[] referenceBlock = validPlacements.get(i).get(0);
                int[] referenceBlockOriginal = preferredPositions.get(0);

                if (distanceSquared(referenceBlock, center) < distanceSquared(referenceBlockOriginal, center))
                {
                    preferredPositions = validPlacements.get(i);
                }
            }

            return preferredPositions;
        }

        return null;
    }

    private static float distanceSquared(int[] referenceBlock, float[] center)
    {
        float distX = referenceBlock[0] - center[0];
        float distY = referenceBlock[1] - center[1];
        float distZ = referenceBlock[2] - center[2];

        return distX * distX + distY * distY + distZ * distZ;
    }

    public static ArrayList<int[]> getRotatedPositions(ArrayList<int[]> positions, int rotation)
    {
        ArrayList<int[]> returnList = new ArrayList<int[]>(positions.size());

        for (int[] position : positions)
        {
            if (rotation == 0)
            {
                returnList.add(new int[]{position[0], position[1], position[2]});
            }
            if (rotation == 1)
            {
                returnList.add(new int[]{position[2], position[1], position[0]});
            }
            if (rotation == 2)
            {
                returnList.add(new int[]{position[0], position[1], position[2]});
            }
            if (rotation == 3)
            {
                returnList.add(new int[]{position[2], position[1], position[0]});
            }
        }

        return returnList;
    }

    public static IvRaytraceableAxisAlignedBox getRotatedBox(Object userInfo, double x, double y, double z, double width, double height, double depth, int direction, double centerX, double centerY, double centerZ)
    {
        IvRaytraceableAxisAlignedBox box = null;

        if (direction == 0)
        {
            box = new IvRaytraceableAxisAlignedBox(userInfo, centerX - x - width, centerY + y, centerZ + z, width, height, depth);
        }
        if (direction == 1)
        {
            box = new IvRaytraceableAxisAlignedBox(userInfo, centerX - z - depth, centerY + y, centerZ - x - width, depth, height, width);
        }
        if (direction == 2)
        {
            box = new IvRaytraceableAxisAlignedBox(userInfo, centerX + x, centerY + y, centerZ - z - depth, width, height, depth);
        }
        if (direction == 3)
        {
            box = new IvRaytraceableAxisAlignedBox(userInfo, centerX + z, centerY + y, centerZ + x, depth, height, width);
        }

        return box;
    }

    public static AxisAlignedBB getRotatedBB(double x, double y, double z, double width, double height, double depth, int direction, double centerX, double centerY, double centerZ)
    {
        AxisAlignedBB box = null;

        if (direction == 0)
        {
            box = getBBWithLengths(centerX + x, centerY + y, centerZ + z, width, height, depth);
        }
        if (direction == 1)
        {
            box = getBBWithLengths(centerX - z - depth, centerY + y, centerZ + x, depth, height, width);
        }
        if (direction == 2)
        {
            box = getBBWithLengths(centerX - x - width, centerY + y, centerZ - z - depth, width, height, depth);
        }
        if (direction == 3)
        {
            box = getBBWithLengths(centerX + z, centerY + y, centerZ - x - width, depth, height, width);
        }

        return box;
    }

    public static AxisAlignedBB getBBWithLengths(double x, double y, double z, double width, double height, double depth)
    {
        return AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + width, y + height, z + depth);
    }
}
