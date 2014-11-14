package ivorius.psychedelicraft.entities.drugs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lukas on 14.11.14.
 */
public class DrugHallucinationManager
{
    public List<DrugHallucination> entities = new ArrayList<>();

    public void update(EntityLivingBase entity, DrugHelper drugHelper)
    {
        updateEntities(entity, drugHelper);
    }

    public void updateEntities(EntityLivingBase entity, DrugHelper drugHelper)
    {
        float hallucinationChance = getHallucinationStrength(drugHelper, 1.0f) * 0.05f;
        if (hallucinationChance > 0.0f)
        {
            if (entity.getRNG().nextInt((int) (1F / hallucinationChance)) == 0)
            {
                if (entity instanceof EntityPlayer)
                {
                    addRandomHallucination((EntityPlayer) entity, drugHelper);
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

    public void addRandomHallucination(EntityPlayer player, DrugHelper drugHelper)
    {
        if (!player.worldObj.isRemote)
        {
            return;
        }

        if (getNumberOfHallucinations(DrugHallucinationRastaHead.class) == 0 && (player.getRNG().nextFloat() < 0.1f && drugHelper.getDrugValue("Cannabis") > 0.4f))
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

    public void receiveChatMessage(EntityLivingBase entity, String message)
    {
        for (DrugHallucination h : entities)
        {
            h.receiveChatMessage(message, entity);
        }
    }

    public float getHallucinationStrength(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += (1.0f - value) * drug.hallucinationStrength();
        return value;
    }

    public float getDesaturation(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += (1.0f - value) * drug.desaturationHallucinationStrength();
        return value;
    }

    public float getColorIntensification(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += (1.0f - value) * drug.superSaturationHallucinationStrength();
        return value;
    }

    public float getSlowColorRotation(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += (1.0f - value) * drug.slowColorRotationHallucinationStrength();
        return value;
    }

    public float getQuickColorRotation(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += (1.0f - value) * drug.quickColorRotationHallucinationStrength();
        return value;
    }

    public float getBigWaveStrength(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += drug.bigWaveHallucinationStrength();
        return value;
    }

    public float getSmallWaveStrength(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += drug.smallWaveHallucinationStrength();
        return value;
    }

    public float getWiggleWaveStrength(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += drug.wiggleWaveHallucinationStrength();
        return value;
    }

    public float getRedPulsesStrength(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += drug.redPulsesHallucinationStrength();
        return value;
    }

    public float getSurfaceFractalStrength(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += drug.surfaceFractalHallucinationStrength();
        return value;
    }

    public float getDistantWorldDeformationStrength(DrugHelper drugHelper, float partialTicks)
    {
        float value = 0.0f;
        for (Drug drug : drugHelper.getAllDrugs())
            value += drug.distantWorldDeformationHallucinationStrength();
        return value;
    }

    public void applyWorldColorizationHallucinationStrength(DrugHelper drugHelper, float[] worldColorization)
    {
        for (Drug drug : drugHelper.getAllDrugs())
            drug.applyWorldColorizationHallucinationStrength(worldColorization);
    }
}
