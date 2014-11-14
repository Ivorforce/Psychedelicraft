package ivorius.psychedelicraft.entities.drugs;

import ivorius.ivtoolkit.math.IvMathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.util.*;

/**
 * Created by lukas on 14.11.14.
 */
public class DrugHallucinationManager
{
    public static final int HALLUCATION_ENTITIES = 0;
    public static final int HALLUCATION_DESATURATION = 1;
    public static final int HALLUCATION_SUPER_SATURATION = 2;
    public static final int HALLUCATION_SLOW_COLOR_ROTATION = 3;
    public static final int HALLUCATION_QUICK_COLOR_ROTATION = 4;
    public static final int HALLUCATION_BIG_WAVES = 5;
    public static final int HALLUCATION_SMALL_WAVES = 6;
    public static final int HALLUCATION_WIGGLE_WAVES = 7;
    public static final int HALLUCATION_RED_PULSES = 8;
    public static final int HALLUCATION_SURFACE_FRACTALS = 9;
    public static final int HALLUCATION_DISTANT_WORLD_DEFORMATION = 10;
    public static final int HALLUCATION_BLOOM = 11;

    public final List<DrugHallucination> entities = new ArrayList<>();

    public final List<HallucinationType> hallucinationTypes = new ArrayList<>();
    public final List<Integer> activeHallucinations = new ArrayList<>();
    public final Map<Integer, Float> hallucinationValues = new HashMap<>();

    public DrugHallucinationManager()
    {
        HallucinationType colorHallucinationType = new HallucinationType()
        {
            @Override
            public float getDesiredValue(DrugHelper drugHelper)
            {
                float val = 0.0f;
                for (Drug drug : drugHelper.getAllDrugs())
                    val += drug.colorHallucinationStrength();
                return val;
            }
        };
        hallucinationTypes.add(colorHallucinationType);
        Collections.addAll(colorHallucinationType.hallucinations, HALLUCATION_DESATURATION, HALLUCATION_SUPER_SATURATION, HALLUCATION_SLOW_COLOR_ROTATION, HALLUCATION_QUICK_COLOR_ROTATION, HALLUCATION_RED_PULSES, HALLUCATION_SURFACE_FRACTALS, HALLUCATION_BLOOM);

        HallucinationType movementHallucinationType = new HallucinationType()
        {
            @Override
            public float getDesiredValue(DrugHelper drugHelper)
            {
                float val = 0.0f;
                for (Drug drug : drugHelper.getAllDrugs())
                    val += drug.movementHallucinationStrength();
                return val;
            }
        };
        hallucinationTypes.add(movementHallucinationType);
        Collections.addAll(movementHallucinationType.hallucinations, HALLUCATION_BIG_WAVES, HALLUCATION_SMALL_WAVES, HALLUCATION_WIGGLE_WAVES, HALLUCATION_DISTANT_WORLD_DEFORMATION);

        HallucinationType contextualHallucinationType = new HallucinationType()
        {
            @Override
            public float getDesiredValue(DrugHelper drugHelper)
            {
                float val = 0.0f;
                for (Drug drug : drugHelper.getAllDrugs())
                    val += drug.contextualHallucinationStrength();
                return val;
            }
        };
        hallucinationTypes.add(contextualHallucinationType);
        Collections.addAll(contextualHallucinationType.hallucinations, HALLUCATION_ENTITIES);

        hallucinationValues.put(HALLUCATION_ENTITIES, 0.0f);
        hallucinationValues.put(HALLUCATION_DESATURATION, 0.0f);
        hallucinationValues.put(HALLUCATION_SUPER_SATURATION, 0.0f);
        hallucinationValues.put(HALLUCATION_SLOW_COLOR_ROTATION, 0.0f);
        hallucinationValues.put(HALLUCATION_QUICK_COLOR_ROTATION, 0.0f);
        hallucinationValues.put(HALLUCATION_BIG_WAVES, 0.0f);
        hallucinationValues.put(HALLUCATION_SMALL_WAVES, 0.0f);
        hallucinationValues.put(HALLUCATION_WIGGLE_WAVES, 0.0f);
        hallucinationValues.put(HALLUCATION_RED_PULSES, 0.0f);
        hallucinationValues.put(HALLUCATION_SURFACE_FRACTALS, 0.0f);
        hallucinationValues.put(HALLUCATION_DISTANT_WORLD_DEFORMATION, 0.0f);
        hallucinationValues.put(HALLUCATION_BLOOM, 0.0f);
    }

    public void update(EntityLivingBase entity, DrugHelper drugHelper)
    {
        Random random = entity.getRNG();

        updateEntities(entity, drugHelper, random);

        float totalHallucinationValue = 0.0f;
        for (HallucinationType type : hallucinationTypes)
        {
            float desiredValue = type.getDesiredValue(drugHelper);
            type.currentValue = IvMathHelper.nearValue(type.currentValue, desiredValue, 0.01f, 0.01f);
            totalHallucinationValue += type.currentValue;
        }

        int desiredHallucinations = MathHelper.floor_float(totalHallucinationValue * 4.0f + 0.9f);

        if (activeHallucinations.size() > 0)
        {
            while (random.nextFloat() < 1.0f / (20 * 60 * 2 / activeHallucinations.size()))
            {
                removeRandomHallucination(random);
                addRandomHallucination(random);
            }
        }

        while (activeHallucinations.size() > desiredHallucinations)
            removeRandomHallucination(random);
        while (activeHallucinations.size() < desiredHallucinations && desiredHallucinations < hallucinationValues.size())
            addRandomHallucination(random);

        for (Integer hKey : hallucinationValues.keySet())
        {
            float val = hallucinationValues.get(hKey);

            if (activeHallucinations.contains(hKey))
            {
                float pureDesiredValue = 1.0f + MathHelper.sin(drugHelper.ticksExisted * 0.00121f) * 0.5f;
                pureDesiredValue *= 1.0f + MathHelper.sin(drugHelper.ticksExisted * 0.0019318f) * 0.5f;
                float desiredValue = pureDesiredValue * getHallucinationMultiplier(hKey);

                val = IvMathHelper.nearValue(val, desiredValue, 0.002f, 0.002f);
                hallucinationValues.put(hKey, val);
            }
            else
            {
                val = IvMathHelper.nearValue(val, 0.0f, 0.002f, 0.002f);
                hallucinationValues.put(hKey, val);
            }
        }
    }

    public void updateEntities(EntityLivingBase entity, DrugHelper drugHelper, Random random)
    {
        float hallucinationChance = getHallucinationStrength(drugHelper, 1.0f) * 0.05f;
        if (hallucinationChance > 0.0f)
        {
            if (random.nextInt((int) (1F / hallucinationChance)) == 0)
            {
                if (entity instanceof EntityPlayer)
                {
                    addRandomEntityHallucination((EntityPlayer) entity, drugHelper, random);
                }
            }
        }

        for (Iterator<DrugHallucination> iterator = entities.iterator(); iterator.hasNext(); )
        {
            DrugHallucination hallucination = iterator.next();
            hallucination.update();

            if (hallucination.isDead())
                iterator.remove();
        }
    }

    public void addRandomEntityHallucination(EntityPlayer player, DrugHelper drugHelper, Random random)
    {
        if (!player.worldObj.isRemote)
        {
            return;
        }

        if (getNumberOfHallucinations(DrugHallucinationRastaHead.class) == 0 && (random.nextFloat() < 0.1f && drugHelper.getDrugValue("Cannabis") > 0.4f))
        {
            entities.add(new DrugHallucinationRastaHead(player));
        }
        else
        {
            entities.add(new DrugHallucinationEntity(player));
        }
    }

    public int getNumberOfHallucinations(Class<? extends DrugHallucination> aClass)
    {
        int count = 0;
        for (DrugHallucination hallucination : entities)
        {
            if (aClass.isAssignableFrom(hallucination.getClass()))
                count++;
        }

        return count;
    }

    public void removeRandomHallucination(Random random)
    {
        activeHallucinations.remove(activeHallucinations.get(random.nextInt(activeHallucinations.size())));
    }

    public void addRandomHallucination(Random random)
    {
        float maxValue = 0.0f;
        int currentHallucination = -1;

        for (int hKey : hallucinationValues.keySet())
        {
            if (!activeHallucinations.contains(hKey))
            {
                float value = random.nextFloat() * getHallucinationMultiplier(hKey);

                if (value > maxValue)
                {
                    currentHallucination = hKey;
                    maxValue = value;
                }
            }
        }

        if (currentHallucination >= 0)
            activeHallucinations.add(currentHallucination);
    }

    public float getHallucinationMultiplier(int hallucination)
    {
        float value = 1.0f;
        for (HallucinationType type : hallucinationTypes)
        {
            if (type.hallucinations.contains(hallucination))
                value *= IvMathHelper.zeroToOne(type.currentValue, 0.0f, 0.1f);
        }
        return value;
    }

    public void receiveChatMessage(EntityLivingBase entity, String message)
    {
        for (DrugHallucination h : entities)
        {
            h.receiveChatMessage(message, entity);
        }
    }

    public float getHallucinationStrength(DrugHelper drugHelper, float partialTicks)
    {
        return getHallucinationMultiplier(HALLUCATION_ENTITIES) * hallucinationValues.get(HALLUCATION_ENTITIES);
    }

    public float getDesaturation(DrugHelper drugHelper, float partialTicks)
    {
        float value = getHallucinationMultiplier(HALLUCATION_DESATURATION) * hallucinationValues.get(HALLUCATION_DESATURATION);
        for (Drug drug : drugHelper.getAllDrugs())
            value += (1.0f - value) * drug.desaturationHallucinationStrength();
        return value;
    }

    public float getColorIntensification(DrugHelper drugHelper, float partialTicks)
    {
        float value = getHallucinationMultiplier(HALLUCATION_SUPER_SATURATION) * hallucinationValues.get(HALLUCATION_SUPER_SATURATION);
        for (Drug drug : drugHelper.getAllDrugs())
            value += (1.0f - value) * drug.superSaturationHallucinationStrength();
        return value;
    }

    public float getSlowColorRotation(DrugHelper drugHelper, float partialTicks)
    {
        return getHallucinationMultiplier(HALLUCATION_SLOW_COLOR_ROTATION) * hallucinationValues.get(HALLUCATION_SLOW_COLOR_ROTATION);
    }

    public float getQuickColorRotation(DrugHelper drugHelper, float partialTicks)
    {
        return getHallucinationMultiplier(HALLUCATION_QUICK_COLOR_ROTATION) * hallucinationValues.get(HALLUCATION_QUICK_COLOR_ROTATION);
    }

    public float getBigWaveStrength(DrugHelper drugHelper, float partialTicks)
    {
        return 0.6f * getHallucinationMultiplier(HALLUCATION_BIG_WAVES) * hallucinationValues.get(HALLUCATION_BIG_WAVES);
    }

    public float getSmallWaveStrength(DrugHelper drugHelper, float partialTicks)
    {
        return 0.5f * getHallucinationMultiplier(HALLUCATION_SMALL_WAVES) * hallucinationValues.get(HALLUCATION_SMALL_WAVES);
    }

    public float getWiggleWaveStrength(DrugHelper drugHelper, float partialTicks)
    {
        return 0.7f * getHallucinationMultiplier(HALLUCATION_WIGGLE_WAVES) * hallucinationValues.get(HALLUCATION_WIGGLE_WAVES);
    }

    public float getRedPulsesStrength(DrugHelper drugHelper, float partialTicks)
    {
        return getHallucinationMultiplier(HALLUCATION_RED_PULSES) * hallucinationValues.get(HALLUCATION_RED_PULSES);
    }

    public float getSurfaceFractalStrength(DrugHelper drugHelper, float partialTicks)
    {
        return getHallucinationMultiplier(HALLUCATION_SURFACE_FRACTALS) * hallucinationValues.get(HALLUCATION_SURFACE_FRACTALS);
    }

    public float getDistantWorldDeformationStrength(DrugHelper drugHelper, float partialTicks)
    {
        return getHallucinationMultiplier(HALLUCATION_DISTANT_WORLD_DEFORMATION) * hallucinationValues.get(HALLUCATION_DISTANT_WORLD_DEFORMATION);
    }

    public void applyWorldColorizationHallucinationStrength(DrugHelper drugHelper, float[] worldColorization)
    {
        for (Drug drug : drugHelper.getAllDrugs())
            drug.applyWorldColorizationHallucinationStrength(worldColorization);
    }

    public float getBloom(DrugHelper drugHelper, float partialTicks)
    {
        float value = 2.0f * getHallucinationMultiplier(HALLUCATION_BLOOM) * hallucinationValues.get(HALLUCATION_BLOOM);
        for (Drug drug : drugHelper.getAllDrugs())
            value += drug.bloomHallucinationStrength();
        return value;
    }

    public static abstract class HallucinationType
    {
        public final List<Integer> hallucinations = new ArrayList<>();
        public float currentValue;

        public abstract float getDesiredValue(DrugHelper drugHelper);
    }
}
