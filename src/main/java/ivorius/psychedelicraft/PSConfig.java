/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

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

    public static boolean spawnRifts;

    public static boolean genJuniper;
    public static boolean genCannabis;
    public static boolean genTobacco;
    public static boolean genCoffea;
    public static boolean genCoca;
    public static boolean genPeyote;

    public static boolean dungeonChests;
    public static boolean villageChests;

    public static boolean farmerDrugDeals;

    public static int ticksForFullWineFermentation;
    public static int ticksUntilWineAcetification;

    public static double beerFermentationHalfTimeTicks;
    private static double beerFermentationTickImprovement;

    public static void loadConfig(String configID)
    {
        if (configID == null || configID.equals(Configuration.CATEGORY_GENERAL))
        {
            PSEntityList.villagerDealerProfessionID = config.get("General", "villagerDealerProfessionID", 87).getInt();
        }

        if (configID == null || configID.equals(CATEGORY_BALANCING))
        {
            spawnRifts = config.get(CATEGORY_BALANCING, "spawnRifts", true).getBoolean();

            genJuniper = config.get(CATEGORY_BALANCING, "generateJuniper", true).getBoolean();
            genCannabis = config.get(CATEGORY_BALANCING, "generateCannabis", true).getBoolean();
            genTobacco = config.get(CATEGORY_BALANCING, "generateTobacco", true).getBoolean();
            genCoffea = config.get(CATEGORY_BALANCING, "generateCoffea", true).getBoolean();
            genCoca = config.get(CATEGORY_BALANCING, "generateCoca", true).getBoolean();
            genPeyote = config.get(CATEGORY_BALANCING, "generatePeyote", true).getBoolean();

            dungeonChests = config.get(CATEGORY_BALANCING, "dungeonChests", true).getBoolean();
            villageChests = config.get(CATEGORY_BALANCING, "villageChests", true).getBoolean();

            farmerDrugDeals = config.get(CATEGORY_BALANCING, "farmerDrugDeals", true).getBoolean();

            int minute = 20 * 60;
            ticksForFullWineFermentation = config.get(CATEGORY_BALANCING, "ticksForFullWineFermentation", minute * 300, "Time until wine gets the 'perfect' strength").getInt();
            ticksUntilWineAcetification = config.get(CATEGORY_BALANCING, "ticksUntilWineAcetification", minute * 30, "Time until wine turns to vinegar after it is 'perfect'. Enter a negative number to disable.").getInt();

            beerFermentationHalfTimeTicks = config.get(CATEGORY_BALANCING, "beerFermentationHalfTimeTicks", minute * 180.0, "Beer gets continuously better; this determines the time until beer is deemed 'half finished'").getDouble();

            // f(x) = 1 - (1 - speed)^x
            // 0.5  = 1.0 - (1.0 - speed)^beerFermentationHalfTimeTicks
            // 1 - beerFermentationHalfTimeTicks'th root (0.5) = speed
            beerFermentationTickImprovement = 1.0 - root (0.5, beerFermentationHalfTimeTicks);
        }

        Psychedelicraft.proxy.loadConfig(configID);
    }

    public static double root(double num, double root)
    {
        return Math.pow(Math.E, Math.log(num) / root);
    }

    public static double getBeerFermentationTickImprovement()
    {
        return beerFermentationTickImprovement;
    }
}
