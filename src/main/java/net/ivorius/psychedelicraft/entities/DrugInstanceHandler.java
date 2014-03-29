package net.ivorius.psychedelicraft.entities;

import net.minecraft.entity.Entity;

import java.util.Hashtable;

/**
 * Created by lukas on 18.02.14.
 */
public class DrugInstanceHandler
{
    private static Hashtable<Integer, DrugHelper> drugHelpers = new Hashtable<Integer, DrugHelper>();

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
