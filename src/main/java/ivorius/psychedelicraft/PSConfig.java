/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import ivorius.psychedelicraft.entities.PSEntityList;
import net.minecraftforge.common.config.Configuration;

import java.util.*;

import static ivorius.psychedelicraft.Psychedelicraft.config;

/**
 * Created by lukas on 31.07.14.
 */
public class PSConfig
{
    public static final String CATEGORY_BALANCING = "balancing";

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
        }

        Psychedelicraft.proxy.loadConfig(configID);
    }
}
