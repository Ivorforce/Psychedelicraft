/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import ivorius.psychedelicraft.fluids.TranslucentFluid;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

/**
 * Created by lukas on 27.10.14.
 */
public class FluidBoxRenderer
{
    public static void renderFluid(float x, float y, float z, float width, float height, float length, float texX0, float texX1, float texY0, float texY1, ForgeDirection... directions)
    {
        Tessellator tessellator = Tessellator.instance;

        for (ForgeDirection direction : directions)
        {
            tessellator.setNormal(direction.offsetX, direction.offsetY, direction.offsetZ);

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
                    tessellator.addVertexWithUV(x + width, y + height, z, texX1, texY0);
                    tessellator.addVertexWithUV(x + width, y + height, z + length, texX1, texY1);
                    tessellator.addVertexWithUV(x + width, y, z + length, texX0, texY1);
                    break;
                case WEST:
                    tessellator.addVertexWithUV(x, y, z, texX0, texY0);
                    tessellator.addVertexWithUV(x, y, z + length, texX1, texY0);
                    tessellator.addVertexWithUV(x, y + height, z + length, texX1, texY1);
                    tessellator.addVertexWithUV(x, y + height, z, texX0, texY1);
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

    public float scale;

    public FluidBoxRenderer(float scale)
    {
        this.scale = scale;
    }

    private float texX0, texX1, texY0, texY1;
    private boolean preparedTranslucency;
    private boolean disabledTextures;

    public void prepare(FluidStack fluidStack)
    {
        Fluid fluid = fluidStack.getFluid();
        IIcon icon = fluid.getIcon(fluidStack);

        if (icon == null)
        {
            boolean translucent = fluid instanceof TranslucentFluid;
            MCColorHelper.setColor(fluid.getColor(fluidStack), translucent);
            texX0 = texX1 = texY0 = texY1 = 0.0f;
            disableTexture();

            if (translucent)
                prepareTranslucency();
        }
        else
        {
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            texX0 = icon.getMinU();
            texX1 = icon.getMaxU();
            texY0 = icon.getMinV();
            texY1 = icon.getMaxV();
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            prepareTranslucency();
        }

        Tessellator.instance.startDrawingQuads();
    }

    public void prepare(ItemStack itemStack)
    {
        Block block = ((ItemBlock) itemStack.getItem()).field_150939_a;
        IIcon icon = block.getIcon(ForgeDirection.UP.ordinal(), itemStack.getItemDamage() % 16);

        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        texX0 = icon.getMinU();
        texX1 = icon.getMaxU();
        texY0 = icon.getMinV();
        texY1 = icon.getMaxV();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        prepareTranslucency();

        Tessellator.instance.startDrawingQuads();
    }

    private void disableTexture()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        disabledTextures = true;
    }

    private void prepareTranslucency()
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.001f);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        preparedTranslucency = true;
    }

    public void renderFluid(float x, float y, float z, float width, float height, float length, ForgeDirection... directions)
    {
        renderFluid(x * scale, y * scale, z * scale, width * scale, height * scale, length * scale, texX0, texX1, texY0, texY1, directions);
    }

    public void cleanUp()
    {
        Tessellator.instance.draw();

        if (preparedTranslucency)
            GL11.glDisable(GL11.GL_BLEND);
        if (disabledTextures)
            GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
