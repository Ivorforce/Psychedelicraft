/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs.effects;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.DrugRenderer;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugWarmth extends DrugSimple
{
    public ResourceLocation coffeeOverlay;

    public DrugWarmth(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus, true);
        coffeeOverlay = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "coffeeOverlayBlend.png");
    }

    @Override
    public float bloomHallucinationStrength()
    {
        return (float)getActiveValue() * 0.5f;
    }

    @Override
    public float superSaturationHallucinationStrength()
    {
        return (float)getActiveValue() * 0.1f;
    }

    @Override
    public void drawOverlays(float partialTicks, EntityLivingBase entity, int updateCounter, int width, int height, DrugProperties drugProperties)
    {
        float warmth = (float)getActiveValue();
        if (warmth > 0)
            renderWarmthOverlay(warmth * 0.5f, width, height, updateCounter);
    }

    private void renderWarmthOverlay(float alpha, int width, int height, int ticks)
    {
        DrugRenderer.bindTexture(coffeeOverlay);
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
}
