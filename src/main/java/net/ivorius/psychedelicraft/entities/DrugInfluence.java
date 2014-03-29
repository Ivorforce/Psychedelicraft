package net.ivorius.psychedelicraft.entities;


import net.minecraft.nbt.NBTTagCompound;

import java.util.Hashtable;

public class DrugInfluence
{
    public static Hashtable<String, Class<? extends DrugInfluence>> keyToClass = new Hashtable<String, Class<? extends DrugInfluence>>();
    public static Hashtable<Class<? extends DrugInfluence>, String> classToKey = new Hashtable<Class<? extends DrugInfluence>, String>();

    public String drugName;

    public int delay;

    public double influenceSpeed;
    public double influenceSpeedPlus;

    public double maxInfluence;

    public static void registerInfluence(Class<? extends DrugInfluence> clazz, String key)
    {
        keyToClass.put(key, clazz);
        classToKey.put(clazz, key);
    }

    public DrugInfluence(String drugName, int delay, double influenceSpeed, double influenceSpeedPlus, double maxInfluence)
    {
        this.drugName = drugName;

        this.delay = delay;

        this.influenceSpeed = influenceSpeed;
        this.influenceSpeedPlus = influenceSpeedPlus;

        this.maxInfluence = maxInfluence;
    }

    public DrugInfluence()
    {

    }

    public void update(DrugHelper drugHelper)
    {
        if (delay > 0)
        {
            delay--;
        }

        if (delay == 0 && maxInfluence > 0.0)
        {
            double addition = Math.min(maxInfluence, influenceSpeedPlus + maxInfluence * influenceSpeed);

            addToDrug(drugHelper, addition);
            maxInfluence -= addition;
        }
    }

    public void addToDrug(DrugHelper drugHelper, double value)
    {
        drugHelper.addToDrug(drugName, value);
    }

    public boolean isDone()
    {
        return maxInfluence <= 0.0;
    }

    public DrugInfluence clone()
    {
        DrugInfluence inf = null;
        try
        {
            inf = getClass().newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        if (inf != null)
        {
            NBTTagCompound cmp = new NBTTagCompound();
            writeToNBT(cmp);

            inf.readFromNBT(cmp);
        }

        return inf;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        drugName = compound.getString("drugName");

        delay = compound.getInteger("delay");

        influenceSpeed = compound.getDouble("influenceSpeed");
        influenceSpeedPlus = compound.getDouble("influenceSpeedPlus");

        maxInfluence = compound.getDouble("maxInfluence");
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setString("drugName", drugName);

        compound.setInteger("delay", delay);

        compound.setDouble("influenceSpeed", influenceSpeed);
        compound.setDouble("influenceSpeedPlus", influenceSpeedPlus);

        compound.setDouble("maxInfluence", maxInfluence);
    }
}
