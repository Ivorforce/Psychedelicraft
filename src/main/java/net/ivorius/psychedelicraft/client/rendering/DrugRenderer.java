package net.ivorius.psychedelicraft.client.rendering;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.entities.DrugHallucination;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.toolkit.IvMathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by lukas on 17.02.14.
 */
public class DrugRenderer implements IDrugRenderer
{
    public ResourceLocation hurtOverlay;
    public ResourceLocation coffeeOverlay;
    public ResourceLocation powerParticle;
    public ResourceLocation[] lightningTextures;

    public float experiencedHealth = 5F;

    public int timeScreenWet;
    public boolean wasInWater;
    public boolean wasInRain;

    public float currentHeat;

    public DrugRenderer()
    {
        hurtOverlay = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "hurtOverlay.png");
        coffeeOverlay = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "coffeeOverlayBlend.png");
        powerParticle = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "powerParticle.png");

        lightningTextures = new ResourceLocation[4];
        for (int i = 0; i < lightningTextures.length; i++)
        {
            lightningTextures[i] = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "lightning" + i + ".png");
        }
        ;
    }

    @Override
    public void update(DrugHelper drugHelper, EntityLivingBase entity)
    {
        if (DrugHelper.hurtOverlayEnabled)
        {
            experiencedHealth = IvMathHelper.nearValue(experiencedHealth, entity.getHealth(), 0.01f, 0.01f);
        }

        Block bID = ActiveRenderInfo.getBlockAtEntityViewpoint(entity.worldObj, entity, 1.0F);
        wasInWater = bID.getMaterial() == Material.water;
        //wasInRain = player.worldObj.getRainStrength(1.0f) > 0.0f && player.worldObj.getPrecipitationHeight(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY)) <= player.posY; //Client can't handle rain

        if (DrugHelper.waterOverlayEnabled)
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

    @Override
    public void distortScreen(float par1, EntityLivingBase entity, int rendererUpdateCount, DrugHelper drugHelper)
    {
        float wobblyness = drugHelper.getDrugValue("Alcohol") * 0.5F + drugHelper.getDrugValue("Cannabis") * 0.02f + drugHelper.getDrugValue("BrownShrooms") * 0.03f + drugHelper.getDrugValue("RedShrooms") * 0.03f;
        if (wobblyness > 1.0F)
        {
            wobblyness = 1.0F;
        }

        if (wobblyness > 0.0F)
        {
            float f4 = 5F / (wobblyness * wobblyness + 5F) - wobblyness * 0.04F;
            f4 *= f4;

            float sin1 = MathHelper.sin(((rendererUpdateCount + par1) / 150 * (float) Math.PI));
            float sin2 = MathHelper.sin(((rendererUpdateCount + par1) / 170 * (float) Math.PI));
            float sin3 = MathHelper.sin(((rendererUpdateCount + par1) / 190 * (float) Math.PI));

            GL11.glRotatef((rendererUpdateCount + par1) * 3F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / (f4 + (wobblyness * sin1) / 2), 1.0F / (f4 + (wobblyness * sin2) / 2), 1.0F / (f4 + (wobblyness * sin3) / 2));
            GL11.glRotatef(-(rendererUpdateCount + par1) * 3F, 0.0F, 1.0F, 1.0F);
        }

        float shiftX = DrugEffectInterpreter.getCameraShiftX(drugHelper, (float) rendererUpdateCount + par1);
        float shiftY = DrugEffectInterpreter.getCameraShiftY(drugHelper, (float) rendererUpdateCount + par1);
        GL11.glTranslatef(shiftX, shiftY, 0.0f);
    }

    @Override
    public void renderOverlays(float partialTicks, EntityLivingBase entity, int updateCounter, int width, int height, DrugHelper drugHelper)
    {
        Tessellator tessellator = Tessellator.instance;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        float alcohol = drugHelper.getDrugValue("Alcohol");
        if (alcohol > 0)
        {
            float d = (MathHelper.sin(updateCounter / 80F) * alcohol * 0.5F + alcohol);
            if (d > 0.8F)
            {
                d = 0.8F;
            }

            renderOverlay(d * 0.25f, width, height, TextureMap.locationBlocksTexture, Blocks.portal.getIcon(0, 0).getMinU(), Blocks.portal.getIcon(0, 0).getMinV(), Blocks.portal.getIcon(0, 0).getMaxU(), Blocks.portal.getIcon(0, 0).getMaxV(), 0);
        }

        float warmth = drugHelper.getDrugValue("Warmth");
        if (warmth > 0)
        {
            renderWarmthOverlay(warmth * 0.5f, width, height, updateCounter);
        }

        float power = drugHelper.getDrugValue("Power");
        Random powerR = new Random(entity.ticksExisted); // 20 changes / sec is alright
        int powerParticles = MathHelper.floor_float(powerR.nextFloat() * 200.0f * power);
        if (powerParticles > 0)
        {
            bindTexture(powerParticle);
            renderRandomParticles(powerParticles, height / 10, MathHelper.ceiling_float_int(height / 10 * power), width, height, powerR);
        }
        Random powerLR = new Random((long) (entity.ticksExisted / 2) * 21124871824l); // Chaos principle doesn't apply ;_;
        float lightningChance = (power - 0.5f) * 0.1f;
        int powerLightnings = 0;
        while (powerLR.nextFloat() < lightningChance && powerLightnings < 3)
        {
            powerLightnings++;
        }
        if (powerLightnings > 0)
        {
            int lightningW = height;

            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE, 1, 0);
            for (int i = 0; i < powerLightnings; i++)
            {
                float lX = powerLR.nextInt(width + lightningW) - lightningW;
                lX += (powerLR.nextFloat() - 0.5f) * lightningW * partialTicks * 2.0f;
                int lIndex = powerLR.nextInt(lightningTextures.length);
                boolean upsideDown = powerLR.nextBoolean();
                float lightningTime = ((entity.ticksExisted % 2) + partialTicks) * 0.5f;

                GL11.glColor4f(1.0f, 1.0f, 1.0f, (0.05f + power * 0.1f) * (1.0f - lightningTime));
                bindTexture(lightningTextures[lIndex]);
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(lX, height, -90.0, 0, upsideDown ? 0 : 1);
                tessellator.addVertexWithUV(lX + lightningW, height, -90.0, 1, upsideDown ? 0 : 1);
                tessellator.addVertexWithUV(lX + lightningW, 0, -90.0, 1, upsideDown ? 1 : 0);
                tessellator.addVertexWithUV(lX, 0, -90.0, 0, upsideDown ? 1 : 0);
                tessellator.draw();
            }
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        }

        if (DrugHelper.hurtOverlayEnabled)
        {
            if (entity.hurtTime > 0 || experiencedHealth < 5F)
            {
                float p1 = (float) entity.hurtTime / (float) entity.maxHurtTime;
                float p2 = +(5f - experiencedHealth) / 6f;

                float p = p1 > 0.0F ? p1 : 0.0f + p2 > 0.0F ? p2 : 0.0F;
                renderOverlay(p, width, height, hurtOverlay, 0.0F, 0.0F, 1.0F, 1.0F, (int) ((1.0F - p) * 40F));
            }
        }

//        if (DrugHelper.waterOverlayEnabled)
//        {
//            if (timeScreenWet > 0 && !wasInWater)
//            {
//                float p = timeScreenWet / 100f;
//                if (p > 1.0F)
//                    p = 1.0F;
//
//                renderOverlay(0.3F * p, width, height, waterOverlay, 0.0F, 0.0F, 1.0F, 1.0F, 0);
//            }
//        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    private void renderOverlay(float alpha, int width, int height, ResourceLocation texture, float x0, float y0, float x1, float y1, int offset)
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

    private void renderWarmthOverlay(float alpha, int width, int height, int ticks)
    {
        bindTexture(coffeeOverlay);
        Tessellator var8 = Tessellator.instance;

        for (int i = 0; i < 3; i++)
        {
            int steps = 30;

            float prevXL = -1;
            float prevXR = -1;
            float prevY = -1;
            boolean init = false;

            for (int y = 0; y < steps; y++)
            {
                float prog = (float) (steps - y) / (float) steps;

                GL11.glColor4f(1.0f, 0.5f + prog * 0.3f, 0.35f + prog * 0.1f, alpha - prog * 0.4f);

                int segWidth = width / 9;

                int xM = (int) (segWidth * (i * 3 + 1.5f));

                float xShift = MathHelper.sin((float) y / (float) steps * 5.0f + ticks / 10.0f);
                float mXL = xM - segWidth + xShift * segWidth * 0.25f;
                float mXR = xM + segWidth + xShift * segWidth * 0.25f;
                float mY = (float) y / (float) steps * height / 7 * 5 + height / 7;

                if (init)
                {
                    var8.startDrawingQuads();
                    var8.addVertexWithUV(mXL, mY, -90.0D, 0, (float) y / (float) steps);
                    var8.addVertexWithUV(mXR, mY, -90.0D, 1, (float) y / (float) steps);
                    var8.addVertexWithUV(prevXR, prevY, -90.0D, 1, (float) (y - 1) / (float) steps);
                    var8.addVertexWithUV(prevXL, prevY, -90.0D, 0, (float) (y - 1) / (float) steps);
                    var8.draw();
                }
                else
                {
                    init = true;
                }

                prevY = mY;
                prevXL = mXL;
                prevXR = mXR;
            }
        }

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

    public void renderRandomParticles(int number, int width, int height, int screenWidth, int screenHeight, Random rand)
    {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        for (int i = 0; i < number; i++)
        {
            int x = rand.nextInt(screenWidth + width) - width;
            int y = rand.nextInt(screenHeight + height) - height;

            tessellator.addVertexWithUV(x, y + height, -90.0, 0, 1);
            tessellator.addVertexWithUV(x + width, y + height, -90.0, 1, 1);
            tessellator.addVertexWithUV(x + width, y, -90.0, 1, 0);
            tessellator.addVertexWithUV(x, y, -90.0, 0, 0);
        }
        tessellator.draw();
    }

    @Override
    public void renderAllHallucinations(float par1, DrugHelper drugHelper)
    {
        for (DrugHallucination h : drugHelper.hallucinations)
        {
            h.render(par1, MathHelper.clamp_float(0.0f, DrugEffectInterpreter.getHallucinationStrength(drugHelper, par1) * 15.0f, 1.0f));
        }
    }

    @Override
    public float getCurrentHeatDistortion()
    {
        if (wasInWater)
        {
            return 0.0f;
        }

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

    private void bindTexture(ResourceLocation resourceLocation)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }
}
