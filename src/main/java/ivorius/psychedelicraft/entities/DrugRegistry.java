/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lukas on 22.10.14.
 */
public class DrugRegistry
{
    private static BiMap<String, Class<? extends DrugInfluence>> drugMap = HashBiMap.create();

    public static void registerInfluence(Class<? extends DrugInfluence> clazz, String key)
    {
        drugMap.put(key, clazz);
    }

    public static Class<? extends DrugInfluence> getClass(String drugID)
    {
        return drugMap.get(drugID);
    }

    public static String getID(Class<? extends DrugInfluence> clazz)
    {
        return drugMap.inverse().get(clazz);
    }
}
