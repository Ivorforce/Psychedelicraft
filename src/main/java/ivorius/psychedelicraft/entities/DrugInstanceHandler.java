/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lukas on 18.02.14.
 */
public class DrugInstanceHandler
{
    private static Map<Integer, DrugHelper> drugHelpers = new HashMap<>();

    public static DrugHelper getDrugHelper(Entity entity)
    {
        int id = entity.getEntityId();

        if (!drugHelpers.containsKey(id))
        {
            drugHelpers.put(id, new DrugHelper());
        }

        return drugHelpers.get(id);
    }

    public static void removeDrugHelper(Entity entity)
    {
        int id = entity.getEntityId();

        drugHelpers.remove(id);
    }
}
