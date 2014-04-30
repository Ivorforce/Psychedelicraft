/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.items.ItemBarrel;
import ivorius.psychedelicraft.items.ItemRiftJar;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class PSBlocks
{
    public static Block wineGrapeLattice;

    public static Block barrel;

    public static Block dryingTable;

    public static Block cannabisPlant;

    public static Block tobaccoPlant;

    public static Block cocaPlant;

    public static BlockPsycheLeaves psycheLeaves;
    public static Block psycheLog;
    public static Block psycheSapling;

    public static Block coffea;

    public static Block peyote;
    public static Block riftJar;
    public static Block glitched;

    public static void preInit(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        //----------------------------------------------------------Barrel----------------------------------

        barrel = new BlockBarrel().setHardness(1.0F);
        barrel.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(barrel, ItemBarrel.class, "barrel");
        GameRegistry.registerTileEntity(TileEntityBarrel.class, "barrel");

        //----------------------------------------------------------Wine----------------------------------

        wineGrapeLattice = new BlockWineGrapeLattice().setHardness(0.3F);
        wineGrapeLattice.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(wineGrapeLattice, ItemBlock.class, "wineGrapeLattice");

        //----------------------------------------------------------Weed----------------------------------

        cannabisPlant = new BlockCannabisPlant().setHardness(0.5f);
        cannabisPlant.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(cannabisPlant, ItemBlock.class, "cannabisPlant");
        cannabisPlant.setCreativeTab(null);

        //----------------------------------------------------------Drying Table----------------------------------

        dryingTable = new BlockDryingTable().setHardness(1.0f);
        dryingTable.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(dryingTable, "dryingTable");
        GameRegistry.registerTileEntity(TileEntityDryingTable.class, "dryingTable");

        //----------------------------------------------------------Tobacco----------------------------------

        tobaccoPlant = new BlockTobaccoPlant().setHardness(0.5f);
        tobaccoPlant.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(tobaccoPlant, "tobaccoPlant");
        tobaccoPlant.setCreativeTab(null);

        //----------------------------------------------------------Cocaine----------------------------------

        cocaPlant = new BlockCocaPlant().setHardness(0.5f);
        cocaPlant.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(cocaPlant, ItemBlock.class, "cocaPlant");
        cocaPlant.setCreativeTab(null);

        //----------------------------------------------------------Jenever----------------------------------

        psycheLeaves = (BlockPsycheLeaves) new BlockPsycheLeaves();
        psycheLeaves.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(psycheLeaves, "psycheLeaves");

        psycheLog = new BlockPsycheLog().setHardness(1.0F);
        psycheLog.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(psycheLog, "psycheLog");

        psycheSapling = new BlockPsycheSapling().setHardness(1.0F);
        psycheSapling.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(psycheSapling, "psycheSapling");

        //----------------------------------------------------------Coffee----------------------------------

        coffea = new BlockCoffea().setHardness(0.5f);
        coffea.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(coffea, ItemBlock.class, "coffea");
        coffea.setCreativeTab(null);

        //----------------------------------------------------------Peyote----------------------------------

        peyote = new BlockPeyote().setHardness(0.5f);
        peyote.setCreativeTab(CreativeTabs.tabDecorations);
        registerBlockDefault(peyote, ItemBlock.class, "peyote");
        GameRegistry.registerTileEntity(TileEntityPeyote.class, "peyote");

        //----------------------------------------------------------Digital----------------------------------

        riftJar = new BlockRiftJar().setBlockName("riftJar").setBlockTextureName(Psychedelicraft.textureBase + "riftJar");
        riftJar.setCreativeTab(CreativeTabs.tabDecorations);
        GameRegistry.registerBlock(riftJar, ItemRiftJar.class, "riftJar", Psychedelicraft.MODID);
        GameRegistry.registerTileEntity(TileEntityRiftJar.class, "riftJar");
        riftJar.setCreativeTab(CreativeTabs.tabDecorations);

        glitched = new BlockGlitched().setBlockName("glitched").setBlockTextureName(Psychedelicraft.textureBase + "glitched");
        GameRegistry.registerBlock(glitched, ItemBlock.class, "glitched", Psychedelicraft.MODID);
        glitched.setCreativeTab(null);
    }

    public static void preInitEnd(FMLPreInitializationEvent event, Psychedelicraft mod, Configuration configuration)
    {
        ResourceLocation beerBarrelTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "beerBarrelTexture.png");
        BlockBarrel.registerBarrelEntry(0, barrel, new BlockBarrel.BarrelEntry(beerBarrelTexture, PSItems.woodenMug, 0, PSItems.woodenMug, 15, 1, 1), new ItemBarrel.BarrelEntry("beer", 0, Psychedelicraft.textureBase + "barrelItemBeer"));

        ResourceLocation wineBarrelTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "wineBarrelTexture.png");
        BlockBarrel.registerBarrelEntry(1, barrel, new BlockBarrel.BarrelEntry(wineBarrelTexture, PSItems.glassChalice, 0, PSItems.glassChalice, 10, 15, 1), new ItemBarrel.BarrelEntry("wine", 1, Psychedelicraft.textureBase + "barrelItemWine"));

        ResourceLocation jeneverBarrelTexture = new ResourceLocation(Psychedelicraft.MODID, Psychedelicraft.filePathTextures + "jeneverBarrelTexture.png");
        BlockBarrel.registerBarrelEntry(2, barrel, new BlockBarrel.BarrelEntry(jeneverBarrelTexture, PSItems.woodenMug, 0, PSItems.woodenMug, 15, 2, 2), new ItemBarrel.BarrelEntry("jenever", 2, Psychedelicraft.textureBase + "barrelItemJenever"));
    }

    public static void registerBlockDefault(Block block, String id)
    {
        GameRegistry.registerBlock(block, id);
        block.setBlockName(id);
        block.setBlockTextureName(Psychedelicraft.textureBase + id);
//        block.setCreativeTab(Psychedelicraft.creativeTab);
    }

    public static void registerBlockDefault(Block block, Class<? extends ItemBlock> itemBlock, String id)
    {
        GameRegistry.registerBlock(block, itemBlock, id);
        block.setBlockName(id);
        block.setBlockTextureName(Psychedelicraft.textureBase + id);
//        block.setCreativeTab(Psychedelicraft.creativeTab);
    }
}