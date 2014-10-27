/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.blocks;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.TileEntityBarrel;
import ivorius.psychedelicraft.blocks.TileEntityMashTub;
import ivorius.psychedelicraft.client.rendering.MCColorHelper;
import ivorius.psychedelicraft.fluids.FluidWithIconSymbol;
import ivorius.psychedelicraft.fluids.FluidWithIconSymbolRegistering;
import ivorius.psychedelicraft.fluids.TranslucentFluid;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import static ivorius.psychedelicraft.client.rendering.blocks.TileEntityRendererFlask.renderFluid;

public class TileEntityRendererMashTub extends TileEntitySpecialRenderer
{
    private ModelBarrel model;

    private ResourceLocation texture;

    public TileEntityRendererMashTub()
    {
        super();

        this.model = new ModelBarrel();
        this.texture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "barrelTexture.png");
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityStatueAt((TileEntityMashTub) tileentity, d, d1, d2, f);
    }

    public void renderTileEntityStatueAt(TileEntityMashTub tileEntity, double d, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.5f, (float) d2 + 0.5F);
        GL11.glRotatef(-90.0f * tileEntity.getBlockRotation() + 180.0f, 0.0f, 1.0f, 0.0f);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);

        Entity emptyEntity = new EntityArrow(tileEntity.getWorldObj());

//        this.bindTexture(texture);
//        model.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        GL11.glPopMatrix();

        FluidStack fluidStack = tileEntity.containedFluid();
        if (fluidStack != null)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.001f);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            GL11.glScalef(1.0f / 16.0f, 1.0f / 16.0f, 1.0f / 16.0f);

            Fluid fluid = fluidStack.getFluid();
            IIcon icon = fluid.getIcon(fluidStack);
            float fluidHeight = IvMathHelper.clamp(0.0f, (float) fluidStack.amount / (float) tileEntity.tankCapacity(), 1.0f);

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
            renderFluid(-8.0f, -8.0f, -8.0f, 16.0f, fluidHeight * 16.0f, 16.0f, texX0, texX1, texY0, texY1, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.UP);
            tessellator.draw();

            if (icon == null)
                GL11.glEnable(GL11.GL_TEXTURE_2D);

            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glPopMatrix();
    }
}
