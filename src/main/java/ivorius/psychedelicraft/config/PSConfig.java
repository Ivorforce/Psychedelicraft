/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.config;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.PSEntityList;
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

    public static int ticksPerWineFermentation;
    public static int ticksPerWineMaturation;
    public static int ticksUntilWineAcetification;

    public static int ticksPerBeerFermentation;
    public static int ticksPerBeerMaturation;

    public static int ticksPerVodkaFermentation;
    public static int ticksPerVodkaDistillation;

    public static int ticksPerJeneverFermentation;
    public static int ticksPerJeneverDistillation;

    public static void loadConfig(String configID)
    {
        if (configID == null || configID.equals(Configuration.CATEGORY_GENERAL))
        {
            PSEntityList.villagerDealerProfessionID = config.get("General", "villagerDealerProfessionID", 87).getInt();
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

            ticksPerWineFermentation = config.get(CATEGORY_BALANCING, "ticksPerWineFermentation", MINUTE * 40, "Time until wine ferments to the next step.").getInt();
            ticksPerWineMaturation = config.get(CATEGORY_BALANCING, "ticksPerWineMaturation", MINUTE * 40, "Time until wine matures to the next step.").getInt();
            ticksUntilWineAcetification = config.get(CATEGORY_BALANCING, "ticksUntilWineAcetification", MINUTE * 30, "Time until wine turns to vinegar after it is 'perfect'. Enter a negative number to disable.").getInt();

            ticksPerBeerFermentation = config.get(CATEGORY_BALANCING, "ticksPerBeerFermentation", MINUTE * 30, "Time until beer wort ferments to the next step.").getInt();
            ticksPerBeerMaturation = config.get(CATEGORY_BALANCING, "ticksPerBeerMaturation", MINUTE * 60, "Time until beer matures to the next step.").getInt();

            ticksPerVodkaFermentation = config.get(CATEGORY_BALANCING, "ticksPerVodkaFermentation", MINUTE * 30, "Time until vodka wort ferments to the next step.").getInt();
            ticksPerVodkaDistillation = config.get(CATEGORY_BALANCING, "ticksPerVodkaDistillation", MINUTE * 10, "Time until vodka distills to the next step.").getInt();

            ticksPerJeneverFermentation = config.get(CATEGORY_BALANCING, "ticksPerJeneverFermentation", MINUTE * 30, "Time until jenever wort ferments to the next step.").getInt();
            ticksPerJeneverDistillation = config.get(CATEGORY_BALANCING, "ticksPerJeneverDistillation", MINUTE * 10, "Time until jenever distills to the next step.").getInt();
        }

        Psychedelicraft.proxy.loadConfig(configID);
    }
}
