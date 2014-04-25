package net.ivorius.psychedelicraft.client.rendering;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.blocks.TileEntityRiftJar;
import net.ivorius.psychedelicraft.toolkit.IvBezierPath3D;
import net.ivorius.psychedelicraft.toolkit.IvBezierPath3DCreator;
import net.ivorius.psychedelicraft.toolkit.IvRenderHelper;
import net.ivorius.psychedelicraft.toolkit.IvStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TileEntityRendererRiftJar extends TileEntitySpecialRenderer
{
    public ModelBase model;
    public ResourceLocation texture;
    public ResourceLocation crackedTexture;

    public ResourceLocation[] zeroScreenTexture;

    public IvBezierPath3D sphereBezierPath;
    public IvBezierPath3D outgoingBezierPath;

    public TileEntityRendererRiftJar()
    {
        super();

        this.model = new ModelMystJar();
        texture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "riftJar.png");
        crackedTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "riftJarCracked.png");

        zeroScreenTexture = new ResourceLocation[8];
        for (int i = 0; i < zeroScreenTexture.length; i++)
        {
            zeroScreenTexture[i] = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "zeroScreen" + i + ".png");
        }

        sphereBezierPath = IvBezierPath3DCreator.createSpiraledSphere(3.0, 8.0, 0.2);
        outgoingBezierPath = IvBezierPath3DCreator.createSpiraledBezierPath(0.06, 6.0, 6.0, 1.0, 0.2, 0.0, false);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityRiftJarAt((TileEntityRiftJar) tileentity, d, d1, d2, f);
    }

    public void renderTileEntityRiftJarAt(TileEntityRiftJar tileEntity, double d, double d1, double d2, float f)
    {
        Tessellator tessellator = Tessellator.instance;
        float ticks = tileEntity.ticksAliveVisual + f;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.5f, (float) d2 + 0.5F);

        Entity emptyEntity = new EntityArrow(tileEntity.getWorldObj());
        emptyEntity.rotationYaw = tileEntity.fractionOpen;
        emptyEntity.rotationPitch = tileEntity.fractionHandleUp * (1.0f + MathHelper.sin(tileEntity.ticksAliveVisual * 0.1f) * 0.1f);

        GL11.glPushMatrix();
        GL11.glRotatef(-90.0f * tileEntity.getBlockRotation() + 180.0f, 0.0f, 1.0f, 0.0f);

        if (tileEntity.currentRiftFraction > 0.0f)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            renderZeroInsides(f, ticks, Math.min(tileEntity.currentRiftFraction * 2.0f, 1.0f));
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glTranslatef(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);

        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        this.bindTexture(texture);
        model.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        float crackedVisibility = Math.min((tileEntity.currentRiftFraction - 0.5f) * 2.0f, 1.0f);

        if (crackedVisibility > 0.0f)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, crackedVisibility);
            this.bindTexture(crackedTexture);
            model.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glPopMatrix();
        GL11.glPopMatrix();

        FontRenderer fontRenderer = Minecraft.getMinecraft().standardGalacticFontRenderer;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.5f, (float) d1 + 0.6f, (float) d2 + 0.5f);
        GL11.glDisable(GL11.GL_CULL_FACE);
        for (TileEntityRiftJar.JarRiftConnection connection : tileEntity.riftConnections)
        {
            if (connection.bezierPath3D == null)
            {
                connection.bezierPath3D = IvBezierPath3DCreator.createSpiraledBezierPath(0.1, 0.5, 8.0, new double[]{connection.entityX - (tileEntity.xCoord + 0.5), connection.entityY - (tileEntity.yCoord + 0.6), connection.entityZ - (tileEntity.zCoord + 0.5)}, 0.2, 0.0, false);
            }

            IvBezierPath3D bezierPath3D = connection.bezierPath3D;

//            int textureChosen = MathHelper.floor_double(ticks * 0.5f);
//            Random thisTextureMov = new Random(textureChosen);
//            bindTexture(zeroScreenTexture[textureChosen % 8]);
//            DrugShaderHelper.setUseScreenTexCoords(true);
//            float pixelsX = 140.0f / 2.0f;
//            float pixelsY = 224.0f / 2.0f;
//            DrugShaderHelper.setPixelSize(1.0f / pixelsX, -1.0f / pixelsY);
//            GL11.glTexCoord2f(thisTextureMov.nextInt(10) * 0.1f * pixelsX, thisTextureMov.nextInt(8) * 0.125f * pixelsY);
//            bezierPath3D.render(0.2f, 0.05f, ticks * -0.002);
//            DrugShaderHelper.setScreenSizeDefault();
//            DrugShaderHelper.setUseScreenTexCoords(false);

            String spiralString = "This is a small spiral.";
            bezierPath3D.renderText(spiralString, fontRenderer, true, (tileEntity.ticksAliveVisual + f) * -0.002, false, 0.0, connection.fractionUp);

            if (connection.fractionUp > 0.0f)
            {
                GL11.glPushMatrix();
                GL11.glTranslated(connection.entityX - (tileEntity.xCoord + 0.5), connection.entityY - (tileEntity.yCoord + 0.6), connection.entityZ - (tileEntity.zCoord + 0.5));
                String sphereStringFull = "This is a small circle.";
                String sphereString = IvStringHelper.cheeseString(sphereStringFull, 1.0f - connection.fractionUp, 42);
                sphereBezierPath.renderText(sphereString, fontRenderer, true, (tileEntity.ticksAliveVisual + f) * -0.002, false);
                GL11.glPopMatrix();
            }
        }

        float outgoingStrength = tileEntity.fractionHandleUp * tileEntity.fractionOpen;
        if (outgoingStrength > 0.0f)
        {
            String spiralString = "This is a small spiral.";
            outgoingBezierPath.renderText(spiralString, fontRenderer, true, (tileEntity.ticksAliveVisual + f) * 0.002, false, 0.0, outgoingStrength);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    public void renderZeroInsides(float partialTicks, float ticks, float alpha)
    {
        int textureChosen = MathHelper.floor_double(ticks * 0.5f);
        Random thisTextureMov = new Random(textureChosen);
        bindTexture(zeroScreenTexture[textureChosen % 8]);
        DrugShaderHelper.setUseScreenTexCoords(true);
        float pixelsX = 140.0f / 2.0f;
        float pixelsY = 224.0f / 2.0f;
        DrugShaderHelper.setPixelSize(1.0f / pixelsX, -1.0f / pixelsY);
        GL11.glTexCoord2f(thisTextureMov.nextInt(10) * 0.1f * pixelsX, thisTextureMov.nextInt(8) * 0.125f * pixelsY);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
        renderJarInside(partialTicks, alpha);
        DrugShaderHelper.setScreenSizeDefault();
        DrugShaderHelper.setUseScreenTexCoords(false);
    }

    public void renderJarInside(float partialTicks, float alpha)
    {
        Tessellator tessellator = Tessellator.instance;

        float in = 0.001f;
        float in2 = in * 2.0f;

        tessellator.startDrawingQuads();
        IvRenderHelper.drawModelCuboid(tessellator, -4.0f + in, 0.0f + in, -4.0f + in, 8.0f - in2, 5.0f - in2, 8.0f - in2);
        IvRenderHelper.drawModelCuboid(tessellator, -3.0f + in, 5.0f - in, -3.0f + in, 6.0f - in2, 2.0f + in2, 6.0f - in2);
        IvRenderHelper.drawModelCuboid(tessellator, -4.0f + in, 7.0f + in, -4.0f + in, 8.0f - in2, 5.0f - in2, 8.0f - in2);
        tessellator.draw();
    }
}
