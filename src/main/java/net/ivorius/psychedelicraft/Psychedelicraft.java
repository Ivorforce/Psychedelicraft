/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package net.ivorius.psychedelicraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.ivorius.psychedelicraft.blocks.*;
import net.ivorius.psychedelicraft.entities.*;
import net.ivorius.psychedelicraft.gui.PsycheGuiHandler;
import net.ivorius.psychedelicraft.items.*;
import net.ivorius.psychedelicraft.toolkit.IvPacketPipeline;
import net.ivorius.psychedelicraft.worldgen.GeneratorGeneric;
import net.ivorius.psychedelicraft.worldgen.WorldGenJuniperTrees;
import net.ivorius.psychedelicraft.worldgen.WorldGenPeyote;
import net.ivorius.psychedelicraft.worldgen.WorldGenTilledPatch;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.Logger;

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
    public static PsycheEventHandler eventHandler;
    public static PsycheCommunicationHandler communicationHandler;

    public static PsycheCoreHandlerClient coreHandlerClient;
    public static PsycheCoreHandlerCommon coreHandlerCommon;
    public static PsycheCoreHandlerServer coreHandlerServer;

    public static IvPacketPipeline packetPipeline;

    public static final byte tagCompoundID = 10;
    public static final byte tagStringID = 8;

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

    public static Item itemGlassChalice;
    public static Item itemWineGrapes;

    public static Block blockWineGrapeLattice;
    public static int blockWineGrapeLatticeRenderType;

    public static Item itemMolotovCocktail;

    public static Item itemWoodenMug;
    public static Block blockBarrel;

    public static Block blockDryingTable;

    public static Block blockCannabisPlant;
    public static Item itemCannabisSeeds;
    public static Item itemCannabisLeaf;
    public static Item itemCannabisBuds;
    public static Item itemDriedCannabisBuds;
    public static Item itemDriedCannabisLeaves;
    public static ItemSmokingPipe itemPipe;
    public static Item itemHashMuffin;

    public static int dryingTableContainerID;

    public static Item itemMagicMushroomsBrown;
    public static Item itemMagicMushroomsRed;

    public static Item itemTobaccoLeaf;
    public static Item itemTobaccoSeeds;
    public static Item itemDriedTobacco;
    public static Item itemCigarette;
    public static Item itemCigar;
    public static Item itemJoint;
    public static Block blockTobaccoPlant;

    public static Block blockCocaPlant;
    public static Item itemCocaSeeds;
    public static Item itemCocaLeaf;
    public static Item itemDriedCocaLeaves;

    public static ItemSyringe itemSyringe;
    public static int itemSyringeCocaineDamage;
    public static int itemSyringeCaffeineDamage;

    public static ItemWoodenBowlDrug itemWoodenBowlDrug;
    public static int itemWoodenBowlPeyoteDamage;

    public static Block blockPsycheLeaves;
    public static Block blockPsycheLog;
    public static Block blockJuniperSapling;
    public static Item itemJuniperBerries;

    public static int villagerDealerProfessionID;

    public static Block blockCoffea;
    public static Item itemCoffeaCherries;
    public static Item itemCoffeeBeans;
    public static Item itemColdCoffee;

    public static Block blockPeyote;
    public static Item itemDriedPeyote;
    public static Item itemPeyoteJoint;

    public static Block blockRiftJar;
    public static Block blockGlitched;

    public static Item itemHarmonium;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        villagerDealerProfessionID = config.get("General", "villagerDealerProfessionID", 87).getInt();

        spawnRifts = config.get("Balancing", "spawnRifts", true).getBoolean(true);


        guiHandler = new PsycheGuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);

        eventHandler = new PsycheEventHandler();
        eventHandler.register();

        communicationHandler = new PsycheCommunicationHandler();

        coreHandlerCommon = new PsycheCoreHandlerCommon();
        coreHandlerCommon.register();

        packetPipeline = new IvPacketPipeline();
        packetPipeline.registerPacket(DrugInfoPacket.class);

        sleepStatusDrugs = EnumHelper.addStatus("onDrugs");

        //----------------------------------------------------------Containers----------------------------------

        itemSyringe = (ItemSyringe) new ItemSyringe().setUnlocalizedName("syringe").setTextureName(textureBase + "syringe");
        GameRegistry.registerItem(itemSyringe, "syringe", MODID);
        itemSyringe.setCreativeTab(CreativeTabs.tabMisc);

        itemWoodenBowlDrug = (ItemWoodenBowlDrug) new ItemWoodenBowlDrug().setUnlocalizedName("woodenBowlDrug").setTextureName(textureBase + "woodenBowl");
        GameRegistry.registerItem(itemWoodenBowlDrug, "psycheWoodenBowl", MODID);
        itemWoodenBowlDrug.setCreativeTab(CreativeTabs.tabFood);

        itemWoodenMug = new ItemWoodenMug().setUnlocalizedName("woodenMug").setTextureName(textureBase + "woodenMug");
        GameRegistry.registerItem(itemWoodenMug, "woodenMug", MODID);
        itemWoodenMug.setCreativeTab(CreativeTabs.tabFood);

        itemPipe = (ItemSmokingPipe) (new ItemSmokingPipe().setUnlocalizedName("smokingPipe").setTextureName(textureBase + "smokingPipe"));
        GameRegistry.registerItem(itemPipe, "smokingPipe", MODID);
        itemPipe.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Barrel----------------------------------

        blockBarrel = new BlockBarrel().setBlockName("barrel").setBlockTextureName(Psychedelicraft.textureBase + "barrel").setHardness(1.0F);
        GameRegistry.registerBlock(blockBarrel, ItemBarrel.class, "barrel", MODID);
        blockBarrel.setCreativeTab(CreativeTabs.tabDecorations);

        GameRegistry.registerTileEntity(TileEntityBarrel.class, "barrel");

        //----------------------------------------------------------Wine----------------------------------

        itemGlassChalice = new ItemGlassChalice().setUnlocalizedName("glassChalice").setTextureName(textureBase + "glassChalice");
        GameRegistry.registerItem(itemGlassChalice, "glassChalice", MODID);
        itemGlassChalice.setCreativeTab(CreativeTabs.tabFood);

        itemWineGrapes = (new ItemWineGrapes(1, 0.5F, true)).setUnlocalizedName("wineGrapes").setTextureName(textureBase + "wineGrapes");
        GameRegistry.registerItem(itemWineGrapes, "wineGrapes", MODID);
        itemWineGrapes.setCreativeTab(CreativeTabs.tabFood);

        blockWineGrapeLattice = new BlockWineGrapeLattice().setBlockName("wineGrapeLattice").setBlockTextureName(Psychedelicraft.textureBase + "wineGrapeLattice").setHardness(0.3F);
        GameRegistry.registerBlock(blockWineGrapeLattice, ItemBlock.class, "wineGrapeLattice", MODID);
        blockWineGrapeLattice.setCreativeTab(CreativeTabs.tabDecorations);

        //----------------------------------------------------------Molotov Cocktail----------------------------------

        int entityMolotovCocktailID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID);
        EntityRegistry.registerModEntity(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID, this, 64, 10, true);

        itemMolotovCocktail = new ItemMolotovCocktail().setUnlocalizedName("molotovCocktail").setTextureName(textureBase + "molotovCocktail");
        GameRegistry.registerItem(itemMolotovCocktail, "molotovCocktail", MODID);
        itemMolotovCocktail.setCreativeTab(CreativeTabs.tabCombat);

        //----------------------------------------------------------Beer----------------------------------

        //----------------------------------------------------------Weed----------------------------------

        blockCannabisPlant = new BlockCannabisPlant().setBlockName("cannabisPlant").setBlockTextureName(textureBase + "cannabisPlant").setHardness(0.5f);
        GameRegistry.registerBlock(blockCannabisPlant, ItemBlock.class, "cannabisPlant", MODID);
        blockCannabisPlant.setCreativeTab(null);

        itemCannabisSeeds = new ItemSeeds(blockCannabisPlant, Blocks.farmland).setUnlocalizedName("cannabisSeeds").setTextureName(textureBase + "cannabisSeeds");
        GameRegistry.registerItem(itemCannabisSeeds, "cannabisSeeds", MODID);
        itemCannabisSeeds.setCreativeTab(CreativeTabs.tabMisc);

        itemCannabisLeaf = new Item().setUnlocalizedName("cannabisLeaf").setTextureName(textureBase + "cannabisLeaf");
        GameRegistry.registerItem(itemCannabisLeaf, "cannabisLeaf", MODID);
        itemCannabisLeaf.setCreativeTab(CreativeTabs.tabMisc);

        itemCannabisBuds = new Item().setUnlocalizedName("cannabisBuds").setTextureName(textureBase + "cannabisBuds");
        GameRegistry.registerItem(itemCannabisBuds, "cannabisBuds", MODID);
        itemCannabisBuds.setCreativeTab(CreativeTabs.tabMisc);

        itemDriedCannabisBuds = new Item().setUnlocalizedName("driedCannabisBuds").setTextureName(textureBase + "driedCannabisBuds");
        GameRegistry.registerItem(itemDriedCannabisBuds, "driedCannabisBuds", MODID);
        itemDriedCannabisBuds.setCreativeTab(CreativeTabs.tabMisc);

        itemDriedCannabisLeaves = new Item().setUnlocalizedName("driedCannabisLeaves").setTextureName(textureBase + "driedCannabisLeaves");
        GameRegistry.registerItem(itemDriedCannabisLeaves, "driedCannabisLeaves", MODID);
        itemDriedCannabisLeaves.setCreativeTab(CreativeTabs.tabMisc);

        itemHashMuffin = new ItemHashMuffin().setUnlocalizedName("hashMuffin").setTextureName(textureBase + "hashMuffin");
        GameRegistry.registerItem(itemHashMuffin, "hashMuffin", MODID);
        itemHashMuffin.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Drying Table----------------------------------

        blockDryingTable = new BlockDryingTable().setBlockName("dryingTable").setBlockTextureName(textureBase + "dryingTable").setHardness(1.0f);
        GameRegistry.registerBlock(blockDryingTable, "dryingTable");
        GameRegistry.registerTileEntity(TileEntityDryingTable.class, "dryingTable");
        blockDryingTable.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Magic Mushrooms----------------------------------

        itemMagicMushroomsBrown = new ItemEdibleDrug(new DrugInfluence("BrownShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsBrown").setTextureName(textureBase + "magicMushroomsBrown");
        GameRegistry.registerItem(itemMagicMushroomsBrown, "brownMagicMushrooms", MODID);
        itemMagicMushroomsBrown.setCreativeTab(CreativeTabs.tabFood);

        itemMagicMushroomsRed = new ItemEdibleDrug(new DrugInfluence("RedShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsRed").setTextureName(textureBase + "magicMushroomsRed");
        GameRegistry.registerItem(itemMagicMushroomsRed, "redMagicMushrooms", MODID);
        itemMagicMushroomsRed.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Tobacco----------------------------------

        blockTobaccoPlant = new BlockTobaccoPlant().setBlockName("tobaccoPlant").setBlockTextureName(textureBase + "tobaccoPlant").setHardness(0.5f);
        GameRegistry.registerBlock(blockTobaccoPlant, "tobaccoPlant");
        blockTobaccoPlant.setCreativeTab(null);

        itemTobaccoLeaf = new Item().setUnlocalizedName("tobaccoLeaf").setTextureName(textureBase + "tobaccoLeaf");
        GameRegistry.registerItem(itemTobaccoLeaf, "tobaccoLeaf", MODID);
        itemTobaccoLeaf.setCreativeTab(CreativeTabs.tabMisc);

        itemTobaccoSeeds = new ItemSeeds(blockTobaccoPlant, Blocks.farmland).setUnlocalizedName("tobaccoSeeds").setTextureName(textureBase + "tobaccoSeeds");
        GameRegistry.registerItem(itemTobaccoSeeds, "tobaccoSeeds", MODID);
        itemTobaccoSeeds.setCreativeTab(CreativeTabs.tabMisc);

        itemDriedTobacco = new Item().setUnlocalizedName("driedTobacco").setTextureName(textureBase + "driedTobacco");
        GameRegistry.registerItem(itemDriedTobacco, "driedTobacco", MODID);
        itemDriedTobacco.setCreativeTab(CreativeTabs.tabMisc);

        itemCigarette = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigarette"}, new String[]{"cigaretteInUse"}).setUnlocalizedName("cigarette").setTextureName(textureBase + "cigarette");
        GameRegistry.registerItem(itemCigarette, "cigarette", MODID);
        itemCigarette.setCreativeTab(CreativeTabs.tabMisc);

        itemCigar = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigar", "cigar1", "cigar2", "cigar3"}, new String[]{"cigarInUse", "cigar1InUse", "cigar2InUse", "cigar3InUse"}).setUnlocalizedName("cigar").setTextureName(textureBase + "cigar");
        GameRegistry.registerItem(itemCigar, "cigar", MODID);
        itemCigar.setCreativeTab(CreativeTabs.tabMisc);

        itemJoint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.20f)}, new String[]{"joint"}, new String[]{"jointInUse"}).setUnlocalizedName("joint").setTextureName(textureBase + "joint");
        GameRegistry.registerItem(itemJoint, "joint", MODID);
        itemJoint.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Cocaine----------------------------------

        blockCocaPlant = new BlockCocaPlant().setBlockName("cocaPlant").setBlockTextureName(textureBase + "cocaPlant").setHardness(0.5f);
        GameRegistry.registerBlock(blockCocaPlant, ItemBlock.class, "cocaPlant", MODID);
        blockCocaPlant.setCreativeTab(null);

        itemCocaSeeds = new ItemSeeds(blockCocaPlant, Blocks.farmland).setUnlocalizedName("cocaSeeds").setTextureName(textureBase + "cocaSeeds");
        GameRegistry.registerItem(itemCocaSeeds, "cocaSeeds", MODID);
        itemCocaSeeds.setCreativeTab(CreativeTabs.tabMisc);

        itemCocaLeaf = new Item().setUnlocalizedName("cocaLeaf").setTextureName(textureBase + "cocaLeaf");
        GameRegistry.registerItem(itemCocaLeaf, "cocaLeaf", MODID);
        itemCocaLeaf.setCreativeTab(CreativeTabs.tabMisc);

        itemDriedCocaLeaves = new Item().setUnlocalizedName("driedCocaLeaves").setTextureName(textureBase + "driedCocaLeaves");
        GameRegistry.registerItem(itemDriedCocaLeaves, "driedCocaLeaves", MODID);
        itemDriedCocaLeaves.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Jenever----------------------------------

        blockPsycheLeaves = new BlockPsycheLeaves().setBlockName("psycheLeaves").setBlockTextureName(Psychedelicraft.textureBase + "psycheLeaves").setHardness(1.0F);
        GameRegistry.registerBlock(blockPsycheLeaves, "psycheLeaves");
        blockPsycheLeaves.setCreativeTab(CreativeTabs.tabDecorations);

        blockPsycheLog = new BlockPsycheLog().setBlockName("psycheLog").setBlockTextureName(Psychedelicraft.textureBase + "psycheLog").setHardness(1.0F);
        GameRegistry.registerBlock(blockPsycheLog, "psycheLog");
        blockPsycheLog.setCreativeTab(CreativeTabs.tabDecorations);

        blockJuniperSapling = new BlockPsycheSapling().setBlockName("psycheSapling").setBlockTextureName(Psychedelicraft.textureBase + "psycheSapling").setHardness(1.0F);
        GameRegistry.registerBlock(blockJuniperSapling, "psycheSapling");
        blockJuniperSapling.setCreativeTab(CreativeTabs.tabDecorations);

        itemJuniperBerries = new ItemFood(1, 0.5F, true).setUnlocalizedName("juniperBerries").setTextureName(textureBase + "juniperBerries");
        GameRegistry.registerItem(itemJuniperBerries, "juniperBerries", MODID);
        itemJuniperBerries.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Coffee----------------------------------

        blockCoffea = new BlockCoffea().setBlockName("coffea").setBlockTextureName(textureBase + "coffea").setHardness(0.5f);
        GameRegistry.registerBlock(blockCoffea, ItemBlock.class, "coffea", MODID);
        blockCoffea.setCreativeTab(null);

        itemCoffeaCherries = new ItemSeeds(blockCoffea, Blocks.farmland).setUnlocalizedName("coffeaCherries").setTextureName(textureBase + "coffeaCherries");
        GameRegistry.registerItem(itemCoffeaCherries, "coffeaCherries", MODID);
        itemCoffeaCherries.setCreativeTab(CreativeTabs.tabFood);

        itemCoffeeBeans = new Item().setUnlocalizedName("coffeeBeans").setTextureName(textureBase + "coffeeBeans");
        GameRegistry.registerItem(itemCoffeeBeans, "coffeeBeans", MODID);
        itemCoffeeBeans.setCreativeTab(CreativeTabs.tabFood);

        itemColdCoffee = new ItemWoodenMugColdCoffee().setUnlocalizedName("coldCoffee").setTextureName(textureBase + "coldCoffee");
        GameRegistry.registerItem(itemColdCoffee, "coldCoffee", MODID);
        itemColdCoffee.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Peyote----------------------------------

        blockPeyote = new BlockPeyote().setBlockName("peyote").setBlockTextureName(textureBase + "peyote").setHardness(0.5f);
        GameRegistry.registerBlock(blockPeyote, ItemBlock.class, "peyote", MODID);
        GameRegistry.registerTileEntity(TileEntityPeyote.class, "peyote");
        blockPeyote.setCreativeTab(CreativeTabs.tabDecorations);

        itemDriedPeyote = new ItemEdibleDrug(new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("driedPeyote").setTextureName(textureBase + "driedPeyote");
        GameRegistry.registerItem(itemDriedPeyote, "driedPeyote", MODID);
        itemDriedPeyote.setCreativeTab(CreativeTabs.tabFood);

        itemPeyoteJoint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Peyote", 20, 0.003, 0.0015, 0.4f), new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.1f)}, new String[]{"jointPeyote"}, new String[]{"jointPeyoteInUse"}).setUnlocalizedName("jointPeyote").setTextureName(textureBase + "jointPeyote");
        GameRegistry.registerItem(itemPeyoteJoint, "peyoteJoint", MODID);
        itemPeyoteJoint.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Digital----------------------------------

        int realityRiftID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityRealityRift.class, "realityRift", realityRiftID);
        EntityRegistry.registerModEntity(EntityRealityRift.class, "realityRift", realityRiftID, this, 80, 3, false);

        blockRiftJar = new BlockRiftJar().setBlockName("riftJar").setBlockTextureName(textureBase + "riftJar");
        GameRegistry.registerBlock(blockRiftJar, ItemRiftJar.class, "riftJar", MODID);
        GameRegistry.registerTileEntity(TileEntityRiftJar.class, "riftJar");
        blockRiftJar.setCreativeTab(CreativeTabs.tabDecorations);

        blockGlitched = new BlockGlitched().setBlockName("glitched").setBlockTextureName(textureBase + "glitched");
        GameRegistry.registerBlock(blockGlitched, ItemBlock.class, "glitched", MODID);
        blockGlitched.setCreativeTab(null);

        //----------------------------------------------------------Harmonium----------------------------------

        itemHarmonium = new ItemHarmonium().setUnlocalizedName("harmonium").setTextureName(textureBase + "harmonium");
        GameRegistry.registerItem(itemHarmonium, "harmonium", MODID);
        itemHarmonium.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Villager Dealer----------------------------------

        VillagerRegistry.instance().registerVillagerId(villagerDealerProfessionID);

        VillagerRegistry.instance().registerVillageTradeHandler(villagerDealerProfessionID, new VillagerTradeHandlerDrugDealer());
        VillagerRegistry.instance().registerVillageTradeHandler(0, new VillagerTradeHandlerFarmer());

        //----------------------------------------------------------Other----------------------------------

        BlockBarrel.registerBarrelEntry(0, blockBarrel, new BlockBarrel.BarrelEntry(new ResourceLocation(MODID, filePathTextures + "beerBarrelTexture.png"), itemWoodenMug, 0, itemWoodenMug, 15, 1, 1), new ItemBarrel.BarrelEntry("beer", 0, textureBase + "barrelItemBeer"));
        BlockBarrel.registerBarrelEntry(1, blockBarrel, new BlockBarrel.BarrelEntry(new ResourceLocation(MODID, filePathTextures + "wineBarrelTexture.png"), itemGlassChalice, 0, itemGlassChalice, 10, 15, 1), new ItemBarrel.BarrelEntry("wine", 1, textureBase + "barrelItemWine"));
        BlockBarrel.registerBarrelEntry(2, blockBarrel, new BlockBarrel.BarrelEntry(new ResourceLocation(MODID, filePathTextures + "jeneverBarrelTexture.png"), itemWoodenMug, 0, itemWoodenMug, 15, 2, 2), new ItemBarrel.BarrelEntry("jenever", 2, textureBase + "barrelItemJenever"));

        itemSyringeCocaineDamage = itemSyringe.addEffect(1, new DrugInfluence[]{new DrugInfluence("Cocaine", 0, 0.005, 0.01, 0.5f)}, 0x55ffffff, "cocaine");
        itemSyringeCaffeineDamage = itemSyringe.addEffect(2, new DrugInfluence[]{new DrugInfluence("Caffeine", 0, 0.005, 0.01, 0.85f)}, 0x552e1404, "caffeine");

        itemWoodenBowlPeyoteDamage = itemWoodenBowlDrug.addEffect(0, new DrugInfluence[]{new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.9f)}, "peyote", textureBase + "woodenBowlPeyote");

        proxy.preInit(config);

        config.save();
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        proxy.registerRenderers();

        DrugInfluence.registerInfluence(DrugInfluence.class, "default");
        DrugInfluence.registerInfluence(DrugInfluenceHarmonium.class, "harmonium");

        addCrafting();
        addWorldGen();

        dryingTableContainerID = guiHandler.acquireUniqueContainerID();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }

    private void addCrafting()
    {
        GameRegistry.addRecipe(new ItemStack(itemSyringe), "I", "#", 'I', Items.iron_ingot, '#', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(itemPipe), "  I", " S ", "WS ", 'I', Items.iron_ingot, 'S', Items.stick, 'W', Blocks.planks);

        GameRegistry.addRecipe(new ItemStack(itemGlassChalice, 4), "# #", " # ", " # ", '#', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(blockBarrel, 1, 1), "###", "###", "W#W", '#', itemWineGrapes, 'W', Blocks.planks);
        GameRegistry.addRecipe(new ItemStack(blockWineGrapeLattice), "###", "###", "O#O", '#', Items.stick, 'O', Blocks.planks);

        for (int i = 0; i < 16; i++)
        {
            GameRegistry.addRecipe(new ItemStack(itemMolotovCocktail, 1, i), "#", "W", '#', Items.paper, 'W', new ItemStack(itemGlassChalice, 1, i + 1));
        }

        GameRegistry.addRecipe(new ItemStack(itemWoodenMug, 8), "# #", "# #", "###", '#', Blocks.planks);
        GameRegistry.addRecipe(new ItemStack(blockBarrel, 1, 0), "###", "###", "WUW", '#', Items.wheat, 'U', Items.water_bucket, 'W', Blocks.planks);

        GameRegistry.addRecipe(new ItemStack(blockDryingTable), "###", "#R#", '#', Blocks.planks, 'R', Items.redstone);

        TileEntityDryingTable.addDryingResult(itemCannabisLeaf, new ItemStack(itemDriedCannabisLeaves, 3));
        TileEntityDryingTable.addDryingResult(itemCannabisBuds, new ItemStack(itemDriedCannabisBuds, 3));
        GameRegistry.addRecipe(new ItemStack(itemHashMuffin), "LLL", "#X#", "LLL", 'X', new ItemStack(Items.dye, 1, 3), '#', Items.wheat, 'L', itemDriedCannabisLeaves);
        GameRegistry.addRecipe(new ItemStack(itemJoint), "P", "C", "P", 'P', Items.paper, 'C', itemDriedCannabisBuds);
        itemPipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(itemDriedCannabisBuds), new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.25f)}));

        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(Blocks.brown_mushroom), new ItemStack(itemMagicMushroomsBrown, 3));
        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(Blocks.red_mushroom), new ItemStack(itemMagicMushroomsRed, 3));

        GameRegistry.addRecipe(new ItemStack(itemCigarette, 4), "P", "T", "P", 'P', Items.paper, 'T', itemDriedTobacco);
        GameRegistry.addRecipe(new ItemStack(itemCigar), "TTT", "TTT", "PPP", 'P', Items.paper, 'T', itemDriedTobacco);
        TileEntityDryingTable.addDryingResult(itemTobaccoLeaf, new ItemStack(itemDriedTobacco, 3));
        itemPipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(itemDriedTobacco), new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.8f)}));

        GameRegistry.addShapelessRecipe(new ItemStack(itemSyringe, 1, itemSyringeCocaineDamage), new ItemStack(Items.water_bucket), new ItemStack(itemSyringe), new ItemStack(itemDriedCocaLeaves));
        TileEntityDryingTable.addDryingResult(itemCocaLeaf, new ItemStack(itemDriedCocaLeaves, 3));

        GameRegistry.addRecipe(new ItemStack(Blocks.planks, 4, 1), "#", '#', blockPsycheLog);
        GameRegistry.addRecipe(new ItemStack(blockBarrel, 1, 2), "JSJ", "AGA", "WUW", 'J', itemJuniperBerries, 'S', Items.sugar, 'A', itemWineGrapes, 'G', Items.wheat, 'U', Items.water_bucket, 'W', Blocks.planks);

        GameRegistry.addSmelting(itemCoffeaCherries, new ItemStack(itemCoffeeBeans), 0.2f);

        GameRegistry.addShapelessRecipe(new ItemStack(itemColdCoffee), new ItemStack(Items.water_bucket), new ItemStack(itemWoodenMug, 1, 0), new ItemStack(itemCoffeeBeans), new ItemStack(itemCoffeeBeans));
        GameRegistry.addSmelting(itemColdCoffee, new ItemStack(itemWoodenMug, 1, 3), 0.2f);
        GameRegistry.addShapelessRecipe(new ItemStack(itemSyringe, 1, itemSyringeCaffeineDamage), new ItemStack(Items.water_bucket), new ItemStack(itemSyringe), new ItemStack(itemCoffeeBeans), new ItemStack(itemCoffeeBeans));

        GameRegistry.addRecipe(new ItemStack(itemWoodenBowlDrug, 1, 0), "P", "P", "B", 'P', itemDriedPeyote, 'B', Items.bowl);
        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(blockPeyote), new ItemStack(itemDriedPeyote, 3));
        GameRegistry.addRecipe(new ItemStack(itemPeyoteJoint), "P", "D", "P", 'P', Items.paper, 'D', itemDriedPeyote);

        for (int i = 0; i < 16; i++)
        {
            itemPipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(itemHarmonium, 1, 15 - i), new DrugInfluence[]{new DrugInfluenceHarmonium("Harmonium", 0, 0.04, 0.01, 0.65f, EntitySheep.fleeceColorTable[i])}, EntitySheep.fleeceColorTable[i]));
            GameRegistry.addShapelessRecipe(new ItemStack(itemHarmonium, 1, i), new ItemStack(Items.dye, 1, i), new ItemStack(Items.glowstone_dust), new ItemStack(itemDriedTobacco));
        }

        GameRegistry.addRecipe(new ItemStack(blockRiftJar), "O-O", "GO ", "OIO", 'O', Blocks.glass, '-', Blocks.planks, 'G', Items.gold_ingot, 'I', Items.iron_ingot);
    }

    private void addWorldGen()
    {
        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenJuniperTrees(false),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.extremeHills, 0.1f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.taiga, 0.1f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.icePlains, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.coldTaiga, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.coldTaigaHills, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.taigaHills, 0.1f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.iceMountains, 0.05f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, blockCannabisPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.04f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, blockTobaccoPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.04f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.04f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, false, blockCoffea),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.05f),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.savanna, 0.05f)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenTilledPatch(false, true, blockCocaPlant),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.plains, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.forest, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.river, 0.4f, 5),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.beach, 0.4f, 5)), 10);

        GameRegistry.registerWorldGenerator(new GeneratorGeneric(new WorldGenPeyote(false),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.desert, 0.01f, 4),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.extremeHills, 0.02f, 4),
                new GeneratorGeneric.EntryDefault(BiomeGenBase.jungleHills, 0.01f, 4)), 10);

        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(itemWineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(itemGlassChalice, 0, 1, 4, 5));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(itemWoodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(itemJuniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(itemDriedTobacco, 0, 1, 16, 5));

        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(itemWineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(itemWoodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(itemJuniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(itemDriedTobacco, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(itemPipe, 0, 1, 1, 3));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(itemCigarette, 0, 1, 8, 5));

        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemWineGrapes, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemJuniperBerries, 0, 1, 8, 10));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemWoodenMug, 0, 1, 16, 5));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemCigarette, 0, 1, 16, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemCigar, 0, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemJoint, 0, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemColdCoffee, 0, 1, 4, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemSyringe, itemSyringeCocaineDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemSyringe, itemSyringeCaffeineDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemWoodenBowlDrug, itemWoodenBowlPeyoteDamage, 1, 1, 1));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(itemHashMuffin, 0, 1, 8, 1));
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
        for (FMLInterModComms.IMCMessage message : event.getMessages())
        {
            communicationHandler.onIMCMessage(message, false);
        }
    }
}