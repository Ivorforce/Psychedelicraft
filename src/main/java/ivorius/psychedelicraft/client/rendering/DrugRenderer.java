/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.ivtoolkit.rendering.IvOpenGLHelper;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.shaders.PSRenderStates;
import ivorius.psychedelicraft.entities.drugs.Drug;
import ivorius.psychedelicraft.entities.drugs.DrugHallucination;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by lukas on 17.02.14.
 */
public class DrugRenderer implements IDrugRenderer
{
    public static void renderOverlay(float alpha, int width, int height, ResourceLocation texture, float x0, float y0, float x1, float y1, int offset)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        bindTexture(texture);
        Tessellator var8 = Tessellator.instance;
        var8.startDrawingQuads();
        var8.addVertexWithUV(-offset, height + offset, -90.0D, x0, y1);
        var8.addVertexWithUV(width + offset, height + offset, -90.0D, x1, y1);
        var8.addVertexWithUV(width + offset, -offset, -90.0D, x1, y0);
        var8.addVertexWithUV(-offset, -offset, -90.0D, x0, y0);
        var8.draw();
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

    public static void bindTexture(ResourceLocation resourceLocation)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }
    
    public ResourceLocation hurtOverlay;

    public float experiencedHealth = 5F;

    public int timeScreenWet;
    public boolean wasInWater;
    public boolean wasInRain;

    public float currentHeat;

    public EffectLensFlare effectLensFlare;

    public DrugRenderer()
    {
        hurtOverlay = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "hurtOverlay.png");

        effectLensFlare = new EffectLensFlare();
        effectLensFlare.sunFlareSizes = new float[]{0.15f, 0.24f, 0.12f, 0.036f, 0.06f, 0.048f, 0.006f, 0.012f, 0.5f, 0.09f, 0.036f, 0.09f, 0.06f, 0.05f, 0.6f};
        effectLensFlare.sunFlareInfluences = new float[]{-1.3f, -2.0f, 0.2f, 0.4f, 0.25f, -0.25f, -0.7f, -1.0f, 1.0f, 1.4f, -1.31f, -1.2f, -1.5f, -1.55f, -3.0f};
        effectLensFlare.sunBlindnessTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "sunBlindness.png");
        effectLensFlare.sunFlareTextures = new ResourceLocation[effectLensFlare.sunFlareSizes.length];
        for (int i = 0; i < effectLensFlare.sunFlareTextures.length; i++)
        {
            effectLensFlare.sunFlareTextures[i] = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "flare" + i + ".png");
        }
    }

    @Override
    public void update(DrugProperties drugProperties, EntityLivingBase entity)
    {
        if (DrugProperties.hurtOverlayEnabled)
        {
            experiencedHealth = IvMathHelper.nearValue(experiencedHealth, entity.getHealth(), 0.01f, 0.01f);
        }

        if (PSRenderStates.sunFlareIntensity > 0.0f)
        {
            effectLensFlare.updateLensFlares();
        }

        Block bID = ActiveRenderInfo.getBlockAtEntityViewpoint(entity.worldObj, entity, 1.0F);
        wasInWater = bID.getMaterial() == Material.water;
        //wasInRain = player.worldObj.getRainStrength(1.0f) > 0.0f && player.worldObj.getPrecipitationHeight(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY)) <= player.posY; //Client can't handle rain

        if (DrugProperties.waterOverlayEnabled)
        {
            timeScreenWet--;

            if (wasInWater)
            {
                timeScreenWet += 20;
            }
            if (wasInRain)
            {
                timeScreenWet += 4;
            }

            timeScreenWet = MathHelper.clamp_int(timeScreenWet, 0, 100);
        }

        int playerX = MathHelper.floor_double(entity.posX);
        int playerY = MathHelper.floor_double(entity.posY);
        int playerZ = MathHelper.floor_double(entity.posZ);
        float newHeat = entity.worldObj.getBiomeGenForCoordsBody(playerX, playerZ).getFloatTemperature(playerX, playerY, playerZ);

        this.currentHeat = IvMathHelper.nearValue(currentHeat, newHeat, 0.01f, 0.01f);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void distortScreen(float partialTicks, EntityLivingBase entity, int rendererUpdateCount, DrugProperties drugProperties)
    {
        float wobblyness = 0.0f;
        for (Drug drug : drugProperties.getAllDrugs())
            wobblyness += drug.viewWobblyness();

        if (wobblyness > 0.0F)
        {
            if (wobblyness > 1.0F)
                wobblyness = 1.0F;

            float f4 = 5F / (wobblyness * wobblyness + 5F) - wobblyness * 0.04F;
            f4 *= f4;

            float sin1 = MathHelper.sin(((rendererUpdateCount + partialTicks) / 150 * (float) Math.PI));
            float sin2 = MathHelper.sin(((rendererUpdateCount + partialTicks) / 170 * (float) Math.PI));
            float sin3 = MathHelper.sin(((rendererUpdateCount + partialTicks) / 190 * (float) Math.PI));

            GL11.glRotatef((rendererUpdateCount + partialTicks) * 3F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / (f4 + (wobblyness * sin1) / 2), 1.0F / (f4 + (wobblyness * sin2) / 2), 1.0F / (f4 + (wobblyness * sin3) / 2));
            GL11.glRotatef(-(rendererUpdateCount + partialTicks) * 3F, 0.0F, 1.0F, 1.0F);
        }

        float shiftX = DrugEffectInterpreter.getCameraShiftX(drugProperties, (float) rendererUpdateCount + partialTicks);
        float shiftY = DrugEffectInterpreter.getCameraShiftY(drugProperties, (float) rendererUpdateCount + partialTicks);
        GL11.glTranslatef(shiftX, shiftY, 0.0f);
    }

    public void renderOverlaysBeforeShaders(float partialTicks, EntityLivingBase entity, int updateCounter, int width, int height, DrugProperties drugProperties)
    {
        IvOpenGLHelper.setUpOpenGLStandard2D(width, height);

        effectLensFlare.sunFlareIntensity = PSRenderStates.sunFlareIntensity;

        if (effectLensFlare.shouldApply(updateCounter + partialTicks))
        {
            effectLensFlare.renderLensFlares(width, height, partialTicks);
        }
    }

    @Override
    public void renderOverlaysAfterShaders(float partialTicks, EntityLivingBase entity, int updateCounter, int width, int height, DrugProperties drugProperties)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        for (Drug drug : drugProperties.getAllDrugs())
            drug.drawOverlays(partialTicks, entity, updateCounter, width, height, drugProperties);

        if (DrugProperties.hurtOverlayEnabled)
        {
            if (entity.hurtTime > 0 || experiencedHealth < 5F)
            {
                float p1 = (float) entity.hurtTime / (float) entity.maxHurtTime;
                float p2 = +(5f - experiencedHealth) / 6f;

                float p = p1 > 0.0F ? p1 : 0.0f + p2 > 0.0F ? p2 : 0.0F;
                renderOverlay(p, width, height, hurtOverlay, 0.0F, 0.0F, 1.0F, 1.0F, (int) ((1.0F - p) * 40F));
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    @Override
    public void renderAllHallucinations(float par1, DrugProperties drugProperties)
    {
        for (DrugHallucination h : drugProperties.hallucinationManager.entities)
        {
            h.render(par1, MathHelper.clamp_float(0.0f, drugProperties.hallucinationManager.getHallucinationStrength(drugProperties, par1) * 15.0f, 1.0f));
        }
    }

    @Override
    public float getCurrentHeatDistortion()
    {
        if (wasInWater)
            return 0.0f;

        return IvMathHelper.clamp(0.0f, ((currentHeat - 1.0f) * 0.0015f), 0.01f);
    }

    @Override
    public float getCurrentWaterDistortion()
    {
        return wasInWater ? 0.025f : 0.0f;
    }

    @Override
    public float getCurrentWaterScreenDistortion()
    {
        if (timeScreenWet > 0 && !wasInWater)
        {
            float p = timeScreenWet / 80f;
            if (p > 1.0F)
            {
                p = 1.0F;
            }

            return p;
        }

        return 0.0f;
    }
}
