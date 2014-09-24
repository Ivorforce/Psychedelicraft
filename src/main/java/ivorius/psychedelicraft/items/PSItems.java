/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.entities.DrugInfluence;
import ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by lukas on 25.04.14.
 */
public class PSItems
{
    public static ItemDrinkHolder woodenMug;
    public static ItemDrinkHolder woodenBowlDrug;
    public static ItemDrinkHolder glassChalice;

    public static Item wineGrapes;
    public static Item molotovCocktail;

    public static Item cannabisSeeds;
    public static Item cannabisLeaf;
    public static Item cannabisBuds;
    public static Item driedCannabisBuds;
    public static Item driedCannabisLeaves;
    public static ItemSmokingPipe pipe;
    public static ItemBong bong;
    public static Item hashMuffin;

    public static Item magicMushroomsBrown;
    public static Item magicMushroomsRed;

    public static Item tobaccoLeaf;
    public static Item tobaccoSeeds;
    public static Item driedTobacco;
    public static Item cigarette;
    public static Item cigar;
    public static Item joint;

    public static Item cocaSeeds;
    public static Item cocaLeaf;
    public static Item driedCocaLeaves;

    public static ItemSyringe syringe;

    public static Item juniperBerries;

    public static Item coffeaCherries;

    public static Item coffeeBeans;

    public static Item driedPeyote;
    public static Item peyoteJoint;

    public static Item harmonium;

    public static void preInit(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        //----------------------------------------------------------Containers----------------------------------

        syringe = (ItemSyringe) new ItemSyringe().setUnlocalizedName("syringe").setTextureName(Psychedelicraft.textureBase + "syringe");
        GameRegistry.registerItem(syringe, "syringe", Psychedelicraft.MODID);
        syringe.setCreativeTab(Psychedelicraft.creativeTab);

        woodenBowlDrug = (ItemDrinkHolder) new ItemDrinkHolder().setUnlocalizedName("woodenBowlDrug").setTextureName("bowl");
        woodenBowlDrug.addEmptySelfToCreativeMenu = false;
        GameRegistry.registerItem(woodenBowlDrug, "psycheWoodenBowl", Psychedelicraft.MODID);
        woodenBowlDrug.setCreativeTab(Psychedelicraft.creativeTab);
        DrinkRegistry.registerDrinkHolder(woodenBowlDrug);

        woodenMug = (ItemDrinkHolder) new ItemDrinkHolder().setUnlocalizedName("woodenMug").setTextureName(Psychedelicraft.textureBase + "woodenMug");
        GameRegistry.registerItem(woodenMug, "woodenMug", Psychedelicraft.MODID);
        woodenMug.setCreativeTab(Psychedelicraft.creativeTab);
        DrinkRegistry.registerDrinkHolder(woodenMug);

        glassChalice = (ItemDrinkHolder) new ItemDrinkHolder().setUnlocalizedName("glassChalice").setTextureName(Psychedelicraft.textureBase + "glassChalice");
        GameRegistry.registerItem(glassChalice, "glassChalice", Psychedelicraft.MODID);
        glassChalice.setCreativeTab(Psychedelicraft.creativeTab);
        DrinkRegistry.registerDrinkHolder(glassChalice);

        pipe = (ItemSmokingPipe) (new ItemSmokingPipe().setUnlocalizedName("smokingPipe").setTextureName(Psychedelicraft.textureBase + "smokingPipe"));
        GameRegistry.registerItem(pipe, "smokingPipe", Psychedelicraft.MODID);
        pipe.setCreativeTab(Psychedelicraft.creativeTab);

        bong = (ItemBong) new ItemBong().setUnlocalizedName("bong").setTextureName(Psychedelicraft.textureBase + "bong");
        GameRegistry.registerItem(bong, "bong", Psychedelicraft.MODID);
        bong.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Wine----------------------------------

        wineGrapes = (new ItemWineGrapes(1, 0.5F, true)).setUnlocalizedName("wineGrapes").setTextureName(Psychedelicraft.textureBase + "wineGrapes");
        GameRegistry.registerItem(wineGrapes, "wineGrapes", Psychedelicraft.MODID);
        wineGrapes.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Molotov Cocktail----------------------------------

        int entityMolotovCocktailID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID);
        EntityRegistry.registerModEntity(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID, mod, 64, 10, true);

        molotovCocktail = new ItemMolotovCocktail().setUnlocalizedName("molotovCocktail").setTextureName(Psychedelicraft.textureBase + "molotovCocktail");
        GameRegistry.registerItem(molotovCocktail, "molotovCocktail", Psychedelicraft.MODID);
        molotovCocktail.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Weed----------------------------------

        cannabisSeeds = new ItemSeeds(PSBlocks.cannabisPlant, Blocks.farmland).setUnlocalizedName("cannabisSeeds").setTextureName(Psychedelicraft.textureBase + "cannabisSeeds");
        GameRegistry.registerItem(cannabisSeeds, "cannabisSeeds", Psychedelicraft.MODID);
        cannabisSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        cannabisLeaf = new Item().setUnlocalizedName("cannabisLeaf").setTextureName(Psychedelicraft.textureBase + "cannabisLeaf");
        GameRegistry.registerItem(cannabisLeaf, "cannabisLeaf", Psychedelicraft.MODID);
        cannabisLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        cannabisBuds = new Item().setUnlocalizedName("cannabisBuds").setTextureName(Psychedelicraft.textureBase + "cannabisBuds");
        GameRegistry.registerItem(cannabisBuds, "cannabisBuds", Psychedelicraft.MODID);
        cannabisBuds.setCreativeTab(Psychedelicraft.creativeTab);

        driedCannabisBuds = new Item().setUnlocalizedName("driedCannabisBuds").setTextureName(Psychedelicraft.textureBase + "driedCannabisBuds");
        GameRegistry.registerItem(driedCannabisBuds, "driedCannabisBuds", Psychedelicraft.MODID);
        driedCannabisBuds.setCreativeTab(Psychedelicraft.creativeTab);

        driedCannabisLeaves = new Item().setUnlocalizedName("driedCannabisLeaves").setTextureName(Psychedelicraft.textureBase + "driedCannabisLeaves");
        GameRegistry.registerItem(driedCannabisLeaves, "driedCannabisLeaves", Psychedelicraft.MODID);
        driedCannabisLeaves.setCreativeTab(Psychedelicraft.creativeTab);

        hashMuffin = new ItemHashMuffin().setUnlocalizedName("hashMuffin").setTextureName(Psychedelicraft.textureBase + "hashMuffin");
        GameRegistry.registerItem(hashMuffin, "hashMuffin", Psychedelicraft.MODID);
        hashMuffin.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Magic Mushrooms----------------------------------

        magicMushroomsBrown = new ItemEdibleDrug(new DrugInfluence("BrownShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsBrown").setTextureName(Psychedelicraft.textureBase + "magicMushroomsBrown");
        GameRegistry.registerItem(magicMushroomsBrown, "brownMagicMushrooms", Psychedelicraft.MODID);
        magicMushroomsBrown.setCreativeTab(Psychedelicraft.creativeTab);

        magicMushroomsRed = new ItemEdibleDrug(new DrugInfluence("RedShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsRed").setTextureName(Psychedelicraft.textureBase + "magicMushroomsRed");
        GameRegistry.registerItem(magicMushroomsRed, "redMagicMushrooms", Psychedelicraft.MODID);
        magicMushroomsRed.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Tobacco----------------------------------

        tobaccoLeaf = new Item().setUnlocalizedName("tobaccoLeaf").setTextureName(Psychedelicraft.textureBase + "tobaccoLeaf");
        GameRegistry.registerItem(tobaccoLeaf, "tobaccoLeaf", Psychedelicraft.MODID);
        tobaccoLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        tobaccoSeeds = new ItemSeeds(PSBlocks.tobaccoPlant, Blocks.farmland).setUnlocalizedName("tobaccoSeeds").setTextureName(Psychedelicraft.textureBase + "tobaccoSeeds");
        GameRegistry.registerItem(tobaccoSeeds, "tobaccoSeeds", Psychedelicraft.MODID);
        tobaccoSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        driedTobacco = new Item().setUnlocalizedName("driedTobacco").setTextureName(Psychedelicraft.textureBase + "driedTobacco");
        GameRegistry.registerItem(driedTobacco, "driedTobacco", Psychedelicraft.MODID);
        driedTobacco.setCreativeTab(Psychedelicraft.creativeTab);

        cigarette = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigarette"}, new String[]{"cigaretteInUse"}).setUnlocalizedName("cigarette").setTextureName(Psychedelicraft.textureBase + "cigarette");
        GameRegistry.registerItem(cigarette, "cigarette", Psychedelicraft.MODID);
        cigarette.setCreativeTab(Psychedelicraft.creativeTab);

        cigar = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigar", "cigar1", "cigar2", "cigar3"}, new String[]{"cigarInUse", "cigar1InUse", "cigar2InUse", "cigar3InUse"}).setUnlocalizedName("cigar").setTextureName(Psychedelicraft.textureBase + "cigar");
        GameRegistry.registerItem(cigar, "cigar", Psychedelicraft.MODID);
        cigar.setCreativeTab(Psychedelicraft.creativeTab);

        joint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.20f)}, new String[]{"joint"}, new String[]{"jointInUse"}).setUnlocalizedName("joint").setTextureName(Psychedelicraft.textureBase + "joint");
        GameRegistry.registerItem(joint, "joint", Psychedelicraft.MODID);
        joint.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Cocaine----------------------------------

        cocaSeeds = new ItemSeeds(PSBlocks.cocaPlant, Blocks.farmland).setUnlocalizedName("cocaSeeds").setTextureName(Psychedelicraft.textureBase + "cocaSeeds");
        GameRegistry.registerItem(cocaSeeds, "cocaSeeds", Psychedelicraft.MODID);
        cocaSeeds.setCreativeTab(Psychedelicraft.creativeTab);

        cocaLeaf = new Item().setUnlocalizedName("cocaLeaf").setTextureName(Psychedelicraft.textureBase + "cocaLeaf");
        GameRegistry.registerItem(cocaLeaf, "cocaLeaf", Psychedelicraft.MODID);
        cocaLeaf.setCreativeTab(Psychedelicraft.creativeTab);

        driedCocaLeaves = new Item().setUnlocalizedName("driedCocaLeaves").setTextureName(Psychedelicraft.textureBase + "driedCocaLeaves");
        GameRegistry.registerItem(driedCocaLeaves, "driedCocaLeaves", Psychedelicraft.MODID);
        driedCocaLeaves.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Jenever----------------------------------

        juniperBerries = new ItemFood(1, 0.5F, true).setUnlocalizedName("juniperBerries").setTextureName(Psychedelicraft.textureBase + "juniperBerries");
        GameRegistry.registerItem(juniperBerries, "juniperBerries", Psychedelicraft.MODID);
        juniperBerries.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Coffee----------------------------------

        coffeaCherries = new ItemSeeds(PSBlocks.coffea, Blocks.farmland).setUnlocalizedName("coffeaCherries").setTextureName(Psychedelicraft.textureBase + "coffeaCherries");
        GameRegistry.registerItem(coffeaCherries, "coffeaCherries", Psychedelicraft.MODID);
        coffeaCherries.setCreativeTab(Psychedelicraft.creativeTab);

        coffeeBeans = new Item().setUnlocalizedName("coffeeBeans").setTextureName(Psychedelicraft.textureBase + "coffeeBeans");
        GameRegistry.registerItem(coffeeBeans, "coffeeBeans", Psychedelicraft.MODID);
        coffeeBeans.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Peyote----------------------------------

        driedPeyote = new ItemEdibleDrug(new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("driedPeyote").setTextureName(Psychedelicraft.textureBase + "driedPeyote");
        GameRegistry.registerItem(driedPeyote, "driedPeyote", Psychedelicraft.MODID);
        driedPeyote.setCreativeTab(Psychedelicraft.creativeTab);

        peyoteJoint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Peyote", 20, 0.003, 0.0015, 0.4f), new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.1f)}, new String[]{"jointPeyote"}, new String[]{"jointPeyoteInUse"}).setUnlocalizedName("jointPeyote").setTextureName(Psychedelicraft.textureBase + "jointPeyote");
        GameRegistry.registerItem(peyoteJoint, "peyoteJoint", Psychedelicraft.MODID);
        peyoteJoint.setCreativeTab(Psychedelicraft.creativeTab);

        //----------------------------------------------------------Harmonium----------------------------------

        harmonium = new ItemHarmonium().setUnlocalizedName("harmonium").setTextureName(Psychedelicraft.textureBase + "harmonium");
        GameRegistry.registerItem(harmonium, "harmonium", Psychedelicraft.MODID);
        harmonium.setCreativeTab(Psychedelicraft.creativeTab);
    }

    public static void preInitEnd(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        syringe.addEffect(ItemSyringe.damageCocaine, new DrugInfluence[]{new DrugInfluence("Cocaine", 0, 0.005, 0.01, 0.5f)}, 0x55ffffff, "cocaine");
        syringe.addEffect(ItemSyringe.damageCaffeine, new DrugInfluence[]{new DrugInfluence("Caffeine", 0, 0.005, 0.01, 0.85f)}, 0x552e1404, "caffeine");

        DrinkRegistry.registerDrink("beer", new Drink(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.06f)));
        DrinkRegistry.registerDrink("jenever", new Drink(new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.25f)));
        DrinkRegistry.registerDrink("coffee", new Drink(new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.3f), new DrugInfluence("Warmth", 0, 0.00, 0.1, 0.8f)));
        DrinkRegistry.registerDrink("coldCoffee", new Drink(new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.25f)));
        DrinkRegistry.registerDrink("wine", new DrinkWine());
        DrinkRegistry.registerDrink("peyote", new Drink(new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.9f)));

        DrinkRegistry.registerSpecialIcon("beer", PSItems.woodenMug, Psychedelicraft.textureBase + "woodenMugBeer");
        DrinkRegistry.registerSpecialIcon("jenever", PSItems.woodenMug, Psychedelicraft.textureBase + "woodenMugJenever");
        DrinkRegistry.registerSpecialIcon("coffee", PSItems.woodenMug, Psychedelicraft.textureBase + "woodenMugCoffee");
        DrinkRegistry.registerSpecialIcon("coldCoffee", PSItems.woodenMug, Psychedelicraft.textureBase + "woodenMugCoffee");
        DrinkRegistry.registerSpecialIcon("wine", PSItems.woodenMug, Psychedelicraft.textureBase + "woodenMugWine");
        DrinkRegistry.registerSpecialIcon("peyote", PSItems.woodenMug, Psychedelicraft.textureBase + "woodenMugPeyote");

        DrinkRegistry.registerSpecialIcon("beer", PSItems.woodenBowlDrug, Psychedelicraft.textureBase + "woodenBowlBeer");
        DrinkRegistry.registerSpecialIcon("jenever", PSItems.woodenBowlDrug, Psychedelicraft.textureBase + "woodenBowlJenever");
        DrinkRegistry.registerSpecialIcon("coffee", PSItems.woodenBowlDrug, Psychedelicraft.textureBase + "woodenBowlCoffee");
        DrinkRegistry.registerSpecialIcon("coldCoffee", PSItems.woodenBowlDrug, Psychedelicraft.textureBase + "woodenBowlCoffee");
        DrinkRegistry.registerSpecialIcon("wine", PSItems.woodenBowlDrug, Psychedelicraft.textureBase + "woodenBowlWine");
        DrinkRegistry.registerSpecialIcon("peyote", PSItems.woodenBowlDrug, Psychedelicraft.textureBase + "woodenBowlPeyote");

        DrinkRegistry.registerSpecialIcon("beer", PSItems.glassChalice, Psychedelicraft.textureBase + "glassChaliceBeer");
        DrinkRegistry.registerSpecialIcon("jenever", PSItems.glassChalice, Psychedelicraft.textureBase + "glassChaliceJenever");
        DrinkRegistry.registerSpecialIcon("coffee", PSItems.glassChalice, Psychedelicraft.textureBase + "glassChaliceCoffee");
        DrinkRegistry.registerSpecialIcon("coldCoffee", PSItems.glassChalice, Psychedelicraft.textureBase + "glassChaliceCoffee");
        DrinkRegistry.registerSpecialIcon("wine", PSItems.glassChalice, Psychedelicraft.textureBase + "glassChaliceWine");
        DrinkRegistry.registerSpecialIcon("peyote", PSItems.glassChalice, Psychedelicraft.textureBase + "glassChalicePeyote");
    }
}
