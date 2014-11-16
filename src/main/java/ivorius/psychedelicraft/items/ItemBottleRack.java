package ivorius.psychedelicraft.items;

import ivorius.ivtoolkit.blocks.IvMultiBlockHelper;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.blocks.TileEntityBottleRack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by lukas on 16.11.14.
 */
public class ItemBottleRack extends ItemBlock
{
    public ItemBottleRack(Block block)
    {
        super(block);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int blockSide, float par8, float par9, float par10)
    {
        Block prevBlock = par3World.getBlock(x, y, z);
        int prevMeta = par3World.getBlockMetadata(x, y, z);

        if (prevBlock == Blocks.snow_layer && prevMeta < 1)
        {
            blockSide = 1;
        }
        else if (prevBlock != Blocks.vine && prevBlock != Blocks.tallgrass && prevBlock != Blocks.deadbush && !prevBlock.isReplaceable(par3World, x, y, z))
        {
            if (blockSide == 0)
                --y;
            if (blockSide == 1)
                ++y;
            if (blockSide == 2)
                --z;
            if (blockSide == 3)
                ++z;
            if (blockSide == 4)
                --x;
            if (blockSide == 5)
                ++x;
        }
        else
        {
            blockSide = 1; // When replacing a block, this should be on ground
        }

        if (blockSide == 0)
        {
            return false;
        }

        Block block = PSBlocks.bottleRack;
        boolean onWall = blockSide != 1;

        if (!block.canPlaceBlockAt(par3World, x, y, z))
        {
            return false;
        }

        int direction = 0;
        if (!onWall)
            direction = IvMultiBlockHelper.getRotation(par2EntityPlayer);
        else
        {
            if (blockSide == 2)
                direction = 0;
            if (blockSide == 3)
                direction = 2;
            if (blockSide == 4)
                direction = 3;
            if (blockSide == 5)
                direction = 1;
        }

        par3World.setBlock(x, y, z, block, onWall ? 1 : 0, 3);

        TileEntity tileEntity = par3World.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityBottleRack)
        {
            ((TileEntityBottleRack) tileEntity).direction = direction;
        }

        par1ItemStack.stackSize--;

        return true;
    }
}
