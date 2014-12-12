/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.audio.MovingSoundDrug;
import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.fluids.FluidWithIconSymbolRegistering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by lukas on 18.02.14.
 */
public class PSEventForgeHandler
{
    public void register()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event)
    {
        Object[] args = event.component.getFormatArgs();

        if (args.length > 1)
        {
            DrugProperties drugProperties = DrugProperties.getDrugProperties(event.player);

            if (drugProperties != null)
            {
                String modified = drugProperties.messageDistorter.distortOutgoingMessage(drugProperties, event.player, event.player.getRNG(), event.message);
                args[1] = modified;
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        if (event.message instanceof ChatComponentText)
        {
            ChatComponentText text = (ChatComponentText) event.message;

            EntityLivingBase renderEntity = Minecraft.getMinecraft().renderViewEntity;
            DrugProperties drugProperties = DrugProperties.getDrugProperties(renderEntity);

            if (drugProperties != null)
            {
                String message = text.getUnformattedTextForChat();
                drugProperties.receiveChatMessage(renderEntity, message);
                String modified = drugProperties.messageDistorter.distortIncomingMessage(drugProperties, renderEntity, renderEntity.getRNG(), message);

                event.message = new ChatComponentText(modified);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerSleep(PlayerSleepInBedEvent event)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(event.entityLiving);

        if (drugProperties != null)
        {
            EntityPlayer.EnumStatus status = drugProperties.getDrugSleepStatus();

            if (status != null)
                event.result = status;
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(event.entity); // Initialize drug helper

        if (event.world.isRemote && drugProperties != null)
            initializeMovingSoundDrug(event.entity, drugProperties);
    }

    @SideOnly(Side.CLIENT)
    public void initializeMovingSoundDrug(Entity entity, DrugProperties drugProperties)
    {
        SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
        for (String drugName : drugProperties.getAllDrugNames())
        {
            if (PSConfig.hasBGM(drugName))
                soundHandler.playSound(new MovingSoundDrug(new ResourceLocation(Psychedelicraft.MODID, "drug." + drugName), entity, drugProperties, drugName));
        }
    }

    @SubscribeEvent
    public void onEntityConstruction(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer)
            DrugProperties.initInEntity(event.entity);
    }

    @SubscribeEvent
    public void getBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(event.entity);

        if (drugProperties != null)
        {
            event.newSpeed = event.newSpeed * drugProperties.getDigSpeedModifier(event.entityLiving);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.PORTAL)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityLivingBase renderEntity = mc.renderViewEntity;
            DrugProperties drugProperties = DrugProperties.getDrugProperties(renderEntity);

            if (drugProperties != null && drugProperties.renderer != null)
            {
                drugProperties.renderer.renderOverlaysAfterShaders(event.partialTicks, renderEntity, renderEntity.ticksExisted, event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), drugProperties);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitchPre(TextureStitchEvent.Pre event)
    {
        IIconRegister iconRegister = event.map;
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
        {
            if (fluid instanceof FluidWithIconSymbolRegistering)
                ((FluidWithIconSymbolRegistering) fluid).registerIcons(iconRegister, event.map.getTextureType());
        }
    }
}
