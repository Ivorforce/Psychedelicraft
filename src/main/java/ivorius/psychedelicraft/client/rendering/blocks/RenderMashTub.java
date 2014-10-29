/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.blocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import ivorius.psychedelicraft.Psychedelicraft;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 29.10.14.
 */
public class RenderMashTub implements ISimpleBlockRenderingHandler
{
    public static void renderStandardBlock(RenderBlocks rb, Block block, int metadata, double x, double y, double z)
    {
        Tessellator tessellator = Tessellator.instance;

        rb.setRenderBoundsFromBlock(block);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        rb.renderFaceYNeg(block, x, y, z, rb.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        rb.renderFaceYPos(block, x, y, z, rb.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        rb.renderFaceZNeg(block, x, y, z, rb.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        rb.renderFaceZPos(block, x, y, z, rb.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        rb.renderFaceXNeg(block, x, y, z, rb.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        rb.renderFaceXPos(block, x, y, z, rb.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        float size = 6.0f / 16.0f;
        float borderWidth = 1.0f / 16.0f;
        float height = 12.0f / 16.0f;

        double x = 0.0, y = 0.0, z = 0.0;

        block.setBlockBounds(0.5f - size - borderWidth, borderWidth, 0.5f - size, 0.5f - size, height, 0.5f + size);
        renderStandardBlock(renderer, block, metadata, x, y, z);
        block.setBlockBounds(0.5f + size, borderWidth, 0.5f - size, 0.5f + size + borderWidth, height, 0.5f + size);
        renderStandardBlock(renderer, block, metadata, x, y, z);

        block.setBlockBounds(0.5f - size - borderWidth, borderWidth, 0.5f - size - borderWidth, 0.5f + size + borderWidth, height, 0.5f - size);
        renderStandardBlock(renderer, block, metadata, x, y, z);
        block.setBlockBounds(0.5f - size - borderWidth, borderWidth, 0.5f + size, 0.5f + size + borderWidth, height, 0.5f + size + borderWidth);
        renderStandardBlock(renderer, block, metadata, x, y, z);

        block.setBlockBounds(0.5f - size - borderWidth, 0.0f, 0.5f - size - borderWidth, 0.5f + size + borderWidth, borderWidth, 0.5f + size + borderWidth);
        renderStandardBlock(renderer, block, metadata, x, y, z);

        block.setBlockBounds(0.5f - size - borderWidth, 0.0f, 0.5f - size - borderWidth, 0.5f + size + borderWidth, height, 0.5f + size + borderWidth);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        float size = 6.0f / 16.0f;
        float borderWidth = 1.0f / 16.0f;
        float height = 12.0f / 16.0f;

        block.setBlockBounds(0.5f - size - borderWidth, borderWidth, 0.5f - size, 0.5f - size, height, 0.5f + size);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(0.5f + size, borderWidth, 0.5f - size, 0.5f + size + borderWidth, height, 0.5f + size);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        block.setBlockBounds(0.5f - size - borderWidth, borderWidth, 0.5f - size - borderWidth, 0.5f + size + borderWidth, height, 0.5f - size);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(0.5f - size - borderWidth, borderWidth, 0.5f + size, 0.5f + size + borderWidth, height, 0.5f + size + borderWidth);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        block.setBlockBounds(0.5f - size - borderWidth, 0.0f, 0.5f - size - borderWidth, 0.5f + size + borderWidth, borderWidth, 0.5f + size + borderWidth);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        block.setBlockBounds(0.5f - size - borderWidth, 0.0f, 0.5f - size - borderWidth, 0.5f + size + borderWidth, height, 0.5f + size + borderWidth);

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
        return Psychedelicraft.blockMashTubRenderType;
    }
}
