/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.config;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.PSEntityList;
import ivorius.psychedelicraft.fluids.FluidDistillingAlcohol;
import ivorius.psychedelicraft.fluids.FluidDistillingMaturingAlcohol;
import ivorius.psychedelicraft.fluids.FluidMaturingAlcohol;
import net.minecraftforge.common.config.Configuration;

import static ivorius.psychedelicraft.Psychedelicraft.config;

/**
 * Created by lukas on 31.07.14.
 */
public class PSConfig
{
    public static final String CATEGORY_BALANCING = "balancing";
    public static final String CATEGORY_VISUAL = "visual";

    private static final int MINUTE = 20 * 60;

    public static int randomTicksUntilRiftSpawn;
    public static boolean enableHarmonium;
    public static boolean enableRiftJars;

    public static boolean genJuniper;
    public static boolean genCannabis;
    public static boolean genTobacco;
    public static boolean genCoffea;
    public static boolean genCoca;
    public static boolean genPeyote;

    public static boolean dungeonChests;
    public static boolean villageChests;

    public static boolean farmerDrugDeals;

    public static int dryingTableTickDuration;

    public static final FluidMaturingAlcohol.TickInfo beerInfo = new FluidMaturingAlcohol.TickInfo();
    public static final FluidMaturingAlcohol.TickInfo wineInfo = new FluidMaturingAlcohol.TickInfo();
    public static final FluidMaturingAlcohol.TickInfo riceWineInfo = new FluidMaturingAlcohol.TickInfo();

    public static final FluidDistillingMaturingAlcohol.TickInfo vodkaInfo = new FluidDistillingMaturingAlcohol.TickInfo();
    public static final FluidDistillingAlcohol.TickInfo jeneverInfo = new FluidDistillingAlcohol.TickInfo();
    public static final FluidDistillingMaturingAlcohol.TickInfo rumInfo = new FluidDistillingMaturingAlcohol.TickInfo();

    public static void loadConfig(String configID)
    {
        if (configID == null || configID.equals(Configuration.CATEGORY_GENERAL))
        {
            PSEntityList.villagerDealerProfessionID = config.get("General", "villagerDealerProfessionID", 87, "Internal ID for the drug dealer villager. Enter a negative number to disable.").getInt();
        }

        if (configID == null || configID.equals(CATEGORY_BALANCING))
        {
            randomTicksUntilRiftSpawn = config.get(CATEGORY_BALANCING, "randomTicksUntilRiftSpawn", MINUTE * 180, "Approximate number of ticks until a rift spawns. Enter a negative number to disable.").getInt();

            enableHarmonium = config.get(CATEGORY_BALANCING, "enableHarmonium", true).getBoolean();
            enableRiftJars = config.get(CATEGORY_BALANCING, "enableRiftJars", true).getBoolean();

            genJuniper = config.get(CATEGORY_BALANCING, "generateJuniper", true).getBoolean();
            genCannabis = config.get(CATEGORY_BALANCING, "generateCannabis", true).getBoolean();
            genTobacco = config.get(CATEGORY_BALANCING, "generateTobacco", true).getBoolean();
            genCoffea = config.get(CATEGORY_BALANCING, "generateCoffea", true).getBoolean();
            genCoca = config.get(CATEGORY_BALANCING, "generateCoca", true).getBoolean();
            genPeyote = config.get(CATEGORY_BALANCING, "generatePeyote", true).getBoolean();

            dungeonChests = config.get(CATEGORY_BALANCING, "dungeonChests", true).getBoolean();
            villageChests = config.get(CATEGORY_BALANCING, "villageChests", true).getBoolean();

            farmerDrugDeals = config.get(CATEGORY_BALANCING, "farmerDrugDeals", true).getBoolean();

            dryingTableTickDuration = config.get(CATEGORY_BALANCING, "dryingTableTickDuration", MINUTE * 16, "Time until plants in the drying table finish the drying process.").getInt();

            readTickInfo(beerInfo, "beer", MINUTE * 30, MINUTE * 60, MINUTE * 100, config);
            readTickInfo(wineInfo, "wine", MINUTE * 40, MINUTE * 40, MINUTE * 30, config);
            readTickInfo(riceWineInfo, "riceWine", MINUTE * 40, MINUTE * 40, MINUTE * 30, config);

            readTickInfo(vodkaInfo, "vodka", MINUTE * 30, MINUTE * 10, MINUTE * 90, config);
            readTickInfo(jeneverInfo, "jenever", MINUTE * 30, MINUTE * 10, config);

            readTickInfo(rumInfo, "rum", MINUTE * 30, MINUTE * 10, MINUTE * 40, config);
        }

        Psychedelicraft.proxy.loadConfig(configID);
    }

    public static void readTickInfo(FluidMaturingAlcohol.TickInfo tickInfo, String fluidName, int defaultFermentation, int defaultMaturation, int defaultAcetification, Configuration config)
    {
        tickInfo.ticksPerFermentation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerFermentation", defaultFermentation, String.format("Time until %s wort ferments to the next step.", fluidName)).getInt();
        tickInfo.ticksPerMaturation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerMaturation", defaultMaturation, String.format("Time until %s matures to the next step.", fluidName)).getInt();
        tickInfo.ticksUntilAcetification = config.get(CATEGORY_BALANCING, fluidName + "_ticksUntilAcetification", defaultAcetification, String.format("Time until %s acetifies when it oxidizes after complete fermentation. Enter a negative number to disable.", fluidName)).getInt();
    }

    public static void readTickInfo(FluidDistillingAlcohol.TickInfo tickInfo, String fluidName, int defaultFermentation, int defaultDistillation, Configuration config)
    {
        tickInfo.ticksPerFermentation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerFermentation", defaultFermentation, String.format("Time until %s wort ferments to the next step.", fluidName)).getInt();
        tickInfo.ticksPerDistillation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerDistillation", defaultDistillation, String.format("Time until %s distills to the next step.", fluidName)).getInt();
    }

    public static void readTickInfo(FluidDistillingMaturingAlcohol.TickInfo tickInfo, String fluidName, int defaultFermentation, int defaultDistillation, int defaultMaturation, Configuration config)
    {
        tickInfo.ticksPerFermentation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerFermentation", defaultFermentation, String.format("Time until %s wort ferments to the next step.", fluidName)).getInt();
        tickInfo.ticksPerDistillation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerDistillation", defaultDistillation, String.format("Time until %s distills to the next step.", fluidName)).getInt();
        tickInfo.ticksPerMaturation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerMaturation", defaultMaturation, String.format("Time until %s matures to the next step.", fluidName)).getInt();
    }
}
