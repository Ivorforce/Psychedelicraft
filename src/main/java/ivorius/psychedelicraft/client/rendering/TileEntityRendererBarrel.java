/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.TileEntityBarrel;
import ivorius.psychedelicraft.items.DrinkRegistry;
import ivorius.psychedelicraft.items.IDrink;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererBarrel extends TileEntitySpecialRenderer
{
    private ModelBarrel barrelModel;
    private ResourceLocation barrelTexture;

    public TileEntityRendererBarrel()
    {
        super();

        this.barrelModel = new ModelBarrel();
        this.barrelTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "barrelTexture.png");
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityStatueAt((TileEntityBarrel) tileentity, d, d1, d2, f);
    }

    public void renderTileEntityStatueAt(TileEntityBarrel tileEntity, double d, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.5f, (float) d2 + 0.5F);
        GL11.glRotatef(-90.0f * tileEntity.getBlockRotation() + 180.0f, 0.0f, 1.0f, 0.0f);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);

        Entity emptyEntity = new EntityArrow(tileEntity.getWorldObj());
        emptyEntity.ticksExisted = (int) (tileEntity.getTapRotation() * 100.0f);

        this.bindTexture(barrelTexture);
        barrelModel.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        GL11.glPopMatrix();

        if (tileEntity.containedDrink != null)
        {
            IIcon drinkIcon = tileEntity.containedDrink.getDrinkIcon();
            if (drinkIcon != null)
            {
                this.bindTexture(TextureMap.locationItemsTexture);
                Tessellator tessellator = Tessellator.instance;

                double barrelZ = -0.45;
                double iconSize = 1.0;
                double centerX = 0.0;
                double centerY = 0.0;

                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(centerX - iconSize * 0.5, centerY - iconSize * 0.5, barrelZ, drinkIcon.getMaxU(), drinkIcon.getMaxV());
                tessellator.addVertexWithUV(centerX - iconSize * 0.5, centerY + iconSize * 0.5, barrelZ, drinkIcon.getMaxU(), drinkIcon.getMinV());
                tessellator.addVertexWithUV(centerX + iconSize * 0.5, centerY + iconSize * 0.5, barrelZ, drinkIcon.getMinU(), drinkIcon.getMinV());
                tessellator.addVertexWithUV(centerX + iconSize * 0.5, centerY - iconSize * 0.5, barrelZ, drinkIcon.getMinU(), drinkIcon.getMaxV());
                tessellator.draw();
            }
        }

        GL11.glPopMatrix();
    }
}
