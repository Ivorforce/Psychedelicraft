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
            float fluidHeight = 16.0f * IvMathHelper.clamp(0.0f, (float) fluidStack.amount / (float) tileEntity.tankCapacity(), 1.0f);

            FluidBoxRenderer fluidBoxRenderer = new FluidBoxRenderer(1.0f / 16.0f);
            fluidBoxRenderer.prepare(fluidStack);

            fluidBoxRenderer.renderFluid(-8.0f, -8.0f, -8.0f, 16.0f, fluidHeight, 16.0f, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.UP);

            fluidBoxRenderer.cleanUp();
        }

        GL11.glPopMatrix();
    }
}
