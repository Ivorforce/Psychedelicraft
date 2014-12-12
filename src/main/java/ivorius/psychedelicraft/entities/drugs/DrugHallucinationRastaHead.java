/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.ModelRastaHead;
import ivorius.psychedelicraft.client.rendering.shaders.PSRenderStates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class DrugHallucinationRastaHead extends DrugHallucination
{
    public int entityMaxTicks;

    public EntityLiving dummyEntity;
    public EntityLookHelper lookHelper;

    public ModelBase modelRastaHead;

    ResourceLocation rastaHeadTexture;

    public DrugHallucinationRastaHead(EntityPlayer playerEntity)
    {
        super(playerEntity);

        this.entityMaxTicks = (playerEntity.getRNG().nextInt(59) + 120) * 20;

        this.dummyEntity = new EntityPig(playerEntity.worldObj);
        this.dummyEntity.posX = playerEntity.posX;
        this.dummyEntity.posY = playerEntity.posY;
        this.dummyEntity.posZ = playerEntity.posZ;

        this.lookHelper = new EntityLookHelper(dummyEntity);

        this.modelRastaHead = new ModelRastaHead();

//        this.chatBot = new ChatBotRastahead(playerEntity.getRNG(), playerEntity);

        rastaHeadTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "rastaHeadTexture.png");
    }

    @Override
    public void update()
    {
        super.update();

        this.dummyEntity.lastTickPosX = this.dummyEntity.posX;
        this.dummyEntity.lastTickPosY = this.dummyEntity.posY;
        this.dummyEntity.lastTickPosZ = this.dummyEntity.posZ;

        this.dummyEntity.prevRotationYawHead = this.dummyEntity.rotationYawHead;
        this.dummyEntity.prevRotationPitch = this.dummyEntity.rotationPitch;

        this.lookHelper.setLookPositionWithEntity(playerEntity, 3.0f, 3.0f);
        this.lookHelper.onUpdateLook();

        double wantedX = playerEntity.posX + MathHelper.sin(playerEntity.ticksExisted / 50.0f) * 5.0f;
        double wantedY = playerEntity.posY;
        double wantedZ = playerEntity.posZ + MathHelper.cos(playerEntity.ticksExisted / 50.0f) * 5.0f;

        double totalDist = MathHelper.sqrt_double((wantedX - dummyEntity.posX) * (wantedX - dummyEntity.posX) + (wantedX - dummyEntity.posX) * (wantedX - dummyEntity.posX) + (wantedX - dummyEntity.posX) * (wantedX - dummyEntity.posX));

        if (totalDist > 3.0f)
        {
            double nX = (wantedX - dummyEntity.posX) / totalDist;
            double nY = (wantedY - dummyEntity.posY) / totalDist;
            double nZ = (wantedZ - dummyEntity.posZ) / totalDist;

            dummyEntity.motionX = nX * 0.05;
            dummyEntity.motionY = nY * 0.05;
            dummyEntity.motionZ = nZ * 0.05;
        }
        else
        {
            dummyEntity.motionX *= 0.9;
            dummyEntity.motionY *= 0.9;
            dummyEntity.motionZ *= 0.9;
        }

        this.dummyEntity.posX += this.dummyEntity.motionX;
        this.dummyEntity.posY += this.dummyEntity.motionY;
        this.dummyEntity.posZ += this.dummyEntity.motionZ;

        if (this.dummyEntity instanceof EntityLiving)
        {
            EntityLiving entityliving = this.dummyEntity;

            double var9 = this.dummyEntity.posX - this.dummyEntity.prevPosX;
            double var12 = this.dummyEntity.posZ - this.dummyEntity.prevPosZ;
            float var11 = MathHelper.sqrt_double(var9 * var9 + var12 * var12) * 4.0F;

            if (var11 > 1.0F)
            {
                var11 = 1.0F;
            }

            entityliving.limbSwingAmount += (var11 / 3f - entityliving.limbSwingAmount) * 0.4F;
            entityliving.limbSwing += entityliving.limbSwingAmount;
        }
    }

    @Override
    public boolean isDead()
    {
        return this.entityTicksAlive >= this.entityMaxTicks;
    }

    @Override
    public void render(float par1, float dAlpha)
    {
        float alpha = MathHelper.sin((float) Math.min(this.entityTicksAlive, this.entityMaxTicks - 2) / (float) (this.entityMaxTicks - 2) * 3.1415f) * 18f;

        if (alpha > 1.0f)
        {
            alpha = 1.0f;
        }
        if (alpha > 0.0f)
        {
            double var3 = this.dummyEntity.lastTickPosX + (this.dummyEntity.posX - this.dummyEntity.lastTickPosX) * par1;
            double var5 = this.dummyEntity.lastTickPosY + (this.dummyEntity.posY - this.dummyEntity.lastTickPosY) * par1;
            double var7 = this.dummyEntity.lastTickPosZ + (this.dummyEntity.posZ - this.dummyEntity.lastTickPosZ) * par1;
            float var9 = -(this.dummyEntity.prevRotationYawHead + (this.dummyEntity.rotationYawHead - this.dummyEntity.prevRotationYawHead) * par1);
            float pitch = this.dummyEntity.prevRotationPitch + (this.dummyEntity.rotationPitch - this.dummyEntity.prevRotationPitch) * par1;

            GL11.glPushMatrix();

            OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
            GL11.glEnable(GL11.GL_BLEND);
            PSRenderStates.setTexture2DEnabled(OpenGlHelper.lightmapTexUnit, false);
            GL11.glTranslated(var3 - RenderManager.renderPosX, var5 + 1.0 - RenderManager.renderPosY, var7 - RenderManager.renderPosZ);
            GL11.glRotatef(pitch, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(var9, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);

            Minecraft.getMinecraft().renderEngine.bindTexture(rastaHeadTexture);
            modelRastaHead.render(dummyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

            PSRenderStates.setTexture2DEnabled(OpenGlHelper.lightmapTexUnit, true);
            PSRenderStates.setOverrideColor((float[]) null);

            GL11.glPopMatrix();
        }
    }

    @Override
    public int getMaxHallucinations()
    {
        return -1;
    }
}
