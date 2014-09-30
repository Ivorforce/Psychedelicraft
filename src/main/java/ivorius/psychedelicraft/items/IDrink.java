/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by lukas on 14.05.14.
 */
public interface IDrink
{
    public List<DrugInfluence> getDrugInfluences(ItemStack stack);

    public Pair<Integer, Float> getFoodLevel(ItemStack stack);

    public void applyToEntity(ItemStack stack, World world, EntityLivingBase entityLivingBase);

    public String getSpecialTranslationKey(ItemStack stack);
}
