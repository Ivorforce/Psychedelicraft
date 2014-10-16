/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import net.minecraftforge.common.config.Configuration;

import java.util.*;

/**
 * Created by lukas on 31.07.14.
 */
public class PSConfig
{
    public static final String CATEGORY_BALANCING = "balancing";

    public static boolean spawnRifts;

    public static void loadConfig(String configID)
    {
//        if (configID == null || configID.equals(Configuration.CATEGORY_GENERAL))
//        {
//        }

        if (configID == null || configID.equals(CATEGORY_BALANCING))
        {
            spawnRifts = Psychedelicraft.config.get(CATEGORY_BALANCING, "spawnRifts", true).getBoolean(true);
        }

        Psychedelicraft.proxy.loadConfig(configID);
    }
}
