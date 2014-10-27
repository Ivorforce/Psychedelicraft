/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.blocks;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.TileEntityFlask;
import ivorius.psychedelicraft.client.rendering.MCColorHelper;
import ivorius.psychedelicraft.fluids.TranslucentFluid;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 25.10.14.
 */
public class TileEntityRendererFlask extends TileEntitySpecialRenderer
{
    private ModelFlask model;
    private ResourceLocation texture;

    public TileEntityRendererFlask()
    {
        model = new ModelFlask();
        texture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "flask.png");
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks)
    {
        TileEntityFlask flask = (TileEntityFlask) tileEntity;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5f, y + 0.502f, z + 0.5f);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        this.bindTexture(texture);
        model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.001f);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        FluidStack fluidStack = flask.containedFluid();
        if (fluidStack != null)
        {
            GL11.glScalef(1.0f / 16.0f, 1.0f / 16.0f, 1.0f / 16.0f);

            Fluid fluid = fluidStack.getFluid();
            IIcon icon = fluid.getIcon(fluidStack);
            float fluidHeight = IvMathHelper.clamp(0.0f, (float)fluidStack.amount / (float) flask.tankCapacity(), 1.0f) * 2.8f;

            float texX0, texX1, texY0, texY1;

            if (icon == null)
            {
                MCColorHelper.setColor(fluid.getColor(fluidStack), fluid instanceof TranslucentFluid);
                texX0 = texX1 = texY0 = texY1 = 0.0f;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            else
            {
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                texX0 = icon.getMinU();
                texX1 = icon.getMaxU();
                texY0 = icon.getMinV();
                texY1 = icon.getMaxV();
                bindTexture(TextureMap.locationBlocksTexture);
            }

            Tessellator tessellator = Tessellator.instance;

            tessellator.startDrawingQuads();
            renderFluid(-1.9f, -8.0f, -3.9f, 3.8f, fluidHeight, 0.9f, texX0, texX1, texY0, texY1, ForgeDirection.NORTH, ForgeDirection.UP);
            renderFluid(-1.9f, -8.0f, 3.0f, 3.8f, fluidHeight, 0.9f, texX0, texX1, texY0, texY1, ForgeDirection.SOUTH, ForgeDirection.UP);
            renderFluid(-3.9f, -8.0f, -1.9f, 0.9f, fluidHeight, 3.8f, texX0, texX1, texY0, texY1, ForgeDirection.EAST, ForgeDirection.UP);
            renderFluid(3.0f, -8.0f, -1.9f, 0.9f, fluidHeight, 3.8f, texX0, texX1, texY0, texY1, ForgeDirection.WEST, ForgeDirection.UP);
            renderFluid(-3.0f, -8.0f, -3.0f, 6.0f, fluidHeight, 6.0f, texX0, texX1, texY0, texY1, ForgeDirection.UP);
            tessellator.draw();

            if (icon == null)
                GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void renderFluid(float x, float y, float z, float width, float height, float length, float texX0, float texX1, float texY0, float texY1, ForgeDirection... directions)
    {
        Tessellator tessellator = Tessellator.instance;

        for (ForgeDirection direction : directions)
        {
            switch (direction)
            {
                case DOWN:
                    tessellator.addVertexWithUV(x, y, z, texX0, texY0);
                    tessellator.addVertexWithUV(x + width, y, z, texX1, texY0);
                    tessellator.addVertexWithUV(x + width, y, z + length, texX1, texY1);
                    tessellator.addVertexWithUV(x, y, z + length, texX0, texY1);
                    break;
                case UP:
                    tessellator.addVertexWithUV(x, y + height, z, texX0, texY0);
                    tessellator.addVertexWithUV(x, y + height, z + length, texX0, texY1);
                    tessellator.addVertexWithUV(x + width, y + height, z + length, texX1, texY1);
                    tessellator.addVertexWithUV(x + width, y + height, z, texX1, texY0);
                    break;
                case EAST:
                    tessellator.addVertexWithUV(x + width, y, z, texX0, texY0);
                    tessellator.addVertexWithUV(x + width, y, z + length, texX0, texY1);
                    tessellator.addVertexWithUV(x + width, y + height, z + length, texX1, texY1);
                    tessellator.addVertexWithUV(x + width, y + height, z, texX1, texY0);
                    break;
                case WEST:
                    tessellator.addVertexWithUV(x, y, z, texX0, texY0);
                    tessellator.addVertexWithUV(x, y + height, z, texX0, texY1);
                    tessellator.addVertexWithUV(x, y + height, z + length, texX1, texY1);
                    tessellator.addVertexWithUV(x, y, z + length, texX1, texY0);
                    break;
                case NORTH:
                    tessellator.addVertexWithUV(x, y, z, texX0, texY0);
                    tessellator.addVertexWithUV(x, y + height, z, texX0, texY1);
                    tessellator.addVertexWithUV(x + width, y + height, z, texX1, texY1);
                    tessellator.addVertexWithUV(x + width, y, z, texX1, texY0);
                    break;
                case SOUTH:
                    tessellator.addVertexWithUV(x, y, z + length, texX0, texY0);
                    tessellator.addVertexWithUV(x + width, y, z + length, texX1, texY0);
                    tessellator.addVertexWithUV(x + width, y + height, z + length, texX1, texY1);
                    tessellator.addVertexWithUV(x, y + height, z + length, texX0, texY1);
                    break;
            }
        }
    }
}
