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
import ivorius.psychedelicraft.PSConfig;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.*;
import ivorius.psychedelicraft.entities.*;
import ivorius.psychedelicraft.items.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.ResourceLocation;

import static ivorius.psychedelicraft.Psychedelicraft.textureBase;
import static ivorius.psychedelicraft.blocks.PSBlocks.*;
import static ivorius.psychedelicraft.items.DrinkRegistry.registerSpecialIcon;
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

        syringe = (ItemSyringe) new ItemSyringe().setUnlocalizedName("syringe").setTextureName(textureBase + "syringe");
        GameRegistry.registerItem(syringe, "syringe", Psychedelicraft.MODID);
        syringe.setCreativeTab(Psychedelicraft.creativeTab);

        woodenBowlDrug = (ItemDrinkHolder) new ItemWoodenBowlDrug(1).setUnlocalizedName("woodenBowlDrug").setTextureName("bowl");
        woodenBowlDrug.addEmptySelfToCreativeMenu = false;
        GameRegistry.registerItem(woodenBowlDrug, "psycheWoodenBowl", Psychedelicraft.MODID);
        woodenBowlDrug.setCreativeTab(Psychedelicraft.drinksTab);
        DrinkRegistry.registerDrinkHolder(woodenBowlDrug);

        woodenMug = (ItemDrinkHolder) new ItemDrinkable(1).setUnlocalizedName("woodenMug").setTextureName(textureBase + "woodenMug");
        GameRegistry.registerItem(woodenMug, "woodenMug", Psychedelicraft.MODID);
        woodenMug.setCreativeTab(Psychedelicraft.drinksTab);
        DrinkRegistry.registerDrinkHolder(woodenMug);

        glassChalice = (ItemDrinkHolder) new ItemDrinkable(1).setUnlocalizedName("glassChalice").setTextureName(textureBase + "glassChalice");
        GameRegistry.registerItem(glassChalice, "glassChalice", Psychedelicraft.MODID);
        glassChalice.setCreativeTab(Psychedelicraft.drinksTab);
        DrinkRegistry.registerDrinkHolder(glassChalice);

        bottle = (ItemDrinkHolder) new ItemBottle().setUnlocalizedName("ygcBottle").setTextureName(textureBase + "bottle");
        GameRegistry.registerItem(bottle, "bottle", Psychedelicraft.MODID);
        bottle.setCreativeTab(Psychedelicraft.drinksTab);
        DrinkRegistry.registerDrinkHolder(bottle);

        molotovCocktail = (ItemDrinkHolder) new ItemMolotovCocktail().setUnlocalizedName("molotovCocktail").setTextureName(textureBase + "molotovCocktail");
        GameRegistry.registerItem(molotovCocktail, "molotovCocktail", Psychedelicraft.MODID);
        molotovCocktail.setCreativeTab(Psychedelicraft.weaponsTab);
        DrinkRegistry.registerDrinkHolder(molotovCocktail);

        pipe = (ItemSmokingPipe) (new ItemSmokingPipe().setUnlocalizedName("smokingPipe").setTextureName(textureBase + "smokingPipe"));
        GameRegistry.registerItem(pipe, "smokingPipe", Psychedelicraft.MODID);
        pipe.setCreativeTab(Psychedelicraft.creativeTab);

        bong = (ItemBong) new ItemBong().setUnlocalizedName("bong").setTextureName(textureBase + "bong");
        GameRegistry.registerItem(bong, "bong", Psychedelicraft.MODID);
        bong.setCreativeTab(Psychedelicraft.creativeTab);

        dryingTable = new BlockDryingTable().setHardness(1.0f);
        dryingTable.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(dryingTable, "dryingTable");
        GameRegistry.registerTileEntity(TileEntityDryingTable.class, "dryingTable");

        barrel = new BlockBarrel().setHardness(1.0F);
        barrel.setCreativeTab(Psychedelicraft.drinksTab);
        PSRegistryHandler.registerBlockDefault(barrel, ItemBarrel.class, "barrel");
        GameRegistry.registerTileEntity(TileEntityBarrel.class, "barrel");
        itemBarrel = (ItemBarrel) Item.getItemFromBlock(barrel);

        //----------------------------------------------------------Wine----------------------------------

        wineGrapeLattice = new BlockWineGrapeLattice().setHardness(0.3F);
        wineGrapeLattice.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(wineGrapeLattice, ItemBlock.class, "wineGrapeLattice");

        wineGrapes = (new ItemWineGrapes(1, 0.5F, true, 15)).setUnlocalizedName("wineGrapes").setTextureName(textureBase + "wineGrapes");
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

        cannabisSeeds = new ItemSeeds(cannabisPlant, Blocks.farmland).setUnlocalizedName("cannabisSeeds").setTextureName(textureBase + "cannabisSeeds");
        GameRegistry.registerItem(cannabisSeeds, "cannabisSeeds", Psychedelicraft.MODID);
        cannabisSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        cannabisLeaf = new Item().setUnlocalizedName("cannabisLeaf").setTextureName(textureBase + "cannabisLeaf");
        GameRegistry.registerItem(cannabisLeaf, "cannabisLeaf", Psychedelicraft.MODID);
        cannabisLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        cannabisBuds = new Item().setUnlocalizedName("cannabisBuds").setTextureName(textureBase + "cannabisBuds");
        GameRegistry.registerItem(cannabisBuds, "cannabisBuds", Psychedelicraft.MODID);
        cannabisBuds.setCreativeTab(Psychedelicraft.creativeTab);

        driedCannabisBuds = new Item().setUnlocalizedName("driedCannabisBuds").setTextureName(textureBase + "driedCannabisBuds");
        GameRegistry.registerItem(driedCannabisBuds, "driedCannabisBuds", Psychedelicraft.MODID);
        driedCannabisBuds.setCreativeTab(Psychedelicraft.creativeTab);

        driedCannabisLeaves = new Item().setUnlocalizedName("driedCannabisLeaves").setTextureName(textureBase + "driedCannabisLeaves");
        GameRegistry.registerItem(driedCannabisLeaves, "driedCannabisLeaves", Psychedelicraft.MODID);
        driedCannabisLeaves.setCreativeTab(Psychedelicraft.creativeTab);

        hashMuffin = new ItemHashMuffin().setUnlocalizedName("hashMuffin").setTextureName(textureBase + "hashMuffin");
        GameRegistry.registerItem(hashMuffin, "hashMuffin", Psychedelicraft.MODID);
        hashMuffin.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Magic Mushrooms----------------------------------

        magicMushroomsBrown = new ItemEdibleDrug(new DrugInfluence("BrownShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsBrown").setTextureName(textureBase + "magicMushroomsBrown");
        GameRegistry.registerItem(magicMushroomsBrown, "brownMagicMushrooms", Psychedelicraft.MODID);
        magicMushroomsBrown.setCreativeTab(Psychedelicraft.creativeTab);

        magicMushroomsRed = new ItemEdibleDrug(new DrugInfluence("RedShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsRed").setTextureName(textureBase + "magicMushroomsRed");
        GameRegistry.registerItem(magicMushroomsRed, "redMagicMushrooms", Psychedelicraft.MODID);
        magicMushroomsRed.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Tobacco----------------------------------

        tobaccoPlant = new BlockTobaccoPlant().setHardness(0.5f);
        tobaccoPlant.setCreativeTab(null);
        PSRegistryHandler.registerBlockDefault(tobaccoPlant, "tobaccoPlant");

        tobaccoLeaf = new Item().setUnlocalizedName("tobaccoLeaf").setTextureName(textureBase + "tobaccoLeaf");
        GameRegistry.registerItem(tobaccoLeaf, "tobaccoLeaf", Psychedelicraft.MODID);
        tobaccoLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        tobaccoSeeds = new ItemSeeds(tobaccoPlant, Blocks.farmland).setUnlocalizedName("tobaccoSeeds").setTextureName(textureBase + "tobaccoSeeds");
        GameRegistry.registerItem(tobaccoSeeds, "tobaccoSeeds", Psychedelicraft.MODID);
        tobaccoSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        driedTobacco = new Item().setUnlocalizedName("driedTobacco").setTextureName(textureBase + "driedTobacco");
        GameRegistry.registerItem(driedTobacco, "driedTobacco", Psychedelicraft.MODID);
        driedTobacco.setCreativeTab(Psychedelicraft.creativeTab);

        cigarette = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigarette"}, new String[]{"cigaretteInUse"}).setUnlocalizedName("cigarette").setTextureName(textureBase + "cigarette");
        GameRegistry.registerItem(cigarette, "cigarette", Psychedelicraft.MODID);
        cigarette.setCreativeTab(Psychedelicraft.creativeTab);

        cigar = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigar", "cigar1", "cigar2", "cigar3"}, new String[]{"cigarInUse", "cigar1InUse", "cigar2InUse", "cigar3InUse"}).setUnlocalizedName("cigar").setTextureName(textureBase + "cigar");
        GameRegistry.registerItem(cigar, "cigar", Psychedelicraft.MODID);
        cigar.setCreativeTab(Psychedelicraft.creativeTab);

        joint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.20f)}, new String[]{"joint"}, new String[]{"jointInUse"}).setUnlocalizedName("joint").setTextureName(textureBase + "joint");
        GameRegistry.registerItem(joint, "joint", Psychedelicraft.MODID);
        joint.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Cocaine----------------------------------

        cocaPlant = new BlockCocaPlant().setHardness(0.5f);
        cocaPlant.setCreativeTab(null);
        PSRegistryHandler.registerBlockDefault(cocaPlant, ItemBlock.class, "cocaPlant");

        cocaSeeds = new ItemSeeds(cocaPlant, Blocks.farmland).setUnlocalizedName("cocaSeeds").setTextureName(textureBase + "cocaSeeds");
        GameRegistry.registerItem(cocaSeeds, "cocaSeeds", Psychedelicraft.MODID);
        cocaSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        cocaLeaf = new Item().setUnlocalizedName("cocaLeaf").setTextureName(textureBase + "cocaLeaf");
        GameRegistry.registerItem(cocaLeaf, "cocaLeaf", Psychedelicraft.MODID);
        cocaLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        driedCocaLeaves = new Item().setUnlocalizedName("driedCocaLeaves").setTextureName(textureBase + "driedCocaLeaves");
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

        juniperBerries = new ItemFoodSpecial(1, 0.5F, true, 15).setUnlocalizedName("juniperBerries").setTextureName(textureBase + "juniperBerries");
        GameRegistry.registerItem(juniperBerries, "juniperBerries", Psychedelicraft.MODID);
        juniperBerries.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Coffee----------------------------------

        coffea = new BlockCoffea().setHardness(0.5f);
        coffea.setCreativeTab(null);
        PSRegistryHandler.registerBlockDefault(coffea, ItemBlock.class, "coffea");

        coffeaCherries = new ItemSeeds(coffea, Blocks.farmland).setUnlocalizedName("coffeaCherries").setTextureName(textureBase + "coffeaCherries");
        GameRegistry.registerItem(coffeaCherries, "coffeaCherries", Psychedelicraft.MODID);
        coffeaCherries.setCreativeTab(Psychedelicraft.creativeTab);

        coffeeBeans = new Item().setUnlocalizedName("coffeeBeans").setTextureName(textureBase + "coffeeBeans");
        GameRegistry.registerItem(coffeeBeans, "coffeeBeans", Psychedelicraft.MODID);
        coffeeBeans.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Peyote----------------------------------

        driedPeyote = new ItemEdibleDrug(new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("driedPeyote").setTextureName(textureBase + "driedPeyote");
        GameRegistry.registerItem(driedPeyote, "driedPeyote", Psychedelicraft.MODID);
        driedPeyote.setCreativeTab(Psychedelicraft.creativeTab);

        peyoteJoint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Peyote", 20, 0.003, 0.0015, 0.4f), new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.1f)}, new String[]{"jointPeyote"}, new String[]{"jointPeyoteInUse"}).setUnlocalizedName("jointPeyote").setTextureName(textureBase + "jointPeyote");
        GameRegistry.registerItem(peyoteJoint, "peyoteJoint", Psychedelicraft.MODID);
        peyoteJoint.setCreativeTab(Psychedelicraft.creativeTab);

        peyote = new BlockPeyote().setHardness(0.5f);
        peyote.setCreativeTab(Psychedelicraft.creativeTab);
        PSRegistryHandler.registerBlockDefault(peyote, ItemBlock.class, "peyote");
        GameRegistry.registerTileEntity(TileEntityPeyote.class, "peyote");

        //----------------------------------------------------------Harmonium----------------------------------

        harmonium = new ItemHarmonium().setUnlocalizedName("harmonium").setTextureName(textureBase + "harmonium");
        GameRegistry.registerItem(harmonium, "harmonium", Psychedelicraft.MODID);
        harmonium.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Digital----------------------------------

        riftJar = new BlockRiftJar().setBlockName("riftJar").setBlockTextureName(textureBase + "riftJar");
        riftJar.setCreativeTab(Psychedelicraft.creativeTab);
        GameRegistry.registerBlock(riftJar, ItemRiftJar.class, "riftJar");
        GameRegistry.registerTileEntity(TileEntityRiftJar.class, "riftJar");
        riftJar.setCreativeTab(Psychedelicraft.creativeTab);

        glitched = new BlockGlitched().setBlockName("glitched").setBlockTextureName(textureBase + "glitched");
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

        //----------------------------------------------------------End----------------------------------

        syringe.addEffect(ItemSyringe.damageCocaine, new DrugInfluence[]{new DrugInfluence("Cocaine", 0, 0.005, 0.01, 0.5f)}, 0x55ffffff, "cocaine");
        syringe.addEffect(ItemSyringe.damageCaffeine, new DrugInfluence[]{new DrugInfluence("Caffeine", 0, 0.005, 0.01, 0.85f)}, 0x552e1404, "caffeine");

        DrinkRegistry.registerDrink("beer", new Drink(textureBase + "drinkBeer", new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.06f)));
        DrinkRegistry.registerDrink("jenever", new Drink(textureBase + "drinkJenever", new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.25f)));
        DrinkRegistry.registerDrink("coffee", new Drink(textureBase + "drinkCoffee", new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.3f), new DrugInfluence("Warmth", 0, 0.00, 0.1, 0.8f)));
        DrinkRegistry.registerDrink("coldCoffee", new Drink(textureBase + "drinkCoffee", new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.25f)));
        DrinkRegistry.registerDrink("wine", new DrinkWine(textureBase + "drinkWine"));
        DrinkRegistry.registerDrink("peyote", new Drink(textureBase + "drinkPeyote", new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.9f)));
        DrinkRegistry.registerDrink("cocaTea", new Drink(textureBase + "drinkCocaTea", new DrugInfluence("Cocaine", 60, 0.005, 0.002, 0.1f)));
        DrinkRegistry.registerDrink("cannabisTea", new Drink(textureBase + "drinkCannabisTea", new DrugInfluence("Cannabis", 60, 0.005, 0.002, 0.15f)));
        DrinkRegistry.registerDrink("vodka", new Drink(textureBase + "drinkVodka", new DrugInfluence("Alcohol", 20, 0.003, 0.002, 0.5f)));

        registerSpecialIcon("beer", woodenMug, textureBase + "woodenMugBeer");
        registerSpecialIcon("jenever", woodenMug, textureBase + "woodenMugJenever");
        registerSpecialIcon("coffee", woodenMug, textureBase + "woodenMugCoffee");
        registerSpecialIcon("coldCoffee", woodenMug, textureBase + "woodenMugCoffee");
        registerSpecialIcon("wine", woodenMug, textureBase + "woodenMugWine");
        registerSpecialIcon("peyote", woodenMug, textureBase + "woodenMugPeyote");
        registerSpecialIcon("cocaTea", woodenMug, textureBase + "woodenMugCocaTea");
        registerSpecialIcon("cannabisTea", woodenMug, textureBase + "woodenMugCocaTea");
        registerSpecialIcon("vodka", woodenMug, textureBase + "woodenMugVodka");

        registerSpecialIcon("beer", woodenBowlDrug, textureBase + "woodenBowlBeer");
        registerSpecialIcon("jenever", woodenBowlDrug, textureBase + "woodenBowlJenever");
        registerSpecialIcon("coffee", woodenBowlDrug, textureBase + "woodenBowlCoffee");
        registerSpecialIcon("coldCoffee", woodenBowlDrug, textureBase + "woodenBowlCoffee");
        registerSpecialIcon("wine", woodenBowlDrug, textureBase + "woodenBowlWine");
        registerSpecialIcon("peyote", woodenBowlDrug, textureBase + "woodenBowlPeyote");
        registerSpecialIcon("cocaTea", woodenBowlDrug, textureBase + "woodenBowlCocaTea");
        registerSpecialIcon("cannabisTea", woodenBowlDrug, textureBase + "woodenBowlCocaTea");
        registerSpecialIcon("vodka", woodenBowlDrug, textureBase + "woodenBowlVodka");

        registerSpecialIcon("beer", glassChalice, textureBase + "glassChaliceBeer");
        registerSpecialIcon("jenever", glassChalice, textureBase + "glassChaliceJenever");
        registerSpecialIcon("coffee", glassChalice, textureBase + "glassChaliceCoffee");
        registerSpecialIcon("coldCoffee", glassChalice, textureBase + "glassChaliceCoffee");
        registerSpecialIcon("wine", glassChalice, textureBase + "glassChaliceWine");
        registerSpecialIcon("peyote", glassChalice, textureBase + "glassChalicePeyote");
        registerSpecialIcon("cocaTea", glassChalice, textureBase + "glassChaliceCocaTea");
        registerSpecialIcon("cannabisTea", glassChalice, textureBase + "glassChaliceCocaTea");
        registerSpecialIcon("vodka", glassChalice, textureBase + "glassChaliceVodka");
    }

    public static void load(FMLInitializationEvent event, Psychedelicraft mod)
    {
    }

    public static void registerBlockDefault(Block block, String id)
    {
        GameRegistry.registerBlock(block, id);
        block.setBlockName(id);
        block.setBlockTextureName(textureBase + id);
//        block.setCreativeTab(Psychedelicraft.creativeTab);
    }

    public static void registerBlockDefault(Block block, Class<? extends ItemBlock> itemBlock, String id)
    {
        GameRegistry.registerBlock(block, itemBlock, id);
        block.setBlockName(id);
        block.setBlockTextureName(textureBase + id);
//        block.setCreativeTab(Psychedelicraft.creativeTab);
    }
}
