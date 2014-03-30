package net.ivorius.psychedelicraft.blocks;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.items.ItemBarrel;
import net.ivorius.psychedelicraft.items.ItemGlassChalice;
import net.ivorius.psychedelicraft.items.ItemWineGrapes;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
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
	
	public static void init()
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
	}
	
	public static void registerBlockDefault(Block block, String id)
	{
		GameRegistry.registerBlock(block, id);
		block.setBlockName(id);
		block.setBlockTextureName(Psychedelicraft.textureBase + id);
		block.setCreativeTab(Psychedelicraft.tab);
	}
	
	public static void registerBlockDefault(Block block, Class<? extends ItemBlock> itemBlock, String id)
	{
		GameRegistry.registerBlock(block, itemBlock, id);
		block.setBlockName(id);
		block.setBlockTextureName(Psychedelicraft.textureBase + id);
		block.setCreativeTab(Psychedelicraft.tab);
	}
}