/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.items;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.blocks.PSBlocks;
import net.ivorius.psychedelicraft.entities.DrugInfluence;
import net.ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import net.minecraft.creativetab.CreativeTabs;
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
    public static Item itemGlassChalice;

    public static Item itemWineGrapes;
    public static Item itemMolotovCocktail;

    public static Item itemWoodenMug;

    public static Item itemCannabisSeeds;
    public static Item itemCannabisLeaf;
    public static Item itemCannabisBuds;
    public static Item itemDriedCannabisBuds;
    public static Item itemDriedCannabisLeaves;
    public static ItemSmokingPipe itemPipe;
    public static Item itemHashMuffin;

    public static Item itemMagicMushroomsBrown;
    public static Item itemMagicMushroomsRed;

    public static Item itemTobaccoLeaf;
    public static Item itemTobaccoSeeds;
    public static Item itemDriedTobacco;
    public static Item itemCigarette;
    public static Item itemCigar;
    public static Item itemJoint;

    public static Item itemCocaSeeds;
    public static Item itemCocaLeaf;
    public static Item itemDriedCocaLeaves;

    public static ItemSyringe itemSyringe;
    public static int itemSyringeCocaineDamage;
    public static int itemSyringeCaffeineDamage;

    public static ItemWoodenBowlDrug itemWoodenBowlDrug;
    public static int itemWoodenBowlPeyoteDamage;

    public static Item itemJuniperBerries;

    public static Item itemCoffeaCherries;

    public static Item itemCoffeeBeans;
    public static Item itemColdCoffee;

    public static Item itemDriedPeyote;
    public static Item itemPeyoteJoint;

    public static Item itemHarmonium;

    public static void preInit(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        //----------------------------------------------------------Containers----------------------------------

        itemSyringe = (ItemSyringe) new ItemSyringe().setUnlocalizedName("syringe").setTextureName(Psychedelicraft.textureBase + "syringe");
        GameRegistry.registerItem(itemSyringe, "syringe", Psychedelicraft.MODID);
        itemSyringe.setCreativeTab(CreativeTabs.tabMisc);

        itemWoodenBowlDrug = (ItemWoodenBowlDrug) new ItemWoodenBowlDrug().setUnlocalizedName("woodenBowlDrug").setTextureName(Psychedelicraft.textureBase + "woodenBowl");
        GameRegistry.registerItem(itemWoodenBowlDrug, "psycheWoodenBowl", Psychedelicraft.MODID);
        itemWoodenBowlDrug.setCreativeTab(CreativeTabs.tabFood);

        itemWoodenMug = new ItemWoodenMug().setUnlocalizedName("woodenMug").setTextureName(Psychedelicraft.textureBase + "woodenMug");
        GameRegistry.registerItem(itemWoodenMug, "woodenMug", Psychedelicraft.MODID);
        itemWoodenMug.setCreativeTab(CreativeTabs.tabFood);

        itemPipe = (ItemSmokingPipe) (new ItemSmokingPipe().setUnlocalizedName("smokingPipe").setTextureName(Psychedelicraft.textureBase + "smokingPipe"));
        GameRegistry.registerItem(itemPipe, "smokingPipe", Psychedelicraft.MODID);
        itemPipe.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Wine----------------------------------

        itemGlassChalice = new ItemGlassChalice().setUnlocalizedName("glassChalice").setTextureName(Psychedelicraft.textureBase + "glassChalice");
        GameRegistry.registerItem(itemGlassChalice, "glassChalice", Psychedelicraft.MODID);
        itemGlassChalice.setCreativeTab(CreativeTabs.tabFood);

        itemWineGrapes = (new ItemWineGrapes(1, 0.5F, true)).setUnlocalizedName("wineGrapes").setTextureName(Psychedelicraft.textureBase + "wineGrapes");
        GameRegistry.registerItem(itemWineGrapes, "wineGrapes", Psychedelicraft.MODID);
        itemWineGrapes.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Molotov Cocktail----------------------------------

        int entityMolotovCocktailID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID);
        EntityRegistry.registerModEntity(EntityMolotovCocktail.class, "molotovCocktail", entityMolotovCocktailID, mod, 64, 10, true);

        itemMolotovCocktail = new ItemMolotovCocktail().setUnlocalizedName("molotovCocktail").setTextureName(Psychedelicraft.textureBase + "molotovCocktail");
        GameRegistry.registerItem(itemMolotovCocktail, "molotovCocktail", Psychedelicraft.MODID);
        itemMolotovCocktail.setCreativeTab(CreativeTabs.tabCombat);

        //----------------------------------------------------------Weed----------------------------------

        itemCannabisSeeds = new ItemSeeds(PSBlocks.blockCannabisPlant, Blocks.farmland).setUnlocalizedName("cannabisSeeds").setTextureName(Psychedelicraft.textureBase + "cannabisSeeds");
        GameRegistry.registerItem(itemCannabisSeeds, "cannabisSeeds", Psychedelicraft.MODID);
        itemCannabisSeeds.setCreativeTab(CreativeTabs.tabMisc);

        itemCannabisLeaf = new Item().setUnlocalizedName("cannabisLeaf").setTextureName(Psychedelicraft.textureBase + "cannabisLeaf");
        GameRegistry.registerItem(itemCannabisLeaf, "cannabisLeaf", Psychedelicraft.MODID);
        itemCannabisLeaf.setCreativeTab(CreativeTabs.tabMisc);

        itemCannabisBuds = new Item().setUnlocalizedName("cannabisBuds").setTextureName(Psychedelicraft.textureBase + "cannabisBuds");
        GameRegistry.registerItem(itemCannabisBuds, "cannabisBuds", Psychedelicraft.MODID);
        itemCannabisBuds.setCreativeTab(CreativeTabs.tabMisc);

        itemDriedCannabisBuds = new Item().setUnlocalizedName("driedCannabisBuds").setTextureName(Psychedelicraft.textureBase + "driedCannabisBuds");
        GameRegistry.registerItem(itemDriedCannabisBuds, "driedCannabisBuds", Psychedelicraft.MODID);
        itemDriedCannabisBuds.setCreativeTab(CreativeTabs.tabMisc);

        itemDriedCannabisLeaves = new Item().setUnlocalizedName("driedCannabisLeaves").setTextureName(Psychedelicraft.textureBase + "driedCannabisLeaves");
        GameRegistry.registerItem(itemDriedCannabisLeaves, "driedCannabisLeaves", Psychedelicraft.MODID);
        itemDriedCannabisLeaves.setCreativeTab(CreativeTabs.tabMisc);

        itemHashMuffin = new ItemHashMuffin().setUnlocalizedName("hashMuffin").setTextureName(Psychedelicraft.textureBase + "hashMuffin");
        GameRegistry.registerItem(itemHashMuffin, "hashMuffin", Psychedelicraft.MODID);
        itemHashMuffin.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Magic Mushrooms----------------------------------

        itemMagicMushroomsBrown = new ItemEdibleDrug(new DrugInfluence("BrownShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsBrown").setTextureName(Psychedelicraft.textureBase + "magicMushroomsBrown");
        GameRegistry.registerItem(itemMagicMushroomsBrown, "brownMagicMushrooms", Psychedelicraft.MODID);
        itemMagicMushroomsBrown.setCreativeTab(CreativeTabs.tabFood);

        itemMagicMushroomsRed = new ItemEdibleDrug(new DrugInfluence("RedShrooms", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("magicMushroomsRed").setTextureName(Psychedelicraft.textureBase + "magicMushroomsRed");
        GameRegistry.registerItem(itemMagicMushroomsRed, "redMagicMushrooms", Psychedelicraft.MODID);
        itemMagicMushroomsRed.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Tobacco----------------------------------

        itemTobaccoLeaf = new Item().setUnlocalizedName("tobaccoLeaf").setTextureName(Psychedelicraft.textureBase + "tobaccoLeaf");
        GameRegistry.registerItem(itemTobaccoLeaf, "tobaccoLeaf", Psychedelicraft.MODID);
        itemTobaccoLeaf.setCreativeTab(CreativeTabs.tabMisc);

        itemTobaccoSeeds = new ItemSeeds(PSBlocks.blockTobaccoPlant, Blocks.farmland).setUnlocalizedName("tobaccoSeeds").setTextureName(Psychedelicraft.textureBase + "tobaccoSeeds");
        GameRegistry.registerItem(itemTobaccoSeeds, "tobaccoSeeds", Psychedelicraft.MODID);
        itemTobaccoSeeds.setCreativeTab(CreativeTabs.tabMisc);

        itemDriedTobacco = new Item().setUnlocalizedName("driedTobacco").setTextureName(Psychedelicraft.textureBase + "driedTobacco");
        GameRegistry.registerItem(itemDriedTobacco, "driedTobacco", Psychedelicraft.MODID);
        itemDriedTobacco.setCreativeTab(CreativeTabs.tabMisc);

        itemCigarette = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigarette"}, new String[]{"cigaretteInUse"}).setUnlocalizedName("cigarette").setTextureName(Psychedelicraft.textureBase + "cigarette");
        GameRegistry.registerItem(itemCigarette, "cigarette", Psychedelicraft.MODID);
        itemCigarette.setCreativeTab(CreativeTabs.tabMisc);

        itemCigar = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.7f)}, new String[]{"cigar", "cigar1", "cigar2", "cigar3"}, new String[]{"cigarInUse", "cigar1InUse", "cigar2InUse", "cigar3InUse"}).setUnlocalizedName("cigar").setTextureName(Psychedelicraft.textureBase + "cigar");
        GameRegistry.registerItem(itemCigar, "cigar", Psychedelicraft.MODID);
        itemCigar.setCreativeTab(CreativeTabs.tabMisc);

        itemJoint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.20f)}, new String[]{"joint"}, new String[]{"jointInUse"}).setUnlocalizedName("joint").setTextureName(Psychedelicraft.textureBase + "joint");
        GameRegistry.registerItem(itemJoint, "joint", Psychedelicraft.MODID);
        itemJoint.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Cocaine----------------------------------

        itemCocaSeeds = new ItemSeeds(PSBlocks.blockCocaPlant, Blocks.farmland).setUnlocalizedName("cocaSeeds").setTextureName(Psychedelicraft.textureBase + "cocaSeeds");
        GameRegistry.registerItem(itemCocaSeeds, "cocaSeeds", Psychedelicraft.MODID);
        itemCocaSeeds.setCreativeTab(CreativeTabs.tabMisc);

        itemCocaLeaf = new Item().setUnlocalizedName("cocaLeaf").setTextureName(Psychedelicraft.textureBase + "cocaLeaf");
        GameRegistry.registerItem(itemCocaLeaf, "cocaLeaf", Psychedelicraft.MODID);
        itemCocaLeaf.setCreativeTab(CreativeTabs.tabMisc);

        itemDriedCocaLeaves = new Item().setUnlocalizedName("driedCocaLeaves").setTextureName(Psychedelicraft.textureBase + "driedCocaLeaves");
        GameRegistry.registerItem(itemDriedCocaLeaves, "driedCocaLeaves", Psychedelicraft.MODID);
        itemDriedCocaLeaves.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Jenever----------------------------------

        itemJuniperBerries = new ItemFood(1, 0.5F, true).setUnlocalizedName("juniperBerries").setTextureName(Psychedelicraft.textureBase + "juniperBerries");
        GameRegistry.registerItem(itemJuniperBerries, "juniperBerries", Psychedelicraft.MODID);
        itemJuniperBerries.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Coffee----------------------------------

        itemCoffeaCherries = new ItemSeeds(PSBlocks.blockCoffea, Blocks.farmland).setUnlocalizedName("coffeaCherries").setTextureName(Psychedelicraft.textureBase + "coffeaCherries");
        GameRegistry.registerItem(itemCoffeaCherries, "coffeaCherries", Psychedelicraft.MODID);
        itemCoffeaCherries.setCreativeTab(CreativeTabs.tabFood);

        itemCoffeeBeans = new Item().setUnlocalizedName("coffeeBeans").setTextureName(Psychedelicraft.textureBase + "coffeeBeans");
        GameRegistry.registerItem(itemCoffeeBeans, "coffeeBeans", Psychedelicraft.MODID);
        itemCoffeeBeans.setCreativeTab(CreativeTabs.tabFood);

        itemColdCoffee = new ItemWoodenMugColdCoffee().setUnlocalizedName("coldCoffee").setTextureName(Psychedelicraft.textureBase + "coldCoffee");
        GameRegistry.registerItem(itemColdCoffee, "coldCoffee", Psychedelicraft.MODID);
        itemColdCoffee.setCreativeTab(CreativeTabs.tabFood);

        //----------------------------------------------------------Peyote----------------------------------

        itemDriedPeyote = new ItemEdibleDrug(new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.5f)).setUnlocalizedName("driedPeyote").setTextureName(Psychedelicraft.textureBase + "driedPeyote");
        GameRegistry.registerItem(itemDriedPeyote, "driedPeyote", Psychedelicraft.MODID);
        itemDriedPeyote.setCreativeTab(CreativeTabs.tabFood);

        itemPeyoteJoint = new ItemSmokeable(new DrugInfluence[]{new DrugInfluence("Peyote", 20, 0.003, 0.0015, 0.4f), new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.1f)}, new String[]{"jointPeyote"}, new String[]{"jointPeyoteInUse"}).setUnlocalizedName("jointPeyote").setTextureName(Psychedelicraft.textureBase + "jointPeyote");
        GameRegistry.registerItem(itemPeyoteJoint, "peyoteJoint", Psychedelicraft.MODID);
        itemPeyoteJoint.setCreativeTab(CreativeTabs.tabMisc);

        //----------------------------------------------------------Harmonium----------------------------------

        itemHarmonium = new ItemHarmonium().setUnlocalizedName("harmonium").setTextureName(Psychedelicraft.textureBase + "harmonium");
        GameRegistry.registerItem(itemHarmonium, "harmonium", Psychedelicraft.MODID);
        itemHarmonium.setCreativeTab(CreativeTabs.tabMisc);
    }

    public static void preInitEnd(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        itemSyringeCocaineDamage = itemSyringe.addEffect(1, new DrugInfluence[]{new DrugInfluence("Cocaine", 0, 0.005, 0.01, 0.5f)}, 0x55ffffff, "cocaine");
        itemSyringeCaffeineDamage = itemSyringe.addEffect(2, new DrugInfluence[]{new DrugInfluence("Caffeine", 0, 0.005, 0.01, 0.85f)}, 0x552e1404, "caffeine");

        itemWoodenBowlPeyoteDamage = itemWoodenBowlDrug.addEffect(0, new DrugInfluence[]{new DrugInfluence("Peyote", 15, 0.005, 0.003, 0.9f)}, "peyote", Psychedelicraft.textureBase + "woodenBowlPeyote");
    }
}
