/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import ivorius.ivtoolkit.network.PacketExtendedEntityPropertiesData;
import ivorius.ivtoolkit.network.PacketExtendedEntityPropertiesDataHandler;
import ivorius.ivtoolkit.network.PacketTileEntityData;
import ivorius.ivtoolkit.network.PacketTileEntityDataHandler;
import ivorius.psychedelicraft.blocks.PSBlocks;
import ivorius.psychedelicraft.blocks.TileEntityDryingTable;
import ivorius.psychedelicraft.entities.DrugInfluence;
import ivorius.psychedelicraft.entities.DrugInfluenceHarmonium;
import ivorius.psychedelicraft.entities.PSEntityList;
import ivorius.psychedelicraft.events.PSEventFMLHandler;
import ivorius.psychedelicraft.events.PSEventForgeHandler;
import ivorius.psychedelicraft.gui.PSGuiHandler;
import ivorius.psychedelicraft.items.*;
import ivorius.psychedelicraft.worldgen.PSWorldGen;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

@Mod(modid = Psychedelicraft.MODID, version = Psychedelicraft.VERSION, name = Psychedelicraft.NAME,
        dependencies = "required-after:ivtoolkit")
public class Psychedelicraft
{
    public static final String MODID = "psychedelicraft";
    public static final String NAME = "Psychedelicraft";
    public static final String VERSION = "1.4.1";

    @Instance(value = "psychedelicraft")
    public static Psychedelicraft instance;

    @SidedProxy(clientSide = "ivorius.psychedelicraft.client.ClientProxy", serverSide = "ivorius.psychedelicraft.server.ServerProxy")
    public static PSProxy proxy;

    public static Logger logger;

    public static PSGuiHandler guiHandler;
    public static PSEventForgeHandler eventForgeHandler;
    public static PSEventFMLHandler eventFMLHandler;
    public static PSCommunicationHandler communicationHandler;

    public static SimpleNetworkWrapper network;

    public static PSCoreHandlerClient coreHandlerClient;
    public static PSCoreHandlerCommon coreHandlerCommon;
    public static PSCoreHandlerServer coreHandlerServer;

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

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        spawnRifts = config.get("Balancing", "spawnRifts", true).getBoolean(true);

        creativeTab = new CreativeTabPsyche("psychedelicraft");

        guiHandler = new PSGuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);

        eventForgeHandler = new PSEventForgeHandler();
        eventForgeHandler.register();
        eventFMLHandler = new PSEventFMLHandler();
        eventFMLHandler.register();

        communicationHandler = new PSCommunicationHandler();

        coreHandlerCommon = new PSCoreHandlerCommon();
        coreHandlerCommon.register();

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
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(PacketExtendedEntityPropertiesDataHandler.class, PacketExtendedEntityPropertiesData.class, 0, Side.CLIENT);
        network.registerMessage(PacketTileEntityDataHandler.class, PacketTileEntityData.class, 1, Side.CLIENT);

        proxy.registerRenderers();

        creativeTab.tabIcon = PSItems.cannabisLeaf;

        DrugInfluence.registerInfluence(DrugInfluence.class, "default");
        DrugInfluence.registerInfluence(DrugInfluenceHarmonium.class, "harmonium");

        addCrafting();
        PSWorldGen.initWorldGen();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        PSOutboundCommuncationHandler.init();
    }

    private void addCrafting()
    {
        GameRegistry.addRecipe(new ItemStack(PSItems.syringe), "I", "#", 'I', Items.iron_ingot, '#', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(PSItems.pipe), "  I", " S ", "WS ", 'I', Items.iron_ingot, 'S', Items.stick, 'W', Blocks.planks);
        GameRegistry.addRecipe(new ItemStack(PSItems.bong, 1, 3), " P ", "G G", "GGG", 'P', Blocks.glass_pane, 'G', Blocks.glass);
        addShapelessRecipe(new ItemStack(PSItems.bong), Items.water_bucket, PSItems.bong);

        GameRegistry.addRecipe(new ItemStack(PSItems.glassChalice, 4), "# #", " # ", " # ", '#', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(PSBlocks.barrel, 1, 1), "###", "###", "W#W", '#', PSItems.wineGrapes, 'W', Blocks.planks);
        GameRegistry.addRecipe(new ItemStack(PSBlocks.wineGrapeLattice), "###", "###", "O#O", '#', Items.stick, 'O', Blocks.planks);

        for (int i = 0; i < 16; i++)
        {
            GameRegistry.addRecipe(new ItemStack(PSItems.molotovCocktail, 1, i), "#", "W", '#', Items.paper, 'W', new ItemStack(PSItems.glassChalice, 1, i + 1));
        }

        GameRegistry.addRecipe(new ItemStack(PSItems.woodenMug, 8), "# #", "# #", "###", '#', Blocks.planks);
        GameRegistry.addRecipe(new ItemStack(PSBlocks.barrel, 1, 0), "###", "###", "WUW", '#', Items.wheat, 'U', Items.water_bucket, 'W', Blocks.planks);

        GameRegistry.addRecipe(new ItemStack(PSBlocks.dryingTable), "###", "#R#", '#', Blocks.planks, 'R', Items.redstone);

        TileEntityDryingTable.addDryingResult(PSItems.cannabisLeaf, new ItemStack(PSItems.driedCannabisLeaves, 3));
        TileEntityDryingTable.addDryingResult(PSItems.cannabisBuds, new ItemStack(PSItems.driedCannabisBuds, 3));
        GameRegistry.addRecipe(new ItemStack(PSItems.hashMuffin), "LLL", "#X#", "LLL", 'X', new ItemStack(Items.dye, 1, 3), '#', Items.wheat, 'L', PSItems.driedCannabisLeaves);
        GameRegistry.addRecipe(new ItemStack(PSItems.joint), "P", "C", "P", 'P', Items.paper, 'C', PSItems.driedCannabisBuds);
        PSItems.pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(PSItems.driedCannabisBuds), new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.25f)}));
        PSItems.bong.addConsumable(new ItemBong.ItemBongConsumable(new ItemStack(PSItems.driedCannabisBuds), new DrugInfluence[]{new DrugInfluence("Cannabis", 20, 0.002, 0.001, 0.2f)})); //TODO: Play around with the bongs benefits

        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(Blocks.brown_mushroom), new ItemStack(PSItems.magicMushroomsBrown, 3));
        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(Blocks.red_mushroom), new ItemStack(PSItems.magicMushroomsRed, 3));

        GameRegistry.addRecipe(new ItemStack(PSItems.cigarette, 4), "P", "T", "P", 'P', Items.paper, 'T', PSItems.driedTobacco);
        GameRegistry.addRecipe(new ItemStack(PSItems.cigar), "TTT", "TTT", "PPP", 'P', Items.paper, 'T', PSItems.driedTobacco);
        TileEntityDryingTable.addDryingResult(PSItems.tobaccoLeaf, new ItemStack(PSItems.driedTobacco, 3));
        PSItems.pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(PSItems.driedTobacco), new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.8f)}));
        PSItems.bong.addConsumable(new ItemBong.ItemBongConsumable(new ItemStack(PSItems.driedTobacco), new DrugInfluence[]{new DrugInfluence("Tobacco", 0, 0.1, 0.02, 0.6f)})); //TODO: Play around with the bongs benefits

        addShapelessRecipe(new ItemStack(PSItems.syringe, 1, ItemSyringe.damageCocaine), Items.water_bucket, new ItemStack(PSItems.syringe, 1, 0), PSItems.driedCocaLeaves);
        TileEntityDryingTable.addDryingResult(PSItems.cocaLeaf, new ItemStack(PSItems.driedCocaLeaves, 3));

        GameRegistry.addRecipe(new ItemStack(Blocks.planks, 4, 1), "#", '#', PSBlocks.psycheLog);
        GameRegistry.addRecipe(new ItemStack(PSBlocks.barrel, 1, 2), "JSJ", "AGA", "WUW", 'J', PSItems.juniperBerries, 'S', Items.sugar, 'A', PSItems.wineGrapes, 'G', Items.wheat, 'U', Items.water_bucket, 'W', Blocks.planks);

        GameRegistry.addSmelting(PSItems.coffeaCherries, new ItemStack(PSItems.coffeeBeans), 0.2f);

        addShapelessRecipe(new ItemStack(PSItems.syringe, 1, ItemSyringe.damageCaffeine), Items.water_bucket, new ItemStack(PSItems.syringe, 1, 0), PSItems.coffeeBeans, PSItems.coffeeBeans);

        TileEntityDryingTable.addDryingResult(Item.getItemFromBlock(PSBlocks.peyote), new ItemStack(PSItems.driedPeyote, 3));
        GameRegistry.addRecipe(new ItemStack(PSItems.peyoteJoint), "P", "D", "P", 'P', Items.paper, 'D', PSItems.driedPeyote);

        for (int i = 0; i < 16; i++)
        {
            PSItems.pipe.addConsumable(new ItemSmokingPipe.ItemSmokingPipeConsumable(new ItemStack(PSItems.harmonium, 1, 15 - i), new DrugInfluence[]{new DrugInfluenceHarmonium("Harmonium", 0, 0.04, 0.01, 0.65f, EntitySheep.fleeceColorTable[i])}, EntitySheep.fleeceColorTable[i]));
            addShapelessRecipe(new ItemStack(PSItems.harmonium, 1, i), new ItemStack(Items.dye, 1, i), Items.glowstone_dust, PSItems.driedTobacco);
        }

        GameRegistry.addRecipe(new ItemStack(PSBlocks.riftJar), "O-O", "GO ", "OIO", 'O', Blocks.glass, '-', Blocks.planks, 'G', Items.gold_ingot, 'I', Items.iron_ingot);

        for (ItemDrinkHolder itemDrinkHolder : DrinkRegistry.getAllDrinkHolders())
        {
            Item emptyContainer = itemDrinkHolder == PSItems.woodenBowlDrug ? Items.bowl : itemDrinkHolder; // Hacky but eh

            GameRegistry.addShapelessRecipe(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "peyote"), PSItems.driedPeyote, PSItems.driedPeyote, new ItemStack(emptyContainer));

            addShapelessRecipe(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "coldCoffee"), Items.water_bucket, new ItemStack(emptyContainer), PSItems.coffeeBeans, PSItems.coffeeBeans);
            //TODO Add when Forge fixes smelting with NBT
//        GameRegistry.addSmelting(DrinkRegistry.createDrinkStack(itemDrinkHolder, 1, "coldCoffee"), new ItemStack(emptyContainer, 1, 3), 0.2f);
        }
    }

    private static void addShapelessRecipe(ItemStack output, Object... params)
    {
        for (int i = 0; i < params.length; i++)
        {
            if (params[i] instanceof Item)
            {
                params[i] = new ItemStack((Item) params[i], 1, OreDictionary.WILDCARD_VALUE);
            }
            else if (params[i] instanceof Block)
            {
                params[i] = new ItemStack((Block) params[i], 1, OreDictionary.WILDCARD_VALUE);
            }
        }

        GameRegistry.addShapelessRecipe(output, params);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new CommandDrug());
        evt.registerServerCommand(new CommandPsyche());
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