/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.BlockWineGrapeLattice;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 17.02.14.
 */
public class RenderWineGrapeLattice implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float f1 = 0.0625F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 1));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addTranslation(0.0F, 0.0F, f1);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 2));
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 3));
        tessellator.addTranslation(0.0F, 0.0F, f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 4));
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 5));
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        int m = world.getBlockMetadata(x, y, z);
        int rotation = m & 1;
        int grapesGrownState = m >> 1;

        Tessellator tessellator = Tessellator.instance;
        BlockWineGrapeLattice latticeBlock = ((BlockWineGrapeLattice) block);

        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        latticeBlock.currentIcon = latticeBlock.getDefaultBlockIcon();

        float d = 0.1F;
        if (rotation == 0)
        {
            renderer.setRenderBounds(0.01f, 0.01f, 0.5f - d, 0.99f, 0.99f, 0.5f + d);
        }
        if (rotation == 1)
        {
            renderer.setRenderBounds(0.5f - d, 0.01f, 0.01F, 0.5F + d, 0.99f, 0.99f);
        }
        renderer.renderStandardBlock(block, x, y, z);

        if (grapesGrownState > 0)
        {
            latticeBlock.currentIcon = latticeBlock.leavesIcons[grapesGrownState - 1];

            float f1 = 0.11f + (grapesGrownState - 1) * 0.06f;
            if (rotation == 0)
            {
                renderer.setRenderBounds(0.0F, 0.0F, 0.5F - f1, 1.0F, 1.0F, 0.5F + f1);
            }
            if (rotation == 1)
            {
                renderer.setRenderBounds(0.5F - f1, 0.0F, 0.0F, 0.5F + f1, 1.0F, 1.0F);
            }

            renderer.renderStandardBlock(block, x, y, z);
        }

        block.setBlockBoundsBasedOnState(world, x, y, z);
        latticeBlock.currentIcon = latticeBlock.getDefaultBlockIcon();

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return Psychedelicraft.blockWineGrapeLatticeRenderType;
    }
}
