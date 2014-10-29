/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.blocks;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.TileEntityMashTub;
import ivorius.psychedelicraft.client.rendering.FluidBoxRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererMashTub extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityStatueAt((TileEntityMashTub) tileentity, d, d1, d2, f);
    }

    public void renderTileEntityStatueAt(TileEntityMashTub tileEntity, double d, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(d, d1, d2);

        FluidStack fluidStack = tileEntity.containedFluid();
        if (fluidStack != null)
        {
            float size = 6.0f / 16.0f;
            float borderWidth = 1.0f / 16.0f;
            float height = 12.0f / 16.0f;

            float fluidHeight = (height - borderWidth - 1.0f / 16.0f) * IvMathHelper.clamp(0.0f, (float) fluidStack.amount / (float) tileEntity.tankCapacity(), 1.0f);

            FluidBoxRenderer fluidBoxRenderer = new FluidBoxRenderer(1.0f);
            fluidBoxRenderer.prepare(fluidStack);

            fluidBoxRenderer.renderFluid(0.5f - size, borderWidth, 0.5f - size, size * 2, fluidHeight, size * 2, ForgeDirection.UP);

            fluidBoxRenderer.cleanUp();
        }

        GL11.glPopMatrix();
    }
}
