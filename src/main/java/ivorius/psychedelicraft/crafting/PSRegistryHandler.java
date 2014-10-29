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
import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.*;
import ivorius.psychedelicraft.entities.*;
import ivorius.psychedelicraft.fluids.*;
import ivorius.psychedelicraft.items.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import static ivorius.psychedelicraft.Psychedelicraft.modBase;
import static ivorius.psychedelicraft.blocks.PSBlocks.*;
import static ivorius.psychedelicraft.fluids.FluidHelper.MILLIBUCKETS_PER_LITER;
import static ivorius.psychedelicraft.fluids.PSFluids.*;
import static ivorius.psychedelicraft.items.PSItems.*;
import static ivorius.psychedelicraft.entities.PSEntityList.*;

/**
 * Created by lukas on 18.10.14.
 */
public class PSRegistryHandler
{
    public static void preInit(FMLPreInitializationEvent event, Psychedelicraft mod)
    {
        //----------------------------------------------------------Containers----------------------------------

        syringe = (ItemInjectable) new ItemInjectable(MILLIBUCKETS_PER_LITER / 100).setUnlocalizedName("syringe").setTextureName(modBase + "syringe");
        GameRegistry.registerItem(syringe, "syringe", Psychedelicraft.MODID);
        syringe.setCreativeTab(Psychedelicraft.creativeTab);

        woodenBowlDrug = (ItemCup) new ItemWoodenBowlDrug(1).setUnlocalizedName("woodenBowlDrug").setTextureName(modBase + "bowl");
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

        dryingTable = new BlockDryingTable().setHardness(1.0f);
        dryingTable.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(dryingTable, "dryingTable");
        GameRegistry.registerTileEntityWithAlternatives(TileEntityDryingTable.class, "ygcDryingTable", "dryingTable");

        barrel = new BlockBarrel().setHardness(1.0F);
        barrel.setCreativeTab(Psychedelicraft.drinksTab);
        PSRegistryHandler.registerBlockDefault(barrel, ItemBarrel.class, "barrel");
        barrel.setBlockName("ygcBarrel");
        GameRegistry.registerTileEntityWithAlternatives(TileEntityBarrel.class, "ygcBarrel", "barrel");
        itemBarrel = (ItemBarrel) Item.getItemFromBlock(barrel);

        mashTub = new BlockMashTub().setHardness(1.0F);
        mashTub.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(mashTub, ItemMashTub.class, "mash_tub");
        mashTub.setBlockName("ygcMashTub");
        GameRegistry.registerTileEntityWithAlternatives(TileEntityMashTub.class, "ygcMashTub");
        itemMashTub = (ItemMashTub) Item.getItemFromBlock(mashTub);

        flask = new BlockFlask().setHardness(1.0F);
        flask.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(flask, ItemFlask.class, "flask");
        GameRegistry.registerTileEntity(TileEntityFlask.class, "ygcFlask");
        itemFlask = (ItemFlask) Item.getItemFromBlock(flask);

        distillery = new BlockDistillery().setHardness(1.0F);
        distillery.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(distillery, ItemDistillery.class, "distillery");
        GameRegistry.registerTileEntity(TileEntityDistillery.class, "ygcDistillery");
        itemDistillery = (ItemDistillery) Item.getItemFromBlock(distillery);

        //----------------------------------------------------------Wine----------------------------------

        wineGrapeLattice = new BlockWineGrapeLattice().setHardness(0.3F);
        wineGrapeLattice.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(wineGrapeLattice, ItemBlock.class, "wineGrapeLattice");

        wineGrapes = (new ItemWineGrapes(1, 0.5F, true, 15)).setUnlocalizedName("wineGrapes").setTextureName(modBase + "wineGrapes");
        GameRegistry.registerItem(wineGrapes, "wineGrapes", Psychedelicraft.MODID);
        wineGrapes.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Molotov Cocktail----------------------------------

        int entityMolotovCocktailID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID);
        EntityRegistry.registerModEntity(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID, mod, 64, 10, true);

        //----------------------------------------------------------Weed----------------------------------

        cannabisPlant = new BlockCannabisPlant().setHardness(0.5f);
        cannabisPlant.setCreativeTab(null);
        PSRegistryHandler.registerBlockDefault(cannabisPlant, ItemBlock.class, "cannabisPlant");

        cannabisSeeds = new ItemSeeds(cannabisPlant, Blocks.farmland).setUnlocalizedName("cannabisSeeds").setTextureName(modBase + "cannabisSeeds");
        GameRegistry.registerItem(cannabisSeeds, "cannabisSeeds", Psychedelicraft.MODID);
        cannabisSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        cannabisLeaf = new Item().setUnlocalizedName("cannabisLeaf").setTextureName(modBase + "cannabisLeaf");
        GameRegistry.registerItem(cannabisLeaf, "cannabisLeaf", Psychedelicraft.MODID);
        cannabisLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        cannabisBuds = new Item().setUnlocalizedName("cannabisBuds").setTextureName(modBase + "cannabisBuds");
        GameRegistry.registerItem(cannabisBuds, "cannabisBuds", Psychedelicraft.MODID);
        cannabisBuds.setCreativeTab(Psychedelicraft.creativeTab);

        driedCannabisBuds = new Item().setUnlocalizedName("driedCannabisBuds").setTextureName(modBase + "driedCannabisBuds");
        GameRegistry.registerItem(driedCannabisBuds, "driedCannabisBuds", Psychedelicraft.MODID);
        driedCannabisBuds.setCreativeTab(Psychedelicraft.creativeTab);

        driedCannabisLeaves = new Item().setUnlocalizedName("driedCannabisLeaves").setTextureName(modBase + "driedCannabisLeaves");
        GameRegistry.registerItem(driedCannabisLeaves, "driedCannabisLeaves", Psychedelicraft.MODID);
        driedCannabisLeaves.setCreativeTab(Psychedelicraft.creativeTab);

        hashMuffin = new ItemHashMuffin().setUnlocalizedName("hashMuffin").setTextureName(modBase + "hashMuffin");
        GameRegistry.registerItem(hashMuffin, "hashMuffin", Psychedelicraft.MODID);
        hashMuffin.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Magic Mushrooms----------------------------------

        magicMushroomsBrown = new ItemEdibleDrug(new DrugInfluence("BrownShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsBrown").setTextureName(modBase + "magicMushroomsBrown");
        GameRegistry.registerItem(magicMushroomsBrown, "brownMagicMushrooms", Psychedelicraft.MODID);
        magicMushroomsBrown.setCreativeTab(Psychedelicraft.creativeTab);

        magicMushroomsRed = new ItemEdibleDrug(new DrugInfluence("RedShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsRed").setTextureName(modBase + "magicMushroomsRed");
        GameRegistry.registerItem(magicMushroomsRed, "redMagicMushrooms", Psychedelicraft.MODID);
        magicMushroomsRed.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Tobacco----------------------------------

        tobaccoPlant = new BlockTobaccoPlant().setHardness(0.5f);
        tobaccoPlant.setCreativeTab(null);
        PSRegistryHandler.registerBlockDefault(tobaccoPlant, "tobaccoPlant");

        tobaccoLeaf = new Item().setUnlocalizedName("tobaccoLeaf").setTextureName(modBase + "tobaccoLeaf");
        GameRegistry.registerItem(tobaccoLeaf, "tobaccoLeaf", Psychedelicraft.MODID);
        tobaccoLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        tobaccoSeeds = new ItemSeeds(tobaccoPlant, Blocks.farmland).setUnlocalizedName("tobaccoSeeds").setTextureName(modBase + "tobaccoSeeds");
        GameRegistry.registerItem(tobaccoSeeds, "tobaccoSeeds", Psychedelicraft.MODID);
        tobaccoSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        driedTobacco = new Item().setUnlocalizedName("driedTobacco").setTextureName(modBase + "driedTobacco");
        GameRegistry.registerItem(driedTobacco, "driedTobacco", Psychedelicraft.MODID);
        driedTobacco.setCreativeTab(Psychedelicraft.creativeTab);

        cigarette = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigarette"}, new String[]{"cigaretteInUse"}).setUnlocalizedName("cigarette").setTextureName(modBase + "cigarette");
        GameRegistry.registerItem(cigarette, "cigarette", Psychedelicraft.MODID);
        cigarette.setCreativeTab(Psychedelicraft.creativeTab);

        cigar = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigar", "cigar1", "cigar2", "cigar3"}, new String[]{"cigarInUse", "cigar1InUse", "cigar2InUse", "cigar3InUse"}).setUnlocalizedName("cigar").setTextureName(modBase + "cigar");
        GameRegistry.registerItem(cigar, "cigar", Psychedelicraft.MODID);
        cigar.setCreativeTab(Psychedelicraft.creativeTab);

        joint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.20f)}, new String[]{"joint"}, new String[]{"jointInUse"}).setUnlocalizedName("joint").setTextureName(modBase + "joint");
        GameRegistry.registerItem(joint, "joint", Psychedelicraft.MODID);
        joint.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Cocaine----------------------------------

        cocaPlant = new BlockCocaPlant().setHardness(0.5f);
        cocaPlant.setCreativeTab(null);
        PSRegistryHandler.registerBlockDefault(cocaPlant, ItemBlock.class, "cocaPlant");

        cocaSeeds = new ItemSeeds(cocaPlant, Blocks.farmland).setUnlocalizedName("cocaSeeds").setTextureName(modBase + "cocaSeeds");
        GameRegistry.registerItem(cocaSeeds, "cocaSeeds", Psychedelicraft.MODID);
        cocaSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        cocaLeaf = new Item().setUnlocalizedName("cocaLeaf").setTextureName(modBase + "cocaLeaf");
        GameRegistry.registerItem(cocaLeaf, "cocaLeaf", Psychedelicraft.MODID);
        cocaLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        driedCocaLeaves = new Item().setUnlocalizedName("driedCocaLeaves").setTextureName(modBase + "driedCocaLeaves");
        GameRegistry.registerItem(driedCocaLeaves, "driedCocaLeaves", Psychedelicraft.MODID);
        driedCocaLeaves.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Jenever----------------------------------

        psycheLeaves = new BlockPsycheLeaves();
        psycheLeaves.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(psycheLeaves, "psycheLeaves");

        psycheLog = new BlockPsycheLog().setHardness(1.0F);
        psycheLog.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(psycheLog, "psycheLog");

        psycheSapling = new BlockPsycheSapling().setHardness(1.0F);
        psycheSapling.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(psycheSapling, "psycheSapling");

        juniperBerries = new ItemFoodSpecial(1, 0.5F, true, 15).setUnlocalizedName("juniperBerries").setTextureName(modBase + "juniperBerries");
        GameRegistry.registerItem(juniperBerries, "juniperBerries", Psychedelicraft.MODID);
        juniperBerries.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Coffee----------------------------------

        coffea = new BlockCoffea().setHardness(0.5f);
        coffea.setCreativeTab(null);
        PSRegistryHandler.registerBlockDefault(coffea, ItemBlock.class, "coffea");

        coffeaCherries = new ItemSeeds(coffea, Blocks.farmland).setUnlocalizedName("coffeaCherries").setTextureName(modBase + "coffeaCherries");
        GameRegistry.registerItem(coffeaCherries, "coffeaCherries", Psychedelicraft.MODID);
        coffeaCherries.setCreativeTab(Psychedelicraft.creativeTab);

        coffeeBeans = new Item().setUnlocalizedName("coffeeBeans").setTextureName(modBase + "coffeeBeans");
        GameRegistry.registerItem(coffeeBeans, "coffeeBeans", Psychedelicraft.MODID);
        coffeeBeans.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Peyote----------------------------------

        driedPeyote = new ItemEdibleDrug(new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("driedPeyote").setTextureName(modBase + "driedPeyote");
        GameRegistry.registerItem(driedPeyote, "driedPeyote", Psychedelicraft.MODID);
        driedPeyote.setCreativeTab(Psychedelicraft.creativeTab);

        peyoteJoint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Peyote", 20, 0.003, 0.0015, 0.4f), new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.1f)}, new String[]{"jointPeyote"}, new String[]{"jointPeyoteInUse"}).setUnlocalizedName("jointPeyote").setTextureName(modBase + "jointPeyote");
        GameRegistry.registerItem(peyoteJoint, "peyoteJoint", Psychedelicraft.MODID);
        peyoteJoint.setCreativeTab(Psychedelicraft.creativeTab);

        peyote = new BlockPeyote().setHardness(0.5f);
        peyote.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(peyote, ItemBlock.class, "peyote");
        GameRegistry.registerTileEntity(TileEntityPeyote.class, "peyote");

        //----------------------------------------------------------Harmonium----------------------------------

        harmonium = new ItemHarmonium().setUnlocalizedName("harmonium").setTextureName(modBase + "harmonium");
        GameRegistry.registerItem(harmonium, "harmonium", Psychedelicraft.MODID);
        harmonium.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Digital----------------------------------

        riftJar = new BlockRiftJar().setBlockName("riftJar").setBlockTextureName(modBase + "riftJar");
        riftJar.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(riftJar, ItemRiftJar.class, "riftJar");
        GameRegistry.registerTileEntity(TileEntityRiftJar.class, "riftJar");
        riftJar.setCreativeTab(Psychedelicraft.creativeTab);

        glitched = new BlockGlitched().setBlockName("glitched").setBlockTextureName(modBase + "glitched");
        GameRegistry.registerBlock(glitched, ItemBlock.class, "glitched");
        glitched.setCreativeTab(null);

        EntityRegistry.registerModEntity(EntityRealityRift.class, "realityRift", realityRiftID, mod, 80, 3, false);

        //----------------------------------------------------------Molotov Cocktail----------------------------------

        EntityRegistry.registerModEntity(EntityMolotovCocktail.class, "molotovCocktail", molotovCocktailID, mod, 64, 10, true);

        //----------------------------------------------------------Villager Dealer----------------------------------

        VillagerRegistry.instance().registerVillagerId(villagerDealerProfessionID);

        VillagerRegistry.instance().registerVillageTradeHandler(villagerDealerProfessionID, new VillagerTradeHandlerDrugDealer());

        if (PSConfig.farmerDrugDeals)
            VillagerRegistry.instance().registerVillageTradeHandler(0, new VillagerTradeHandlerFarmer());

        //----------------------------------------------------------Liquids----------------------------------

        beer = new FluidBeer("pscBeer");
        beer.setDrinkable(true);
        beer.setColor(0xaaffcd18);
        beer.setSymbolIconName(modBase + "drinkBeer");
        FluidRegistry.registerFluid(beer);

        jenever = new FluidJenever("pscJenever");
        jenever.setDrinkable(true);
        jenever.setColor(0x44e8f4f8);
        jenever.setSymbolIconName(modBase + "drinkJenever");
        FluidRegistry.registerFluid(jenever);

        coffee = new FluidCoffee("pscCoffee");
        coffee.setDrinkable(true);
        coffee.setColor(0xffa77d55);
        coffee.setSymbolIconName(modBase + "drinkCoffee");
        FluidRegistry.registerFluid(coffee);

        wine = new FluidWine("pscWine");
        wine.setDrinkable(true);
        wine.setColor(0xee3f0822);
        wine.setSymbolIconName(modBase + "drinkWine");
        FluidRegistry.registerFluid(wine);

        peyoteJuice = new FluidDrug("pscPeyoteJuice", new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.9f));
        peyoteJuice.setDrinkable(true);
        peyoteJuice.setColor(0x779bab62);
        peyoteJuice.setSymbolIconName(modBase + "drinkPeyote");
        FluidRegistry.registerFluid(peyoteJuice);

        cocaineFluid = new FluidDrug("pscCocaineFluid", new DrugInfluence("Cocaine", 0, 0.005, 0.01, 50.0f));
        cocaineFluid.setInjectable(true);
        cocaineFluid.setColor(0x44e8f4f8);
        FluidRegistry.registerFluid(cocaineFluid);

        caffeineFluid = new FluidDrug("pscCaffeineFluid", new DrugInfluence("Caffeine", 0, 0.005, 0.01, 85.0f));
        caffeineFluid.setInjectable(true);
        caffeineFluid.setColor(0x66eee2d3);
        FluidRegistry.registerFluid(caffeineFluid);

        cocaTea = new FluidDrug("pscCocaTea", new DrugInfluence("Cocaine", 60, 0.005, 0.002, 0.1f));
        cocaTea.setDrinkable(true);
        cocaTea.setColor(0x44787a36);
        cocaTea.setSymbolIconName(modBase + "drinkCocaTea");
        FluidRegistry.registerFluid(cocaTea);

        cannabisTea = new FluidDrug("pscCannabisTea", new DrugInfluence("Cannabis", 60, 0.005, 0.002, 0.15f));
        cannabisTea.setDrinkable(true);
        cannabisTea.setColor(0x446d6f3c);
        cannabisTea.setSymbolIconName(modBase + "drinkCannabisTea");
        FluidRegistry.registerFluid(cannabisTea);

        vodka = new FluidVodka("pscVodka");
        vodka.setDrinkable(true);
        vodka.setColor(0x44e8f4f8);
        vodka.setSymbolIconName(modBase + "drinkVodka");
        FluidRegistry.registerFluid(vodka);

        slurry = new FluidSlurry("pscSlurry");
        FluidRegistry.registerFluid(slurry);

//        syringe.addEffect(ItemSyringe.damageCocaine, new DrugInfluence[]{new DrugInfluence("Cocaine", 0, 0.005, 0.01, 0.5f)}, 0x55ffffff, "cocaine");
//        syringe.addEffect(ItemSyringe.damageCaffeine, new DrugInfluence[]{new DrugInfluence("Caffeine", 0, 0.005, 0.01, 0.85f)}, 0x552e1404, "caffeine");

        //----------------------------------------------------------Syringe----------------------------------

//        DrinkRegistry.registerDrink("beer", new Drink(modBase + "drinkBeer", new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.06f)));
//        DrinkRegistry.registerDrink("jenever", new Drink(modBase + "drinkJenever", new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.25f)));
//        DrinkRegistry.registerDrink("coffee", new Drink(modBase + "drinkCoffee", new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.3f), new DrugInfluence("Warmth", 0, 0.00, 0.1, 0.8f)));
//        DrinkRegistry.registerDrink("coldCoffee", new Drink(modBase + "drinkCoffee", new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.25f)));
//        DrinkRegistry.registerDrink("wine", new DrinkWine(modBase + "drinkWine"));
//        DrinkRegistry.registerDrink("peyote", new Drink(modBase + "drinkPeyote", new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.9f)));
//        DrinkRegistry.registerDrink("cocaTea", new Drink(modBase + "drinkCocaTea", new DrugInfluence("Cocaine", 60, 0.005, 0.002, 0.1f)));
//        DrinkRegistry.registerDrink("cannabisTea", new Drink(modBase + "drinkCannabisTea", new DrugInfluence("Cannabis", 60, 0.005, 0.002, 0.15f)));
//        DrinkRegistry.registerDrink("vodka", new Drink(modBase + "drinkVodka", new DrugInfluence("Alcohol", 20, 0.003, 0.002, 0.5f)));

//        registerSpecialIcon("beer", woodenMug, modBase + "woodenMugBeer");
//        registerSpecialIcon("jenever", woodenMug, modBase + "woodenMugJenever");
//        registerSpecialIcon("coffee", woodenMug, modBase + "woodenMugCoffee");
//        registerSpecialIcon("coldCoffee", woodenMug, modBase + "woodenMugCoffee");
//        registerSpecialIcon("wine", woodenMug, modBase + "woodenMugWine");
//        registerSpecialIcon("peyote", woodenMug, modBase + "woodenMugPeyote");
//        registerSpecialIcon("cocaTea", woodenMug, modBase + "woodenMugCocaTea");
//        registerSpecialIcon("cannabisTea", woodenMug, modBase + "woodenMugCocaTea");
//        registerSpecialIcon("vodka", woodenMug, modBase + "woodenMugVodka");
//
//        registerSpecialIcon("beer", woodenBowlDrug, modBase + "woodenBowlBeer");
//        registerSpecialIcon("jenever", woodenBowlDrug, modBase + "woodenBowlJenever");
//        registerSpecialIcon("coffee", woodenBowlDrug, modBase + "woodenBowlCoffee");
//        registerSpecialIcon("coldCoffee", woodenBowlDrug, modBase + "woodenBowlCoffee");
//        registerSpecialIcon("wine", woodenBowlDrug, modBase + "woodenBowlWine");
//        registerSpecialIcon("peyote", woodenBowlDrug, modBase + "woodenBowlPeyote");
//        registerSpecialIcon("cocaTea", woodenBowlDrug, modBase + "woodenBowlCocaTea");
//        registerSpecialIcon("cannabisTea", woodenBowlDrug, modBase + "woodenBowlCocaTea");
//        registerSpecialIcon("vodka", woodenBowlDrug, modBase + "woodenBowlVodka");
//
//        registerSpecialIcon("beer", glassChalice, modBase + "glassChaliceBeer");
//        registerSpecialIcon("jenever", glassChalice, modBase + "glassChaliceJenever");
//        registerSpecialIcon("coffee", glassChalice, modBase + "glassChaliceCoffee");
//        registerSpecialIcon("coldCoffee", glassChalice, modBase + "glassChaliceCoffee");
//        registerSpecialIcon("wine", glassChalice, modBase + "glassChaliceWine");
//        registerSpecialIcon("peyote", glassChalice, modBase + "glassChalicePeyote");
//        registerSpecialIcon("cocaTea", glassChalice, modBase + "glassChaliceCocaTea");
//        registerSpecialIcon("cannabisTea", glassChalice, modBase + "glassChaliceCocaTea");
//        registerSpecialIcon("vodka", glassChalice, modBase + "glassChaliceVodka");
    }

    public static void load(FMLInitializationEvent event, Psychedelicraft mod)
    {
    }

    public static void registerBlockDefault(Block block, String id)
    {
        GameRegistry.registerBlock(block, id);
        block.setBlockName(id);
        block.setBlockTextureName(modBase + id);
//        block.setCreativeTab(Psychedelicraft.creativeTab);
    }

    public static void registerBlockDefault(Block block, Class<? extends ItemBlock> itemBlock, String id)
    {
        GameRegistry.registerBlock(block, itemBlock, id);
        block.setBlockName(id);
        block.setBlockTextureName(modBase + id);
//        block.setCreativeTab(Psychedelicraft.creativeTab);
    }
}
