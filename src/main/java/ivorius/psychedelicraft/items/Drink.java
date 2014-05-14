/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import com.sun.tools.javac.util.Pair;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public class Drink implements IDrink
{
    public List<DrugInfluence> drugInfluences;
    public int foodLevel;
    public float foodSaturation;

    public Drink(int foodLevel, float foodSaturation, DrugInfluence... drugInfluences)
    {
        this.drugInfluences = Arrays.asList(drugInfluences);
        this.foodLevel = foodLevel;
        this.foodSaturation = foodSaturation;
    }

    public Drink(DrugInfluence... drugInfluences)
    {
        this(0, 0.0f, drugInfluences);
    }

    @Override
    public List<DrugInfluence> getDrugInfluences(ItemStack stack)
    {
        return drugInfluences;
    }

    @Override
    public Pair<Integer, Float> getFoodLevel(ItemStack stack)
    {
        return foodLevel != 0 || foodSaturation != 0.0f ? new Pair<Integer, Float>(foodLevel, foodSaturation) : null;
    }

    @Override
    public void applyToEntity(ItemStack stack, World world, EntityLivingBase entityLivingBase)
    {

    }

    @Override
    public String getSpecialTranslationKey(ItemStack stack)
    {
        return null;
    }
}
