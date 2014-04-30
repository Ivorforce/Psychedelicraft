/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.VillagerRegistry;
import ivorius.psychedelicraft.PSCoreHandlerClient;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.ServerProxy;
import ivorius.psychedelicraft.blocks.TileEntityBarrel;
import ivorius.psychedelicraft.blocks.TileEntityDryingTable;
import ivorius.psychedelicraft.blocks.TileEntityPeyote;
import ivorius.psychedelicraft.blocks.TileEntityRiftJar;
import ivorius.psychedelicraft.client.rendering.*;
import ivorius.psychedelicraft.client.rendering.shaders.DrugShaderHelper;
import ivorius.psychedelicraft.entities.DrugHelper;
import ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import ivorius.psychedelicraft.entities.EntityRealityRift;
import ivorius.psychedelicraft.entities.PSEntityList;
import ivorius.psychedelicraft.items.PSItems;
import ivorius.psychedelicraft.ivToolkit.IvParticleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class ClientProxy extends ServerProxy
{
    @Override
    public void preInit(Configuration config)
    {
        DrugShaderHelper.setShaderEnabled(config.get("Visual", "shaderEnabled", true).getBoolean(true));
        DrugShaderHelper.setShader2DEnabled(config.get("Visual", "shader2DEnabled", true).getBoolean(true));
        DrugShaderHelper.sunFlareIntensity = (float) config.get("Visual", "sunFlareIntensity", 0.25f).getDouble(0.25);
        DrugShaderHelper.doHeatDistortion = config.get("Visual", "biomeHeatDistortion", true).getBoolean(true);
        DrugShaderHelper.doWaterDistortion = config.get("Visual", "waterDistortion", true).getBoolean(true);
//        DrugShaderHelper.doShadows = config.get("Visual", "doShadows", true).getBoolean(true);
        DrugShaderHelper.doShadows = false;

        DrugHelper.waterOverlayEnabled = config.get("Visual", "waterOverlayEnabled", true).getBoolean(true);
        DrugHelper.hurtOverlayEnabled = config.get("Visual", "hurtOverlayEnabled", true).getBoolean(true);
        DrugHelper.digitalEffectPixelRescale = new float[]{(float) config.get("Visual", "digitalEffectPixelRescaleX", 0.05f).getDouble(0.05), (float) config.get("Visual", "digitalEffectPixelRescaleY", 0.05f).getDouble(0.05)};
        DrugShaderHelper.disableDepthBuffer = config.get("Visual", "disableDepthBuffer", false).getBoolean(false);
        DrugShaderHelper.bypassPingPongBuffer = config.get("Visual", "bypassPingPongBuffer", false).getBoolean(false);

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        Psychedelicraft.coreHandlerClient = new PSCoreHandlerClient();
        Psychedelicraft.coreHandlerClient.register();
    }

    @Override
    public void registerRenderers()
    {
        Psychedelicraft.blockWineGrapeLatticeRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(Psychedelicraft.blockWineGrapeLatticeRenderType, new RenderWineGrapeLattice());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new TileEntityRendererBarrel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingTable.class, new TileEntityRendererDryingTable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPeyote.class, new TileEntityRendererPeyote());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRiftJar.class, new TileEntityRendererRiftJar());

        RenderingRegistry.registerEntityRenderingHandler(EntityMolotovCocktail.class, new RenderSnowball(PSItems.molotovCocktail));
        RenderingRegistry.registerEntityRenderingHandler(EntityRealityRift.class, new RenderRealityRift());
        VillagerRegistry.instance().registerVillagerSkin(PSEntityList.villagerDealerProfessionID, new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "villagerDealer.png"));

        DrugShaderHelper.allocate();
        DrugShaderHelper.outputShaderInfo();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.PORTAL)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityLivingBase renderEntity = mc.renderViewEntity;
            DrugHelper drugHelper = DrugHelper.getDrugHelper(renderEntity);

            if (drugHelper != null && drugHelper.drugRenderer != null)
            {
                drugHelper.drugRenderer.renderOverlaysAfterShaders(event.partialTicks, renderEntity, renderEntity.ticksExisted, event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), drugHelper);
            }
        }
    }

    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event)
    {
        if (event.message instanceof ChatComponentText)
        {
            ChatComponentText text = (ChatComponentText) event.message;

            EntityLivingBase renderEntity = Minecraft.getMinecraft().renderViewEntity;
            DrugHelper drugHelper = DrugHelper.getDrugHelper(renderEntity);

            if (drugHelper != null)
            {
                String message = text.getUnformattedTextForChat();
                drugHelper.receiveChatMessage(renderEntity, message);
                String modified = drugHelper.distortIncomingMessage(renderEntity, message);

                event.message = new ChatComponentText(modified);
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            Minecraft mc = Minecraft.getMinecraft();

            if (mc != null && !mc.isGamePaused())
            {
                DrugHelper drugHelper = DrugHelper.getDrugHelper(mc.renderViewEntity);

                if (drugHelper != null)
                {
                    SmoothCameraHelper.instance.update(mc.gameSettings.mouseSensitivity, DrugEffectInterpreter.getSmoothVision(drugHelper));
                }
            }
        }
    }

    @Override
    public void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size)
    {
        EntityFX particle = new EntitySmokeFX(entity.worldObj, entity.posX, entity.posY - 0.15, entity.posZ, direction.xCoord * speed + entity.motionX, direction.yCoord * speed + entity.motionY, direction.zCoord * speed + entity.motionZ, size);
        particle.setRBGColorF(color[0], color[1], color[2]);
        IvParticleHelper.spawnParticle(particle);
    }

    @Override
    public void createDrugRender(DrugHelper drugHelper)
    {
        drugHelper.drugRenderer = new DrugRenderer();
    }
}
