/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderMolotovCocktail extends Render
{
    private float field_77002_a;

    public ResourceLocation texture;

    public RenderMolotovCocktail(float par1)
    {
        this.field_77002_a = par1;
        texture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "molotovCocktailTexture.png");
    }

    public void doRenderCocktail(EntityMolotovCocktail par1EntityCocktail, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float var10 = this.field_77002_a;
        GL11.glScalef(var10 / 1.0F, var10 / 1.0F, var10 / 1.0F);
        bindEntityTexture(par1EntityCocktail);
        Tessellator var12 = Tessellator.instance;
        float var13 = 0; //TexMinX
        float var14 = 1; //TexMaxX
        float var15 = 0; //TexMinY
        float var16 = 1; //TexMaxY
        float var17 = 1.0F;
        float var18 = 0.5F;
        float var19 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var12.startDrawingQuads();
        var12.setNormal(0.0F, 1.0F, 0.0F);
        var12.addVertexWithUV(0.0F - var18, 0.0F - var19, 0.0D, var13, var16);
        var12.addVertexWithUV(var17 - var18, 0.0F - var19, 0.0D, var14, var16);
        var12.addVertexWithUV(var17 - var18, 1.0F - var19, 0.0D, var14, var15);
        var12.addVertexWithUV(0.0F - var18, 1.0F - var19, 0.0D, var13, var15);
        var12.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderCocktail((EntityMolotovCocktail) par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity var1)
    {
        return texture;
    }
}
