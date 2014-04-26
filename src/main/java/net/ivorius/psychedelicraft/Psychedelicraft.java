/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package net.ivorius.psychedelicraft;

import net.ivorius.psychedelicraft.blocks.PSBlocks;
import net.ivorius.psychedelicraft.entities.*;
import net.ivorius.psychedelicraft.gui.PsycheGuiHandler;
import net.ivorius.psychedelicraft.items.*;
import net.ivorius.psychedelicraft.ivToolkit.IvPacketPipeline;
import net.ivorius.psychedelicraft.worldgen.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Psychedelicraft.MODID, version = Psychedelicraft.VERSION)
public class Psychedelicraft
{
    public static final String MODID = "psychedelicraft";
    public static final String VERSION = "1.4";

    @Instance(value = "psychedelicraft")
    public static Psychedelicraft instance;

    @SidedProxy(clientSide = "net.ivorius.psychedelicraft.client.ClientProxy", serverSide = "net.ivorius.psychedelicraft.ServerProxy")
    public static ServerProxy proxy;

    public static Logger logger;

    public static PsycheGuiHandler guiHandler;
    public static PSEventHandler eventHandler;
    public static PSCommunicationHandler communicationHandler;

    public static PSCoreHandlerClient coreHandlerClient;
    public static PSCoreHandlerCommon coreHandlerCommon;
    public static PSCoreHandlerServer coreHandlerServer;

    public static IvPacketPipeline packetPipeline;
    
    public static CreativeTabPsyche creativeTab;

    public static String filePathTexturesFull = "psychedelicraft:textures/mod/";
    public static String filePathTextures = "textures/mod/";
    public static String filePathOther = "other/";
    public static String filePathShaders = "shaders/";
    public static String textureBase = "psychedelicraft:";
    public static String soundBase = "psychedelicraft:";
    public static String otherBase = "psychedelicraft:";
    public static String shaderBase = "psychedelicraft:";

    public static boolean spawnRifts;

    public static EntityPlayer.EnumStatus sleepStatusDrugs;

    public static int blockWineGrapeLatticeRenderType;

    public static int dryingTableContainerID;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        PSEntityList.villagerDealerProfessionID = config.get("General", "villagerDealerProfessionID", 87).getInt();

        spawnRifts = config.get("Balancing", "spawnRifts", true).getBoolean(true);

        creativeTab = new CreativeTabPsyche("psychedelicraft");

        guiHandler = new PsycheGuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);

        eventHandler = new PSEventHandler();
        eventHandler.register();

        communicationHandler = new PSCommunicationHandler();

        coreHandlerCommon = new PSCoreHandlerCommon();
        coreHandlerCommon.register();

        packetPipeline = new IvPacketPipeline();
        packetPipeline.registerPacket(DrugInfoPacket.class);

        sleepStatusDrugs = EnumHelper.addStatus("onDrugs");

        PSBlocks.preInit(event, this, config);
        PSItems.preInit(event, this, config);
        PSEntityList.preInit(event, this, config);

        PSBlocks.preInitEnd(event, this, config);
        PSItems.preInitEnd(event, this, config);
        PSEntityList.preInitEnd(event, this, config);

        proxy.preInit(config);

        config.save();
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        proxy.registerRenderers();

        creativeTab.tabIcon = PSItems.itemCannabisLeaf;

        DrugInfluence.registerInfluence(DrugInfluence.class, "default");
        DrugInfluence.registerInfluence(DrugInfluenceHarmonium.class, "harmonium");

        addCrafting();
        PSWorldGen.initWorldGen();

        dryingTableContainerID = guiHandler.acquireUniqueContainerID();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }

    private void addCrafting()
    {
//        GameRegistry.addRecipe(new ItemStack(itemSyringe), "I", "#", 'I', Items.iron_ingot, '#', Blocks.glass);
//        GameRegistry.addRecipe(new ItemStack(itemPipe), "  I", " S ", "WS ", 'I', Items.iron_ingot, 'S', Items.stick, 'W', Blocks.planks);
//
//        GameRegistry.addRecipe(new ItemStack(itemGlassChalice, 4), "# #", " # ", " # ", '#', Blocks.glass);
//        GameRegistry.addRecipe(new ItemStack(blockBarrel, 1, 1), "###", "###", "W#W", '#', itemWineGrapes, 'W', Blocks.planks);
//        GameRegistry.addRecipe(new ItemStack(blockWineGrapeLattice), "###", "###", "O#O", '#', Items.stick, 'O', Blocks.planks);
//
//        for (int i = 0; i < 16; i++)
//        {
//            GameRegistry.addRecipe(new ItemStack(itemMolotovCocktail, 1, i), "#", "W", '#', Items.paper, 'W', new ItemStack(itemGlassChalice, 1, i + 1));
//        }
//
//        GameRegistry.addRecipe(new ItemStack(itemWoodenMug, 8), "# #", "# #", "###", '#', Blocks.planks);
//        GameRegistry.addRecipe(new ItemStack(blockBarrel, 1, 0), "###", "###", "WUW", '#', Items.wheat, 'U', Items.water_bucket, 'W', Blocks.planks);
//
//        GameRegistry.addRecipe(new ItemStack(blockDryingTable), "###", "#R#", '#', Blocks.planks, 'R', Items.redstone);
//
//        TileEntityDryingTable.addDryingResult(itemCannabisLeaf, new ItemStack(itemDriedCannabisLeaves, 3));
//        TileEntityDryingTable.addDryingResult(itemCannabisBuds, new ItemStack(itemDriedCannabisBuds, 3));
//        GameRegistry.addRecipe(new ItemStack(itemHashMuffin), "LLL", "#X#", "LLL", 'X', new ItemStack(Items.dye, 1, 3), '#', Items.wheat, 'L', itemDriedCannabisLeaves);
//        GameRegistry.addRecipe(new ItemStack(itemJoint), "P", "C", "P", 'P', Items.paper, 'C', itemDriedCannabisBuds);
//        itemPipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(itemDriedCannabisBuds), new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.25f)}));
//
//        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(Blocks.brown_mushroom), new ItemStack(itemMagicMushroomsBrown, 3));
//        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(Blocks.red_mushroom), new ItemStack(itemMagicMushroomsRed, 3));
//
//        GameRegistry.addRecipe(new ItemStack(itemCigarette, 4), "P", "T", "P", 'P', Items.paper, 'T', itemDriedTobacco);
//        GameRegistry.addRecipe(new ItemStack(itemCigar), "TTT", "TTT", "PPP", 'P', Items.paper, 'T', itemDriedTobacco);
//        TileEntityDryingTable.addDryingResult(itemTobaccoLeaf, new ItemStack(itemDriedTobacco, 3));
//        itemPipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(itemDriedTobacco), new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.8f)}));
//
//        GameRegistry.addShapelessRecipe(new ItemStack(itemSyringe, 1, itemSyringeCocaineDamage), new ItemStack(Items.water_bucket), new ItemStack(itemSyringe), new ItemStack(itemDriedCocaLeaves));
//        TileEntityDryingTable.addDryingResult(itemCocaLeaf, new ItemStack(itemDriedCocaLeaves, 3));
//
//        GameRegistry.addRecipe(new ItemStack(Blocks.planks, 4, 1), "#", '#', blockPsycheLog);
//        GameRegistry.addRecipe(new ItemStack(blockBarrel, 1, 2), "JSJ", "AGA", "WUW", 'J', itemJuniperBerries, 'S', Items.sugar, 'A', itemWineGrapes, 'G', Items.wheat, 'U', Items.water_bucket, 'W', Blocks.planks);
//
//        GameRegistry.addSmelting(itemCoffeaCherries, new ItemStack(itemCoffeeBeans), 0.2f);
//
//        GameRegistry.addShapelessRecipe(new ItemStack(itemColdCoffee), new ItemStack(Items.water_bucket), new ItemStack(itemWoodenMug, 1, 0), new ItemStack(itemCoffeeBeans), new ItemStack(itemCoffeeBeans));
//        GameRegistry.addSmelting(itemColdCoffee, new ItemStack(itemWoodenMug, 1, 3), 0.2f);
//        GameRegistry.addShapelessRecipe(new ItemStack(itemSyringe, 1, itemSyringeCaffeineDamage), new ItemStack(Items.water_bucket), new ItemStack(itemSyringe), new ItemStack(itemCoffeeBeans), new ItemStack(itemCoffeeBeans));
//
//        GameRegistry.addRecipe(new ItemStack(itemWoodenBowlDrug, 1, 0), "P", "P", "B", 'P', itemDriedPeyote, 'B', Items.bowl);
//        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(blockPeyote), new ItemStack(itemDriedPeyote, 3));
//        GameRegistry.addRecipe(new ItemStack(itemPeyoteJoint), "P", "D", "P", 'P', Items.paper, 'D', itemDriedPeyote);
//
//        for (int i = 0; i < 16; i++)
//        {
//            itemPipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(itemHarmonium, 1, 15 - i), new DrugInfluence[]{new DrugInfluenceHarmonium("Harmonium", 0, 0.04, 0.01, 0.65f, EntitySheep.fleeceColorTable[i])}, EntitySheep.fleeceColorTable[i]));
//            GameRegistry.addShapelessRecipe(new ItemStack(itemHarmonium, 1, i), new ItemStack(Items.dye, 1, i), new ItemStack(Items.glowstone_dust), new ItemStack(itemDriedTobacco));
//        }
//
//        GameRegistry.addRecipe(new ItemStack(blockRiftJar), "O-O", "GO ", "OIO", 'O', Blocks.glass, '-', Blocks.planks, 'G', Items.gold_ingot, 'I', Items.iron_ingot);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new CommandDrug());
        evt.registerServerCommand(new CommandPsyche());
    }

    @EventHandler
    public void initialise(FMLInitializationEvent evt)
    {
        packetPipeline.initialise();
    }

    @EventHandler
    public void postInitialise(FMLPostInitializationEvent evt)
    {
        packetPipeline.postInitialise();
    }

    @EventHandler
    public void onIMCEvent(FMLInterModComms.IMCEvent event)
    {
        // Could be fatal if we don't know the side
//        for (FMLInterModComms.IMCMessage message : event.getMessages())
//        {
//            communicationHandler.onIMCMessage(message, false, false);
//        }
    }
}