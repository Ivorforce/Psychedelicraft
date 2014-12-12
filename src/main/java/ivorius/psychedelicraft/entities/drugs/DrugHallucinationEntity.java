/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities.drugs;

import ivorius.psychedelicraft.client.rendering.shaders.PSRenderStates;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class DrugHallucinationEntity extends DrugHallucination
{
    public static String[] hEntities = new String[]{"Creeper", "Zombie", "Blaze", "Enderman", "Cow", "Sheep", "Pig", "Ozelot", "Wolf", "Silverfish", "Villager", "VillagerGolem", "SnowMan", "EntityHorse"};

    public Entity entity;

    public int entityMaxTicks;
    public float rotationYawPlus;

    public float[] color;

    public float scale;

    public DrugHallucinationEntity(EntityPlayer playerEntity)
    {
        super(playerEntity);

        Random rand = playerEntity.getRNG();

        String entityName = hEntities[playerEntity.worldObj.rand.nextInt(hEntities.length)];

        entity = EntityList.createEntityByName(entityName, playerEntity.worldObj);
        entity.setPosition(playerEntity.posX + rand.nextDouble() * 50D - 25D, playerEntity.posY + rand.nextDouble() * 10D - 5D, playerEntity.posZ + rand.nextDouble() * 50D - 25D);
        entity.motionX = (rand.nextDouble() - 0.5D) / 10D;
        entity.motionY = (rand.nextDouble() - 0.5D) / 10D;
        entity.motionZ = (rand.nextDouble() - 0.5D) / 10D;
        entity.rotationYaw = rand.nextInt(360);

        this.entityMaxTicks = (rand.nextInt(59) + 3) * 20;

        this.rotationYawPlus = rand.nextFloat() * 10f;
        if (rand.nextBoolean())
        {
            this.rotationYawPlus = 0;
        }

        this.color = new float[]{rand.nextFloat(), rand.nextFloat(), rand.nextFloat()};

        this.scale = 1.0f;
        while (rand.nextFloat() < 0.3f)
        {
            scale *= rand.nextFloat() * 2.7f + 0.3f;
        }

        if (scale > 20.0f)
        {
            scale = 20.0f;
        }
    }

    @Override
    public void update()
    {
        super.update();

        this.entity.lastTickPosX = this.entity.posX;
        this.entity.lastTickPosY = this.entity.posY;
        this.entity.lastTickPosZ = this.entity.posZ;

        this.entity.prevRotationYaw = this.entity.rotationYaw;
        this.entity.prevRotationPitch = this.entity.rotationPitch;

        this.entity.posX += this.entity.motionX;
        this.entity.posY += this.entity.motionY;
        this.entity.posZ += this.entity.motionZ;

        this.entity.rotationYaw += this.rotationYawPlus;
        while (this.entity.rotationYaw > 360f)
        {
            this.entity.rotationYaw -= 360;
            this.entity.prevRotationYaw -= 360;
        }
        while (this.entity.rotationYaw < 360f)
        {
            this.entity.rotationYaw += 360;
            this.entity.prevRotationYaw += 360;
        }

        if (this.entity instanceof EntityLivingBase)
        {
            EntityLiving entityliving = (EntityLiving) this.entity;

            double var9 = this.entity.posX - this.entity.prevPosX;
            double var12 = this.entity.posZ - this.entity.prevPosZ;
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
            double var3 = this.entity.lastTickPosX + (this.entity.posX - this.entity.lastTickPosX) * par1;
            double var5 = this.entity.lastTickPosY + (this.entity.posY - this.entity.lastTickPosY) * par1;
            double var7 = this.entity.lastTickPosZ + (this.entity.posZ - this.entity.lastTickPosZ) * par1;
            float var9 = this.entity.prevRotationYaw + (this.entity.rotationYaw - this.entity.prevRotationYaw) * par1;

            GL11.glPushMatrix();

            OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
            GL11.glEnable(GL11.GL_BLEND);
            PSRenderStates.setOverrideColor(color[0], color[1], color[2], alpha * dAlpha);
            PSRenderStates.setTexture2DEnabled(OpenGlHelper.lightmapTexUnit, false);

            Render var10 = RenderManager.instance.getEntityRenderObject(this.entity);
            GL11.glTranslated(var3 - RenderManager.renderPosX, var5 - RenderManager.renderPosY, var7 - RenderManager.renderPosZ);
            GL11.glRotatef(var9, 0, 1, 0);

            GL11.glScaled(scale, scale, scale);
            var10.doRender(this.entity, 0.0F, 0.0F, 0.0F, 0.0F, 1.0f);

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

    @Override
    public void receiveChatMessage(String message, EntityLivingBase player)
    {

    }
}
