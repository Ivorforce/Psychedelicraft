/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.ivorius.psychedelicraft.PSCoreHandlerClient;
import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.ServerProxy;
import net.ivorius.psychedelicraft.blocks.TileEntityBarrel;
import net.ivorius.psychedelicraft.blocks.TileEntityDryingTable;
import net.ivorius.psychedelicraft.blocks.TileEntityPeyote;
import net.ivorius.psychedelicraft.blocks.TileEntityRiftJar;
import net.ivorius.psychedelicraft.client.rendering.*;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import net.ivorius.psychedelicraft.entities.EntityRealityRift;
import net.ivorius.psychedelicraft.entities.PSEntityList;
import net.ivorius.psychedelicraft.items.PSItems;
import net.ivorius.psychedelicraft.ivToolkit.IvParticleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

        DrugHelper.waterOverlayEnabled = config.get("Visual", "waterOverlayEnabled", true).getBoolean(true);
        DrugHelper.hurtOverlayEnabled = config.get("Visual", "hurtOverlayEnabled", true).getBoolean(true);
        DrugHelper.digitalEffectPixelRescale = new float[]{(float) config.get("Visual", "digitalEffectPixelRescaleX", 0.05f).getDouble(0.05), (float) config.get("Visual", "digitalEffectPixelRescaleY", 0.05f).getDouble(0.05)};
        DrugShaderHelper.bypassFramebuffers = config.get("Visual", "bypassFramebuffers", false).getBoolean(false);

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

        RenderingRegistry.registerEntityRenderingHandler(EntityMolotovCocktail.class, new RenderSnowball(PSItems.itemMolotovCocktail));
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
            EntityPlayer player = mc.thePlayer;
            DrugHelper.getDrugHelper(player).renderOverlays(event.partialTicks, player, (int) mc.ingameGUI.getUpdateCounter(), event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
        }
    }

    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event)
    {
        if (event.message instanceof ChatComponentText)
        {
            ChatComponentText text = (ChatComponentText) event.message;

            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            DrugHelper drugHelper = DrugHelper.getDrugHelper(player);

            if (drugHelper != null)
            {
                String message = text.getUnformattedTextForChat();
                drugHelper.receiveChatMessage(player, message);
                String modified = drugHelper.distortIncomingMessage(player, message);

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
                EntityPlayer player = mc.thePlayer;

                if (player != null)
                {
                    SmoothCameraHelper.instance.update(mc.gameSettings.mouseSensitivity, DrugEffectInterpreter.getSmoothVision(DrugHelper.getDrugHelper(player)));
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
