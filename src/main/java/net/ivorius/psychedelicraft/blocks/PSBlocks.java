/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.blocks;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.items.ItemBarrel;
import net.ivorius.psychedelicraft.items.ItemRiftJar;
import net.ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class PSBlocks
{
    public static Block blockWineGrapeLattice;

    public static Block blockBarrel;

    public static Block blockDryingTable;

    public static Block blockCannabisPlant;

    public static Block blockTobaccoPlant;

    public static Block blockCocaPlant;

    public static Block blockPsycheLeaves;
    public static Block blockPsycheLog;
    public static Block blockJuniperSapling;

    public static Block blockCoffea;

    public static Block blockPeyote;
    public static Block blockRiftJar;
    public static Block blockGlitched;

    public static void preInit(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        //----------------------------------------------------------Barrel----------------------------------

        blockBarrel = new BlockBarrel().setHardness(1.0F);
        registerBlockDefault(blockBarrel, ItemBarrel.class, "barrel");
        GameRegistry.registerTileEntity(TileEntityBarrel.class, "barrel");

        //----------------------------------------------------------Wine----------------------------------

        blockWineGrapeLattice = new BlockWineGrapeLattice().setHardness(0.3F);
        registerBlockDefault(blockWineGrapeLattice, ItemBlock.class, "wineGrapeLattice");

        //----------------------------------------------------------Weed----------------------------------

        blockCannabisPlant = new BlockCannabisPlant().setHardness(0.5f);
        registerBlockDefault(blockCannabisPlant, ItemBlock.class, "cannabisPlant");
        blockCannabisPlant.setCreativeTab(null);

        //----------------------------------------------------------Drying Table----------------------------------

        blockDryingTable = new BlockDryingTable().setHardness(1.0f);
        registerBlockDefault(blockDryingTable, "dryingTable");
        GameRegistry.registerTileEntity(TileEntityDryingTable.class, "dryingTable");

        //----------------------------------------------------------Tobacco----------------------------------

        blockTobaccoPlant = new BlockTobaccoPlant().setHardness(0.5f);
        registerBlockDefault(blockTobaccoPlant, "tobaccoPlant");
        blockTobaccoPlant.setCreativeTab(null);

        //----------------------------------------------------------Cocaine----------------------------------

        blockCocaPlant = new BlockCocaPlant().setHardness(0.5f);
        registerBlockDefault(blockCocaPlant, ItemBlock.class, "cocaPlant");
        blockCocaPlant.setCreativeTab(null);

        //----------------------------------------------------------Jenever----------------------------------

        blockPsycheLeaves = new BlockPsycheLeaves().setHardness(1.0F);
        registerBlockDefault(blockPsycheLeaves, "psycheLeaves");

        blockPsycheLog = new BlockPsycheLog().setHardness(1.0F);
        registerBlockDefault(blockPsycheLog, "psycheLog");

        blockJuniperSapling = new BlockPsycheSapling().setHardness(1.0F);
        registerBlockDefault(blockJuniperSapling, "psycheSapling");

        //----------------------------------------------------------Coffee----------------------------------

        blockCoffea = new BlockCoffea().setHardness(0.5f);
        registerBlockDefault(blockCoffea, ItemBlock.class, "coffea");
        blockCoffea.setCreativeTab(null);

        //----------------------------------------------------------Peyote----------------------------------

        blockPeyote = new BlockPeyote().setHardness(0.5f);
        registerBlockDefault(blockPeyote, ItemBlock.class, "peyote");
        GameRegistry.registerTileEntity(TileEntityPeyote.class, "peyote");

        //----------------------------------------------------------Digital----------------------------------

        blockRiftJar = new BlockRiftJar().setBlockName("riftJar").setBlockTextureName(Psychedelicraft.textureBase + "riftJar");
        GameRegistry.registerBlock(blockRiftJar, ItemRiftJar.class, "riftJar", Psychedelicraft.MODID);
        GameRegistry.registerTileEntity(TileEntityRiftJar.class, "riftJar");
        blockRiftJar.setCreativeTab(CreativeTabs.tabDecorations);

        blockGlitched = new BlockGlitched().setBlockName("glitched").setBlockTextureName(Psychedelicraft.textureBase + "glitched");
        GameRegistry.registerBlock(blockGlitched, ItemBlock.class, "glitched", Psychedelicraft.MODID);
        blockGlitched.setCreativeTab(null);
    }

    public static void preInitEnd(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        ResourceLocation beerBarrelTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "beerBarrelTexture.png");
        BlockBarrel.registerBarrelEntry(0, blockBarrel, new BlockBarrel.BarrelEntry(beerBarrelTexture, PSItems.itemWoodenMug, 0, PSItems.itemWoodenMug, 15, 1, 1), new ItemBarrel.BarrelEntry("beer", 0, Psychedelicraft.textureBase + "barrelItemBeer"));

        ResourceLocation wineBarrelTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "wineBarrelTexture.png");
        BlockBarrel.registerBarrelEntry(1, blockBarrel, new BlockBarrel.BarrelEntry(wineBarrelTexture, PSItems.itemGlassChalice, 0, PSItems.itemGlassChalice, 10, 15, 1), new ItemBarrel.BarrelEntry("wine", 1, Psychedelicraft.textureBase + "barrelItemWine"));

        ResourceLocation jeneverBarrelTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "jeneverBarrelTexture.png");
        BlockBarrel.registerBarrelEntry(2, blockBarrel, new BlockBarrel.BarrelEntry(jeneverBarrelTexture, PSItems.itemWoodenMug, 0, PSItems.itemWoodenMug, 15, 2, 2), new ItemBarrel.BarrelEntry("jenever", 2, Psychedelicraft.textureBase + "barrelItemJenever"));
    }

    public static void registerBlockDefault(Block block, String id)
    {
        GameRegistry.registerBlock(block, id);
        block.setBlockName(id);
        block.setBlockTextureName(Psychedelicraft.textureBase + id);
        block.setCreativeTab(Psychedelicraft.creativeTab);
    }

    public static void registerBlockDefault(Block block, Class<? extends ItemBlock> itemBlock, String id)
    {
        GameRegistry.registerBlock(block, itemBlock, id);
        block.setBlockName(id);
        block.setBlockTextureName(Psychedelicraft.textureBase + id);
        block.setCreativeTab(Psychedelicraft.creativeTab);
    }
}