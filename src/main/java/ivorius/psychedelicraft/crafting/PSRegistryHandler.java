/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.crafting;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.*;
import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import ivorius.psychedelicraft.entities.EntityRealityRift;
import ivorius.psychedelicraft.entities.VillagerTradeHandlerDrugDealer;
import ivorius.psychedelicraft.entities.VillagerTradeHandlerFarmer;
import ivorius.psychedelicraft.entities.drugs.DrugFactoryPsychedelicraft;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
import ivorius.psychedelicraft.entities.drugs.DrugRegistry;
import ivorius.psychedelicraft.fluids.*;
import ivorius.psychedelicraft.items.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static ivorius.psychedelicraft.Psychedelicraft.modBase;
import static ivorius.psychedelicraft.blocks.PSBlocks.*;
import static ivorius.psychedelicraft.crafting.OreDictionaryConstants.*;
import static ivorius.psychedelicraft.entities.PSEntityList.*;
import static ivorius.psychedelicraft.fluids.FluidHelper.MILLIBUCKETS_PER_LITER;
import static ivorius.psychedelicraft.fluids.PSFluids.*;
import static ivorius.psychedelicraft.items.PSItems.*;

/**
 * Created by lukas on 18.10.14.
 */
public class PSRegistryHandler
{
    public static void preInit(FMLPreInitializationEvent event, Psychedelicraft mod)
    {
        DrugRegistry.registerFactory(new DrugFactoryPsychedelicraft());

        //----------------------------------------------------------Containers----------------------------------

        syringe = (ItemInjectable) new ItemInjectable(MILLIBUCKETS_PER_LITER / 100).setUnlocalizedName("syringe").setTextureName(modBase + "syringe");
        GameRegistry.registerItem(syringe, "syringe", Psychedelicraft.MODID);
        syringe.setCreativeTab(Psychedelicraft.creativeTab);

        woodenBowlDrug = (ItemCup) new ItemWoodenBowlDrug(MILLIBUCKETS_PER_LITER / 20).setUnlocalizedName("woodenBowlDrug").setTextureName(modBase + "bowl");
        GameRegistry.registerItem(woodenBowlDrug, "psycheWoodenBowl", Psychedelicraft.MODID);
        woodenBowlDrug.setCreativeTab(Psychedelicraft.drinksTab);

        woodenMug = (ItemCup) new ItemCupWithLiquid(MILLIBUCKETS_PER_LITER / 2).setUnlocalizedName("woodenMug").setTextureName(modBase + "woodenMug");
        GameRegistry.registerItem(woodenMug, "woodenMug", Psychedelicraft.MODID);
        woodenMug.setCreativeTab(Psychedelicraft.drinksTab);

        glassChalice = (ItemCup) new ItemCupWithLiquid(MILLIBUCKETS_PER_LITER / 10).setUnlocalizedName("glassChalice").setTextureName(modBase + "glassChalice");
        GameRegistry.registerItem(glassChalice, "glassChalice", Psychedelicraft.MODID);
        glassChalice.setCreativeTab(Psychedelicraft.drinksTab);

        bottle = (ItemBottleDrinkable) new ItemBottleDrinkable(MILLIBUCKETS_PER_LITER * 2).setUnlocalizedName("pscBottle").setTextureName(modBase + "bottle");
        GameRegistry.registerItem(bottle, "bottle", Psychedelicraft.MODID);
        bottle.setCreativeTab(Psychedelicraft.drinksTab);

        molotovCocktail = (ItemMolotovCocktail) new ItemMolotovCocktail(MILLIBUCKETS_PER_LITER * 2).setUnlocalizedName("molotovCocktail").setTextureName(modBase + "molotovCocktail");
        GameRegistry.registerItem(molotovCocktail, "molotovCocktail", Psychedelicraft.MODID);
        molotovCocktail.setCreativeTab(Psychedelicraft.weaponsTab);

        pipe = (ItemSmokingPipe) (new ItemSmokingPipe().setUnlocalizedName("smokingPipe").setTextureName(modBase + "smokingPipe"));
        GameRegistry.registerItem(pipe, "smokingPipe", Psychedelicraft.MODID);
        pipe.setCreativeTab(Psychedelicraft.creativeTab);

        bong = (ItemBong) new ItemBong().setUnlocalizedName("bong").setTextureName(modBase + "bong");
        GameRegistry.registerItem(bong, "bong", Psychedelicraft.MODID);
        bong.setCreativeTab(Psychedelicraft.creativeTab);

        dryingTable = new BlockDryingTable().setHardness(1.0f).setBlockName("dryingTable").setBlockTextureName(modBase + "dryingTable");
        dryingTable.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(dryingTable, "dryingTable");
        GameRegistry.registerTileEntityWithAlternatives(TileEntityDryingTable.class, "ygcDryingTable", "dryingTable");

        barrel = new BlockBarrel().setHardness(1.0F).setBlockName("psBarrel").setBlockTextureName(modBase + "barrel");
        barrel.setCreativeTab(Psychedelicraft.drinksTab);
        GameRegistry.registerBlock(barrel, ItemBarrel.class, "barrel");
        GameRegistry.registerTileEntityWithAlternatives(TileEntityBarrel.class, "ygcBarrel", "barrel");
        itemBarrel = (ItemBarrel) Item.getItemFromBlock(barrel);

        mashTub = new BlockMashTub().setHardness(1.0F).setBlockName("psMashTub");
        mashTub.setCreativeTab(Psychedelicraft.drinksTab);
        GameRegistry.registerBlock(mashTub, ItemMashTub.class, "mash_tub");
        GameRegistry.registerTileEntityWithAlternatives(TileEntityMashTub.class, "ygcMashTub");
        itemMashTub = (ItemMashTub) Item.getItemFromBlock(mashTub);

        flask = new BlockFlask().setHardness(1.0F).setBlockName("psFlask");
        flask.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(flask, ItemFlask.class, "flask");
        GameRegistry.registerTileEntity(TileEntityFlask.class, "psFlask");
        itemFlask = (ItemFlask) Item.getItemFromBlock(flask);

        distillery = new BlockDistillery().setHardness(1.0F).setBlockName("psDistillery");
        distillery.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(distillery, ItemDistillery.class, "distillery");
        GameRegistry.registerTileEntity(TileEntityDistillery.class, "psDistillery");
        itemDistillery = (ItemDistillery) Item.getItemFromBlock(distillery);

        //----------------------------------------------------------Wine----------------------------------

        wineGrapeLattice = new BlockWineGrapeLattice().setHardness(0.3F).setBlockName("wineGrapeLattice").setBlockTextureName(modBase + "wineGrapeLattice");
        wineGrapeLattice.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(wineGrapeLattice, ItemBlock.class, "wineGrapeLattice");

        wineGrapes = (new ItemWineGrapes(1, 0.5F, true, 15)).setUnlocalizedName("wineGrapes").setTextureName(modBase + "wineGrapes");
        GameRegistry.registerItem(wineGrapes, "wineGrapes", Psychedelicraft.MODID);
        wineGrapes.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_GRAPE, wineGrapes);

        //----------------------------------------------------------Molotov Cocktail----------------------------------

        int entityMolotovCocktailID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID);
        EntityRegistry.registerModEntity(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID, mod, 64, 10, true);

        //----------------------------------------------------------Weed----------------------------------

        cannabisPlant = new BlockCannabisPlant().setHardness(0.5f).setBlockName("cannabisPlant").setBlockTextureName(modBase + "cannabisPlant");
        cannabisPlant.setCreativeTab(null);
        GameRegistry.registerBlock(cannabisPlant, ItemBlock.class, "cannabisPlant");
        OreDictionary.registerOre(DC_CROP_CANNABIS, cannabisPlant);

        cannabisSeeds = new ItemSeeds(cannabisPlant, Blocks.farmland).setUnlocalizedName("cannabisSeeds").setTextureName(modBase + "cannabisSeeds");
        GameRegistry.registerItem(cannabisSeeds, "cannabisSeeds", Psychedelicraft.MODID);
        cannabisSeeds.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_SEED_CANNABIS, cannabisSeeds);

        cannabisLeaf = new Item().setUnlocalizedName("cannabisLeaf").setTextureName(modBase + "cannabisLeaf");
        GameRegistry.registerItem(cannabisLeaf, "cannabisLeaf", Psychedelicraft.MODID);
        cannabisLeaf.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_LEAF_CANNABIS, cannabisLeaf);

        cannabisBuds = new Item().setUnlocalizedName("cannabisBuds").setTextureName(modBase + "cannabisBuds");
        GameRegistry.registerItem(cannabisBuds, "cannabisBuds", Psychedelicraft.MODID);
        cannabisBuds.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_BUD_CANNABIS, cannabisBuds);

        driedCannabisBuds = new Item().setUnlocalizedName("driedCannabisBuds").setTextureName(modBase + "driedCannabisBuds");
        GameRegistry.registerItem(driedCannabisBuds, "driedCannabisBuds", Psychedelicraft.MODID);
        driedCannabisBuds.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_BUD_CANNABIS_DRIED, driedCannabisBuds);

        driedCannabisLeaves = new Item().setUnlocalizedName("driedCannabisLeaves").setTextureName(modBase + "driedCannabisLeaves");
        GameRegistry.registerItem(driedCannabisLeaves, "driedCannabisLeaves", Psychedelicraft.MODID);
        driedCannabisLeaves.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_LEAF_CANNABIS_DRIED, driedCannabisLeaves);

        hashMuffin = new ItemHashMuffin().setUnlocalizedName("hashMuffin").setTextureName(modBase + "hashMuffin");
        GameRegistry.registerItem(hashMuffin, "hashMuffin", Psychedelicraft.MODID);
        hashMuffin.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_HASH_MUFFIN, hashMuffin);

        //----------------------------------------------------------Magic Mushrooms----------------------------------

        magicMushroomsBrown = new ItemEdibleDrug(new DrugInfluence("BrownShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsBrown").setTextureName(modBase + "magicMushroomsBrown");
        GameRegistry.registerItem(magicMushroomsBrown, "brownMagicMushrooms", Psychedelicraft.MODID);
        magicMushroomsBrown.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_FOOD_MAGIC_MUSHROOM_BROWN, magicMushroomsBrown);

        magicMushroomsRed = new ItemEdibleDrug(new DrugInfluence("RedShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsRed").setTextureName(modBase + "magicMushroomsRed");
        GameRegistry.registerItem(magicMushroomsRed, "redMagicMushrooms", Psychedelicraft.MODID);
        magicMushroomsRed.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_FOOD_MAGIC_MUSHROOM_RED, magicMushroomsRed);

        //----------------------------------------------------------Tobacco----------------------------------

        tobaccoPlant = new BlockTobaccoPlant().setHardness(0.5f).setBlockName("tobaccoPlant").setBlockTextureName(modBase + "tobaccoPlant");
        tobaccoPlant.setCreativeTab(null);
        GameRegistry.registerBlock(tobaccoPlant, "tobaccoPlant");
        OreDictionary.registerOre(DC_CROP_TOBACCO, tobaccoPlant);

        tobaccoSeeds = new ItemSeeds(tobaccoPlant, Blocks.farmland).setUnlocalizedName("tobaccoSeeds").setTextureName(modBase + "tobaccoSeeds");
        GameRegistry.registerItem(tobaccoSeeds, "tobaccoSeeds", Psychedelicraft.MODID);
        tobaccoSeeds.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_SEED_TOBACCO, tobaccoSeeds);

        tobaccoLeaf = new Item().setUnlocalizedName("tobaccoLeaf").setTextureName(modBase + "tobaccoLeaf");
        GameRegistry.registerItem(tobaccoLeaf, "tobaccoLeaf", Psychedelicraft.MODID);
        tobaccoLeaf.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_LEAF_TOBACCO, tobaccoLeaf);

        driedTobacco = new Item().setUnlocalizedName("driedTobacco").setTextureName(modBase + "driedTobacco");
        GameRegistry.registerItem(driedTobacco, "driedTobacco", Psychedelicraft.MODID);
        driedTobacco.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_DRIED_TOBACCO, driedTobacco);

        cigarette = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigarette"}, new String[]{"cigaretteInUse"}).setUnlocalizedName("cigarette").setTextureName(modBase + "cigarette");
        GameRegistry.registerItem(cigarette, "cigarette", Psychedelicraft.MODID);
        cigarette.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_CIGARETTE, cigarette);

        cigar = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigar", "cigar1", "cigar2", "cigar3"}, new String[]{"cigarInUse", "cigar1InUse", "cigar2InUse", "cigar3InUse"}).setUnlocalizedName("cigar").setTextureName(modBase + "cigar");
        GameRegistry.registerItem(cigar, "cigar", Psychedelicraft.MODID);
        cigar.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_CIGAR, cigar);

        joint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.20f)}, new String[]{"joint"}, new String[]{"jointInUse"}).setUnlocalizedName("joint").setTextureName(modBase + "joint");
        GameRegistry.registerItem(joint, "joint", Psychedelicraft.MODID);
        joint.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_JOINT, joint);

        //----------------------------------------------------------Cocaine----------------------------------

        cocaPlant = new BlockCocaPlant().setHardness(0.5f).setBlockName("cocaPlant").setBlockTextureName(modBase + "cocaPlant");
        cocaPlant.setCreativeTab(null);
        GameRegistry.registerBlock(cocaPlant, ItemBlock.class, "cocaPlant");
        OreDictionary.registerOre(DC_CROP_COCA, cocaPlant);

        cocaSeeds = new ItemSeeds(cocaPlant, Blocks.farmland).setUnlocalizedName("cocaSeeds").setTextureName(modBase + "cocaSeeds");
        GameRegistry.registerItem(cocaSeeds, "cocaSeeds", Psychedelicraft.MODID);
        cocaSeeds.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_SEED_COCA, cocaSeeds);

        cocaLeaf = new Item().setUnlocalizedName("cocaLeaf").setTextureName(modBase + "cocaLeaf");
        GameRegistry.registerItem(cocaLeaf, "cocaLeaf", Psychedelicraft.MODID);
        cocaLeaf.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_LEAF_COCA, cocaLeaf);

        driedCocaLeaves = new Item().setUnlocalizedName("driedCocaLeaves").setTextureName(modBase + "driedCocaLeaves");
        GameRegistry.registerItem(driedCocaLeaves, "driedCocaLeaves", Psychedelicraft.MODID);
        driedCocaLeaves.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_LEAF_COCA_DRIED, driedCocaLeaves);

        //----------------------------------------------------------Jenever----------------------------------

        psycheLeaves = (BlockPsycheLeaves) new BlockPsycheLeaves().setBlockName("psycheLeaves").setBlockTextureName(modBase + "psycheLeaves");
        psycheLeaves.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(psycheLeaves, ItemLeavesForge.class, "psycheLeaves");
        OreDictionary.registerOre(DC_TREE_LEAVES, psycheLeaves);

        psycheLog = new BlockPsycheLog().setHardness(1.0F).setBlockName("psycheLog").setBlockTextureName(modBase + "psycheLog");
        psycheLog.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(psycheLog, "psycheLog");
        OreDictionary.registerOre(DC_LOG_WOOD, psycheLog);

        psycheSapling = new BlockPsycheSapling().setHardness(1.0F).setBlockName("psycheSapling").setBlockTextureName(modBase + "psycheSapling");
        psycheSapling.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(psycheSapling, "psycheSapling");
        OreDictionary.registerOre(DC_TREE_SAPLING, psycheSapling);

        juniperBerries = new ItemFoodSpecial(1, 0.5F, true, 15).setUnlocalizedName("juniperBerries").setTextureName(modBase + "juniperBerries");
        GameRegistry.registerItem(juniperBerries, "juniperBerries", Psychedelicraft.MODID);
        juniperBerries.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_JUNIPER, juniperBerries);

        //----------------------------------------------------------Coffee----------------------------------

        coffea = new BlockCoffea().setHardness(0.5f).setBlockName("coffea").setBlockTextureName(modBase + "coffea");
        coffea.setCreativeTab(null);
        GameRegistry.registerBlock(coffea, ItemBlock.class, "coffea");
        OreDictionary.registerOre(DC_CROP_COFFEA, coffea);

        coffeaCherries = new ItemSeeds(coffea, Blocks.farmland).setUnlocalizedName("coffeaCherries").setTextureName(modBase + "coffeaCherries");
        GameRegistry.registerItem(coffeaCherries, "coffeaCherries", Psychedelicraft.MODID);
        coffeaCherries.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_SEED_COFFEA, coffeaCherries);

        coffeeBeans = new Item().setUnlocalizedName("coffeeBeans").setTextureName(modBase + "coffeeBeans");
        GameRegistry.registerItem(coffeeBeans, "coffeeBeans", Psychedelicraft.MODID);
        coffeeBeans.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_COFFEE_BEANS, coffeeBeans);

        //----------------------------------------------------------Peyote----------------------------------

        peyote = new BlockPeyote().setHardness(0.5f).setBlockName("peyote").setBlockTextureName(modBase + "peyote");
        peyote.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(peyote, ItemBlock.class, "peyote");
        GameRegistry.registerTileEntity(TileEntityPeyote.class, "peyote");
        OreDictionary.registerOre(DC_CROP_PEYOTE, peyote);

        driedPeyote = new ItemEdibleDrug(new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("driedPeyote").setTextureName(modBase + "driedPeyote");
        GameRegistry.registerItem(driedPeyote, "driedPeyote", Psychedelicraft.MODID);
        driedPeyote.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_PEYOTE_DRIED, driedPeyote);

        peyoteJoint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Peyote", 20, 0.003, 0.0015, 0.4f), new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.1f)}, new String[]{"jointPeyote"}, new String[]{"jointPeyoteInUse"}).setUnlocalizedName("jointPeyote").setTextureName(modBase + "jointPeyote");
        GameRegistry.registerItem(peyoteJoint, "peyoteJoint", Psychedelicraft.MODID);
        peyoteJoint.setCreativeTab(Psychedelicraft.creativeTab);
        OreDictionary.registerOre(DC_PEYOTE_JOINT, peyoteJoint);

        //----------------------------------------------------------Harmonium----------------------------------

        harmonium = new ItemHarmonium().setUnlocalizedName("harmonium").setTextureName(modBase + "harmonium");
        GameRegistry.registerItem(harmonium, "harmonium", Psychedelicraft.MODID);
        if (PSConfig.enableHarmonium)
            harmonium.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Digital----------------------------------

        riftJar = new BlockRiftJar().setBlockName("riftJar").setBlockTextureName(modBase + "riftJar");
        GameRegistry.registerBlock(riftJar, ItemRiftJar.class, "riftJar");
        GameRegistry.registerTileEntity(TileEntityRiftJar.class, "riftJar");
        if (PSConfig.enableRiftJars)
            riftJar.setCreativeTab(Psychedelicraft.creativeTab);

        glitched = new BlockGlitched().setBlockName("glitched").setBlockTextureName(modBase + "glitched");
        GameRegistry.registerBlock(glitched, ItemBlock.class, "glitched");
        glitched.setCreativeTab(null);

        EntityRegistry.registerModEntity(EntityRealityRift.class, "realityRift", realityRiftID, mod, 80, 3, false);

        //----------------------------------------------------------Molotov Cocktail----------------------------------

        EntityRegistry.registerModEntity(EntityMolotovCocktail.class, "molotovCocktail", molotovCocktailID, mod, 64, 10, true);

        //----------------------------------------------------------Villager Dealer----------------------------------

        if (villagerDealerProfessionID >= 0)
        {
            VillagerRegistry.instance().registerVillagerId(villagerDealerProfessionID);
            VillagerRegistry.instance().registerVillageTradeHandler(villagerDealerProfessionID, new VillagerTradeHandlerDrugDealer());
        }

        if (PSConfig.farmerDrugDeals)
            VillagerRegistry.instance().registerVillageTradeHandler(0, new VillagerTradeHandlerFarmer());

        //----------------------------------------------------------Liquids----------------------------------

        slurry = new FluidSlurry("pscSlurry");
        slurry.setColor(0xcc704E21);
        slurry.setStillIconName(modBase + "slurry_still");
        slurry.setFlowingIconName(modBase + "slurry_flow");
        FluidRegistry.registerFluid(slurry);

        beer = new FluidMaturingAlcohol("pscBeer", 2, 2, 0.25, 0.08, PSConfig.beerInfo);
        beer.setDrinkable(true);
        beer.setColor(0xaafeaa08);
        beer.setSymbolIconName(modBase + "drinkBeer");
        beer.setStillIconName(modBase + "beer_still");
        beer.setFlowingIconName(modBase + "beer_flow");
        FluidRegistry.registerFluid(beer);

        wine = new FluidMaturingAlcohol("pscWine", 2, 5, 0.55, 0.2, PSConfig.wineInfo);
        wine.setDrinkable(true);
        wine.setColor(0xee3f0822);
        wine.setSymbolIconName(modBase + "drinkWine");
        wine.setStillIconName(modBase + "wine_still");
        wine.setFlowingIconName(modBase + "wine_flow");
        FluidRegistry.registerFluid(wine);

        riceWine = new FluidMaturingAlcohol("pscRiceWine", 2, 5, 0.7, 0.25, PSConfig.riceWineInfo);
        riceWine.setDrinkable(true);
        riceWine.setColor(0xeecac4b2);
        riceWine.setSymbolIconName(modBase + "drinkRiceWine");
        riceWine.setStillIconName(modBase + "rice_wine_still");
        riceWine.setFlowingIconName(modBase + "rice_wine_flow");
        FluidRegistry.registerFluid(riceWine);

        jenever = new FluidJenever("pscJenever", 2, 8, 0.5f, 1.7f, PSConfig.jeneverInfo);
        jenever.setDrinkable(true);
        jenever.setColor(0x44e8f4f8);
        jenever.setSymbolIconName(modBase + "drinkJenever");
        jenever.setStillIconName(modBase + "clear_still");
        jenever.setFlowingIconName(modBase + "clear_flow");
        FluidRegistry.registerFluid(jenever);

        vodka = new FluidVodka("pscVodka", 2, 8, 0.55f, 1.7f, PSConfig.vodkaInfo);
        vodka.setDrinkable(true);
        vodka.setColor(0x44e8f4f8);
        vodka.setSymbolIconName(modBase + "drinkVodka");
        vodka.setStillIconName(modBase + "clear_still");
        vodka.setFlowingIconName(modBase + "clear_flow");
        FluidRegistry.registerFluid(vodka);

        rum = new FluidRum("pscRum", 2, 8, 5, 0.35f, 1.7f, 0.2f, PSConfig.rumInfo);
        rum.setDrinkable(true);
        rum.setColor(0x44e8f4f8);
        rum.setSymbolIconName(modBase + "drinkRum");
        rum.setStillIconName(modBase + "clear_still");
        rum.setFlowingIconName(modBase + "clear_flow");
        FluidRegistry.registerFluid(rum);

        coffee = new FluidCoffee("pscCoffee");
        coffee.setDrinkable(true);
        coffee.setColor(0xffa77d55);
        coffee.setSymbolIconName(modBase + "drinkCoffee");
        coffee.setStillIconName(modBase + "coffee_still");
        coffee.setFlowingIconName(modBase + "coffee_flow");
        FluidRegistry.registerFluid(coffee);

        peyoteJuice = new FluidDrug("pscPeyoteJuice", new DrugInfluence("Peyote", 15, 0.005, 0.003, 2.0f));
        peyoteJuice.setDrinkable(true);
        peyoteJuice.setColor(0x779bab62);
        peyoteJuice.setSymbolIconName(modBase + "drinkPeyote");
        peyoteJuice.setStillIconName(modBase + "tea_still");
        peyoteJuice.setFlowingIconName(modBase + "tea_flow");
        FluidRegistry.registerFluid(peyoteJuice);

        cocaTea = new FluidDrug("pscCocaTea", new DrugInfluence("Cocaine", 60, 0.005, 0.002, 0.2f));
        cocaTea.setDrinkable(true);
        cocaTea.setColor(0x44787a36);
        cocaTea.setSymbolIconName(modBase + "drinkCocaTea");
        cocaTea.setStillIconName(modBase + "tea_still");
        cocaTea.setFlowingIconName(modBase + "tea_flow");
        FluidRegistry.registerFluid(cocaTea);

        cannabisTea = new FluidDrug("pscCannabisTea", new DrugInfluence("Cannabis", 60, 0.005, 0.002, 0.25f));
        cannabisTea.setDrinkable(true);
        cannabisTea.setColor(0x446d6f3c);
        cannabisTea.setSymbolIconName(modBase + "drinkCannabisTea");
        cannabisTea.setStillIconName(modBase + "tea_still");
        cannabisTea.setFlowingIconName(modBase + "tea_flow");
        FluidRegistry.registerFluid(cannabisTea);

        cocaineFluid = new FluidDrug("pscCocaineFluid", new DrugInfluence("Cocaine", 0, 0.005, 0.01, 50.0f));
        cocaineFluid.setInjectable(true);
        cocaineFluid.setColor(0x44e8f4f8);
        cocaineFluid.setStillIconName(modBase + "clear_still");
        cocaineFluid.setFlowingIconName(modBase + "clear_flow");
        FluidRegistry.registerFluid(cocaineFluid);

        caffeineFluid = new FluidDrug("pscCaffeineFluid", new DrugInfluence("Caffeine", 0, 0.005, 0.01, 85.0f));
        caffeineFluid.setInjectable(true);
        caffeineFluid.setColor(0x66eee2d3);
        caffeineFluid.setStillIconName(modBase + "clear_still");
        caffeineFluid.setFlowingIconName(modBase + "clear_flow");
        FluidRegistry.registerFluid(caffeineFluid);
    }

    public static void load(FMLInitializationEvent event, Psychedelicraft mod)
    {
    }
}
