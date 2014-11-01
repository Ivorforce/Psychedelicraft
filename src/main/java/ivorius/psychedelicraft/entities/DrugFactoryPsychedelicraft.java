/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import net.minecraft.entity.EntityLivingBase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugFactoryPsychedelicraft implements DrugFactory
{
    @Override
    public void createDrugs(EntityLivingBase entity, List<Pair<String, Drug>> drugs)
    {
        addDrug("Alcohol", new DrugSimple(1, 0.0002d), drugs);
        addDrug("Cannabis", new DrugSimple(1, 0.0002d), drugs);
        addDrug("BrownShrooms", new DrugSimple(1, 0.0002d).setShouldApplyToShader(true), drugs);
        addDrug("RedShrooms", new DrugSimple(1, 0.0002d).setShouldApplyToShader(true), drugs);
        addDrug("Tobacco", new DrugSimple(1, 0.003d), drugs);
        addDrug("Cocaine", new DrugSimple(1, 0.0003d), drugs);
        addDrug("Caffeine", new DrugSimple(1, 0.0002d), drugs);
        addDrug("Warmth", new DrugSimple(1, 0.004d, true), drugs);
        addDrug("Peyote", new DrugSimple(1, 0.0002d).setShouldApplyToShader(true), drugs);
        addDrug("Zero", new DrugSimple(1, 0.0001d), drugs);
        addDrug("Power", new DrugSimple(0.95, 0.0001d, true), drugs);

        addDrug("Harmonium", new DrugHarmonium(1, 0.0003d), drugs);
    }

    public void addDrug(String key, Drug drug, List<Pair<String, Drug>> drugs)
    {
        drugs.add(new ImmutablePair<String, Drug>(key, drug));
    }
}
