/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering.blocks;

import ivorius.ivtoolkit.blocks.IvMultiBlockRenderHelper;
import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.TileEntityMashTub;
import ivorius.psychedelicraft.client.rendering.FluidBoxRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererMashTub extends TileEntitySpecialRenderer
{
    public static IModelCustom modelWoodenVat = AdvancedModelLoader.loadModel(new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathModels + "woodenVat.obj"));
    public static ResourceLocation textureMashTub = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "woodenVat.png");

    private IModelCustom model;
    private ResourceLocation texture;

    public TileEntityRendererMashTub()
    {
        model = modelWoodenVat;
        texture = textureMashTub;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityMashTub((TileEntityMashTub) tileentity, d, d1, d2, f);
    }

    public void renderTileEntityMashTub(TileEntityMashTub tileEntity, double x, double y, double z, float partialTicks)
    {
        if (tileEntity.isParent())
        {
            GL11.glPushMatrix();
            IvMultiBlockRenderHelper.transformFor(tileEntity, x, y, z);
            GL11.glTranslated(0.0f, 0.002f, 0.0f);

            GL11.glPushMatrix();
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0f, -.5f, 0f);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            this.bindTexture(texture);
            model.renderAll();
            GL11.glPopMatrix();

            FluidStack fluidStack = tileEntity.tank.getFluid();
            if (fluidStack != null)
            {
                float size = 15.0f / 16.0f;
                float borderWidth = 1.0f / 16.0f;
                float height = 12.0f / 16.0f;

                float fluidHeight = (height - borderWidth - 1.0f / 16.0f) * IvMathHelper.clamp(0.0f, (float) fluidStack.amount / (float) tileEntity.tank.getCapacity(), 1.0f);

                FluidBoxRenderer fluidBoxRenderer = new FluidBoxRenderer(1.0f);
                fluidBoxRenderer.prepare(fluidStack);

                fluidBoxRenderer.renderFluid(-size, -.5f + borderWidth, -size, size * 2, fluidHeight, size * 2, ForgeDirection.UP);

                fluidBoxRenderer.cleanUp();
            }

            GL11.glPopMatrix();
        }
    }
}
