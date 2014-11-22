/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import ivorius.ivtoolkit.rendering.IvParticleHelper;
import ivorius.psychedelicraft.PSProxy;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.*;
import ivorius.psychedelicraft.client.rendering.DrugRenderer;
import ivorius.psychedelicraft.client.rendering.ItemRendererModelCustom;
import ivorius.psychedelicraft.client.rendering.ItemRendererThatMakesFuckingSense;
import ivorius.psychedelicraft.client.rendering.RenderRealityRift;
import ivorius.psychedelicraft.client.rendering.blocks.*;
import ivorius.psychedelicraft.client.rendering.shaders.DrugShaderHelper;
import ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import ivorius.psychedelicraft.entities.EntityRealityRift;
import ivorius.psychedelicraft.entities.PSEntityList;
import ivorius.psychedelicraft.entities.drugs.DrugFactory;
import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import ivorius.psychedelicraft.entities.drugs.DrugRegistry;
import ivorius.psychedelicraft.entities.drugs.effects.*;
import ivorius.psychedelicraft.events.PSCoreHandlerClient;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.ArrayList;
import java.util.List;

import static ivorius.psychedelicraft.Psychedelicraft.*;
import static ivorius.psychedelicraft.config.PSConfig.CATEGORY_AUDIO;
import static ivorius.psychedelicraft.config.PSConfig.CATEGORY_VISUAL;
import static ivorius.psychedelicraft.config.PSConfig.readHasBGM;

public class ClientProxy implements PSProxy
{
    public static float dofFocalPointNear;
    public static float dofFocalBlurNear;
    public static float dofFocalPointFar;
    public static float dofFocalBlurFar;

    @Override
    public void preInit()
    {
        Psychedelicraft.coreHandlerClient = new PSCoreHandlerClient();
        Psychedelicraft.coreHandlerClient.register();
    }

    @Override
    public void registerRenderers()
    {
        Psychedelicraft.blockWineGrapeLatticeRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(Psychedelicraft.blockWineGrapeLatticeRenderType, new RenderWineGrapeLattice());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDistillery.class, new TileEntityRendererDistillery());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlask.class, new TileEntityRendererFlask());

        MinecraftForgeClient.registerItemRenderer(PSItems.bottle, new ItemRendererThatMakesFuckingSense());
        MinecraftForgeClient.registerItemRenderer(PSItems.molotovCocktail, new ItemRendererThatMakesFuckingSense());
        MinecraftForgeClient.registerItemRenderer(PSItems.woodenMug, new ItemRendererThatMakesFuckingSense());
        MinecraftForgeClient.registerItemRenderer(PSItems.stoneCup, new ItemRendererThatMakesFuckingSense());
        MinecraftForgeClient.registerItemRenderer(PSItems.glassChalice, new ItemRendererThatMakesFuckingSense());
        MinecraftForgeClient.registerItemRenderer(PSItems.syringe, new ItemRendererThatMakesFuckingSense());

        MinecraftForgeClient.registerItemRenderer(PSItems.itemMashTub, new ItemRendererModelCustom(new ItemRendererModelCustom.ItemModelRendererSimple(TileEntityRendererMashTub.modelWoodenVat), new ResourceLocation(MODID, filePathTextures + "woodenVat.png"), 2.0f, new float[]{0f, -1.5f, 0f}, new float[]{0.0f, 0.0f, 0.0f}));
        MinecraftForgeClient.registerItemRenderer(PSItems.itemDistillery, new ItemRendererModelCustom(new ItemRendererModelCustom.ItemModelRendererSimple(TileEntityRendererDistillery.modelDistillery), new ResourceLocation(MODID, filePathTextures + "distillery.png"), 0.75f, new float[]{-.5f, -1.8f, .5f}, new float[]{0.0f, 0.0f, 0.0f}));
        MinecraftForgeClient.registerItemRenderer(PSItems.itemFlask, new ItemRendererModelCustom(new ItemRendererModelCustom.ItemModelRendererSimple(TileEntityRendererFlask.modelFlask), new ResourceLocation(MODID, filePathTextures + "flask.png"), 0.75f, new float[]{0f, -1.8f, 0f}, new float[]{0.0f, 0.0f, 0.0f}));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(PSBlocks.bottleRack), new ItemRendererModelCustom(new ItemRendererModelCustom.ItemModelRendererSimple(TileEntityRendererBottleRack.modelBottleRack), new ResourceLocation(MODID, filePathTextures + "wineRack.png"), 1f, new float[]{0f, -1.8f, 0.3f}, new float[]{0.0f, 0.0f, 0.0f}));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMashTub.class, new TileEntityRendererMashTub());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new TileEntityRendererBarrel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingTable.class, new TileEntityRendererDryingTable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPeyote.class, new TileEntityRendererPeyote());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRiftJar.class, new TileEntityRendererRiftJar());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBottleRack.class, new TileEntityRendererBottleRack());

        RenderingRegistry.registerEntityRenderingHandler(EntityMolotovCocktail.class, new RenderSnowball(PSItems.molotovCocktail));
        RenderingRegistry.registerEntityRenderingHandler(EntityRealityRift.class, new RenderRealityRift());

        if (PSEntityList.villagerDealerProfessionID >= 0)
            VillagerRegistry.instance().registerVillagerSkin(PSEntityList.villagerDealerProfessionID, new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "villagerDealer.png"));

        DrugShaderHelper.allocate();
        DrugShaderHelper.outputShaderInfo();
    }

    @Override
    public void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size)
    {
        EntityFX particle = new EntitySmokeFX(entity.worldObj, entity.posX, entity.posY - 0.15, entity.posZ, direction.xCoord * speed + entity.motionX, direction.yCoord * speed + entity.motionY, direction.zCoord * speed + entity.motionZ, size);
        particle.setRBGColorF(color[0], color[1], color[2]);
        IvParticleHelper.spawnParticle(particle);
    }

    @Override
    public void createDrugRenderer(DrugHelper drugHelper)
    {
        drugHelper.drugRenderer = new DrugRenderer();
    }

    @Override
    public void loadConfig(String configID)
    {
        if (configID == null || configID.equals(CATEGORY_VISUAL))
        {
            DrugShaderHelper.setShaderEnabled(config.get(CATEGORY_VISUAL, "shaderEnabled", true).getBoolean());
            DrugShaderHelper.setShader2DEnabled(config.get(CATEGORY_VISUAL, "shader2DEnabled", true).getBoolean());
            DrugShaderHelper.sunFlareIntensity = (float) config.get(CATEGORY_VISUAL, "sunFlareIntensity", 0.25).getDouble();
            DrugShaderHelper.doHeatDistortion = config.get(CATEGORY_VISUAL, "biomeHeatDistortion", true).getBoolean();
            DrugShaderHelper.doWaterDistortion = config.get(CATEGORY_VISUAL, "waterDistortion", true).getBoolean();
            DrugShaderHelper.doMotionBlur = config.get(CATEGORY_VISUAL, "motionBlur", true).getBoolean();
//        DrugShaderHelper.doShadows = config.get(CATEGORY_VISUAL, "doShadows", true).getBoolean(true);
            DrugShaderHelper.doShadows = false;

            dofFocalPointNear = (float) config.get(CATEGORY_VISUAL, "dofFocalPointNear", 0f, "The point at which DoF starts blurring the screen, towards the player, in blocks.").getDouble();
            dofFocalPointFar = (float) config.get(CATEGORY_VISUAL, "dofFocalPointFar", 128f, "The point at which DoF starts blurring the screen, away from the player, in blocks.").getDouble();
            dofFocalBlurNear = (float) config.get(CATEGORY_VISUAL, "dofFocalBlurNear", 0f, "The strength of DoF blur towards the player.").getDouble();
            dofFocalBlurFar = (float) config.get(CATEGORY_VISUAL, "dofFocalBlurFar", 1f, "The strength of DoF blur away from the player.").getDouble();

            DrugHelper.waterOverlayEnabled = config.get(CATEGORY_VISUAL, "waterOverlayEnabled", true).getBoolean();
            DrugHelper.hurtOverlayEnabled = config.get(CATEGORY_VISUAL, "hurtOverlayEnabled", true).getBoolean();
            DrugHelper.digitalEffectPixelRescale = new float[]{(float) config.get(CATEGORY_VISUAL, "digitalEffectPixelRescaleX", 0.05).getDouble(),
                    (float) config.get(CATEGORY_VISUAL, "digitalEffectPixelRescaleY", 0.05).getDouble()};
            DrugShaderHelper.disableDepthBuffer = config.get(CATEGORY_VISUAL, "disableDepthBuffer", false).getBoolean();
            DrugShaderHelper.bypassPingPongBuffer = config.get(CATEGORY_VISUAL, "bypassPingPongBuffer", false).getBoolean();
        }

        if (configID == null || configID.equals(CATEGORY_AUDIO))
        {
            List<String> names = new ArrayList<>();
            for (DrugFactory factory : DrugRegistry.allFactories())
                factory.addManagedDrugNames(names);

            for (String s : names)
                readHasBGM(s, config);
        }
    }
}
