/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import ivorius.ivtoolkit.network.IvNetworkHelperServer;
import ivorius.ivtoolkit.network.PartialUpdateHandler;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.client.rendering.DrugEffectInterpreter;
import ivorius.psychedelicraft.client.rendering.IDrugRenderer;
import ivorius.psychedelicraft.network.PSNetworkHelperServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class DrugHelper implements IExtendedEntityProperties, PartialUpdateHandler
{
    public static final UUID drugUUID = UUID.fromString("2da054e7-0fe0-4fb4-bf2c-a185a5f72aa1"); // Randomly gen'd

    public static boolean waterOverlayEnabled;
    public static boolean hurtOverlayEnabled;
    public static float[] digitalEffectPixelRescale;

    private Hashtable<String, Drug> drugs;
    public ArrayList<DrugInfluence> influences;

    public boolean hasChanges;

    public ArrayList<DrugHallucination> hallucinations = new ArrayList<DrugHallucination>();

    public IDrugRenderer drugRenderer;
    public DrugMessageDistorter drugMessageDistorter;

    public int ticksExisted = 0;

    public int timeBreathingSmoke;
    public float[] breathSmokeColor;

    public int delayUntilHeartbeat;
    public int delayUntilBreath;
    public boolean breathDeep;

    public DrugHelper()
    {
        drugs = new Hashtable<String, Drug>();
        influences = new ArrayList<DrugInfluence>();

        drugMessageDistorter = new DrugMessageDistorter();

        addDrug("Alcohol", new Drug(1, 0.0002d));
        addDrug("Cannabis", new Drug(1, 0.0002d));
        addDrug("BrownShrooms", new Drug(1, 0.0002d).setShouldApplyToShader(true));
        addDrug("RedShrooms", new Drug(1, 0.0002d).setShouldApplyToShader(true));
        addDrug("Tobacco", new Drug(1, 0.003d));
        addDrug("Cocaine", new Drug(1, 0.0003d));
        addDrug("Caffeine", new Drug(1, 0.0002d));
        addDrug("Warmth", new Drug(1, 0.004d, true));
        addDrug("Peyote", new Drug(1, 0.0002d).setShouldApplyToShader(true));
        addDrug("Zero", new Drug(1, 0.0001d));
        addDrug("Power", new Drug(0.95, 0.0001d, true));

        addDrug("Harmonium", new DrugHarmonium(1, 0.0003d));
    }

    public static DrugHelper getDrugHelper(Entity entity)
    {
        if (entity != null)
        {
            return (DrugHelper) entity.getExtendedProperties("DrugHelper");
        }

        return null;
    }

    public static void initInEntity(Entity entity)
    {
        entity.registerExtendedProperties("DrugHelper", new DrugHelper());
        DrugHelper drugHelper = getDrugHelper(entity);

        if (drugHelper != null)
        {
            Psychedelicraft.proxy.createDrugRender(drugHelper);
        }
    }

    public void addDrug(String drugName, Drug drug)
    {
        drugs.put(drugName, drug);
    }

    public Drug getDrug(String drugName)
    {
        if (!drugs.containsKey(drugName))
        {
            return null;
        }

        return drugs.get(drugName);
    }

    public float getDrugValue(String drugName)
    {
        return (float) drugs.get(drugName).getActiveValue();
    }

    public float getDrugClamped(String drugName, float min, float max)
    {
        return MathHelper.clamp_float((getDrugValue(drugName) - min) / (max - min), 0.0f, 1.0f);
    }

    public void addToDrug(String drugName, double effect)
    {
        if (!drugs.containsKey(drugName))
        {
            Psychedelicraft.logger.warn("Tried to add to drug " + drugName);
            return;
        }

        hasChanges = true;
        drugs.get(drugName).addToDesiredValue(effect);
    }

    public void setDrugValue(String drugName, double effect)
    {
        if (!drugs.containsKey(drugName))
        {
            Psychedelicraft.logger.warn("Tried to set drug value " + drugName);
            return;
        }

        hasChanges = true;
        drugs.get(drugName).setDesiredValue(effect);
    }

    public void addToDrug(DrugInfluence influence)
    {
        hasChanges = true;
        influences.add(influence);
    }

    public Collection<Drug> getAllDrugs()
    {
        return drugs.values();
    }

    public String[] getAllVisibleDrugNames()
    {
        ArrayList<String> visibleDrugs = new ArrayList<String>();

        for (String s : drugs.keySet())
        {
            if (drugs.get(s).isVisible())
            {
                visibleDrugs.add(s);
            }
        }

        String[] returnArray = new String[visibleDrugs.size()];
        visibleDrugs.toArray(returnArray);

        return returnArray;
    }

    public boolean doesDrugExist(String name)
    {
        return drugs.containsKey(name);
    }

    public void addRandomHallucination(EntityPlayer player)
    {
        if (!player.worldObj.isRemote)
        {
            return;
        }

        if (getNumberOfHallucinations(DrugHallucinationRastaHead.class) == 0 && (player.getRNG().nextFloat() < 0.1f && getDrugValue("Cannabis") > 0.4f))
        {
            hallucinations.add(new DrugHallucinationRastaHead(player));
        }
        else
        {
            hallucinations.add(new DrugHallucinationEntity(player));
        }
    }

    public int getNumberOfHallucinations(Class aClass)
    {
        int count = 0;
        for (DrugHallucination hallucination : hallucinations)
        {
            if (aClass.isAssignableFrom(hallucination.getClass()))
            {
                count++;
            }
        }

        return count;
    }

    public void startBreathingSmoke(int time, float[] color)
    {
        if (color != null)
        {
            this.breathSmokeColor = color;
        }
        else
        {
            this.breathSmokeColor = new float[]{1.0f, 1.0f, 1.0f};
        }

        this.timeBreathingSmoke = time + 10; //10 is the time spent breathing in
    }

    public void updateDrugEffects(EntityLivingBase entity)
    {
        if (ticksExisted % 5 == 0) //4 times / sec is enough
        {
            Iterator<DrugInfluence> influenceIterator = influences.iterator();

            while (influenceIterator.hasNext())
            {
                DrugInfluence influence = influenceIterator.next();
                influence.update(this);

                if (influence.isDone())
                {
                    influenceIterator.remove();
                }
            }
        }

        for (String key : drugs.keySet())
        {
            Drug drug = drugs.get(key);
            drug.updateValues();
        }

        if (getDrugValue("Alcohol") > 0.0f)
        {
            if ((ticksExisted % 20) == 0)
            {
                if (getDrugValue("Alcohol") > 0.8f)
                {
                    entity.attackEntityFrom(DamageSource.starve, (int) ((getDrugValue("Alcohol") - 0.8f) * 25 + 1.0f));
                }
            }

            float d = getDrugValue("Alcohol");
            if (d > 0.8F)
            {
                d = 0.8F;
            }

//            player.motionX += MathHelper.sin(ticksExisted / 10.0F * (float) Math.PI) / 40.0F * d * (player.worldObj.rand.nextFloat() + 0.5F);
//            player.motionZ += MathHelper.cos(ticksExisted / 10.0F * (float) Math.PI) / 40.0F * d * (player.worldObj.rand.nextFloat() + 0.5F);
//
//            player.motionX *= (player.worldObj.rand.nextFloat() - 0.5F) * 2 * d + 1.0F;
//            player.motionZ *= (player.worldObj.rand.nextFloat() - 0.5F) * 2 * d + 1.0F;

            entity.rotationPitch += MathHelper.sin(ticksExisted / 600.0F * (float) Math.PI) / 2.0F * d * (entity.worldObj.rand.nextFloat() + 0.5F);
            entity.rotationYaw += MathHelper.cos(ticksExisted / 500.0F * (float) Math.PI) / 1.3F * d * (entity.worldObj.rand.nextFloat() + 0.5F);

            entity.rotationPitch += MathHelper.sin(ticksExisted / 180.0F * (float) Math.PI) / 3.0F * d * (entity.worldObj.rand.nextFloat() + 0.5F);
            entity.rotationYaw += MathHelper.cos(ticksExisted / 150.0F * (float) Math.PI) / 2.0F * d * (entity.worldObj.rand.nextFloat() + 0.5F);
        }

        if (getDrugValue("Cannabis") > 0.0f)
        {
            if (entity instanceof EntityPlayer)
            {
                ((EntityPlayer) entity).addExhaustion(0.03F * getDrugValue("Cannabis"));
            }
        }

        if (getDrugValue("Cocaine") > 0.0f)
        {
            if (!entity.worldObj.isRemote)
            {
                float chance = (getDrugValue("Cocaine") - 0.8f) * 0.1f;

                if (ticksExisted % 20 == 0 && entity.getRNG().nextFloat() < chance)
                {
                    entity.attackEntityFrom(DamageSource.magic, 1000);
                }
            }
        }

        if (entity.worldObj.isRemote)
        {
            updateHallucinations(entity);

            if (delayUntilHeartbeat > 0)
            {
                delayUntilHeartbeat--;
            }
            if (delayUntilBreath > 0)
            {
                delayUntilBreath--;
            }

            if (delayUntilHeartbeat == 0)
            {
                float heartbeatVolume = 0.0f;
                if (getDrugValue("Cocaine") > 0.4f)
                {
                    heartbeatVolume += (getDrugValue("Cocaine") - 0.4f) * 2.0f;
                }
                if (getDrugValue("Caffeine") > 0.6f)
                {
                    heartbeatVolume += (getDrugValue("Caffeine") - 0.6f) * 0.5f;
                }

                if (heartbeatVolume > 0.0f)
                {
                    float speed = 1.0f;
                    speed += getDrugValue("Cocaine") * 0.1f;
                    speed += getDrugValue("Caffeine") * 0.2f;

                    delayUntilHeartbeat = (int) (35.0f - (speed - 1.0f) * 80.0f);
                    entity.worldObj.playSound(entity.posX, entity.posY, entity.posZ, Psychedelicraft.soundBase + "heartBeat", heartbeatVolume, speed, false);
                }
            }
            if (getDrugValue("Cocaine") > 0.4f && delayUntilBreath == 0)
            {
                delayUntilBreath = (int) (30.0f - getDrugValue("Cocaine") * 10.0f);
                breathDeep = !breathDeep;

                entity.worldObj.playSound(entity.posX, entity.posY, entity.posZ, Psychedelicraft.soundBase + "breath", (getDrugValue("Cocaine") - 0.4f) * 1.5f, 1.0f + getDrugValue("Cocaine") * 0.1f + (breathDeep ? 0.15f : 0.0f), false);
            }

            if (getDrugValue("Caffeine") > 0.0f)
            {
                if (entity.onGround)
                {
                    float jumpChance = 0.0f;
                    if (getDrugValue("Cocaine") > 0.6f)
                    {
                        jumpChance += (getDrugValue("Cocaine") - 0.6f) * 0.03f;
                    }
                    if (getDrugValue("Caffeine") > 0.6f)
                    {
                        jumpChance += (getDrugValue("Caffeine") - 0.6f) * 0.07f;
                    }

                    if (entity instanceof EntityPlayer)
                    {
                        if (entity.getRNG().nextFloat() < jumpChance)
                        {
                            ((EntityPlayer) entity).jump(); // Also do this with EntityLivingBase, but that has protected access
                        }
                    }
                }

                if (!entity.isSwingInProgress)
                {
                    float punchChance = 0.0f;
                    if (getDrugValue("Cocaine") > 0.3f)
                    {
                        punchChance += (getDrugValue("Cocaine") - 0.5f) * 0.02f;
                    }
                    if (getDrugValue("Caffeine") > 0.3f)
                    {
                        punchChance += (getDrugValue("Caffeine") - 0.3f) * 0.05f;
                    }

                    if (entity.getRNG().nextFloat() < punchChance)
                    {
                        entity.swingItem();
                    }
                }
            }
        }

        if (timeBreathingSmoke > 0)
        {
            timeBreathingSmoke--;

            if (timeBreathingSmoke > 10 && entity.worldObj.isRemote)
            {
                Vec3 look = entity.getLookVec();

                if (entity.getRNG().nextInt(2) == 0)
                {
                    float s = entity.getRNG().nextFloat() * 0.05f + 0.1f;
                    Psychedelicraft.proxy.spawnColoredParticle(entity, breathSmokeColor, look, s, 1.0f);
                }
                if (entity.getRNG().nextInt(5) == 0)
                {
                    float s = entity.getRNG().nextFloat() * 0.05f + 0.1f;
                    Psychedelicraft.proxy.spawnColoredParticle(entity, breathSmokeColor, look, s, 2.5f);
                }
            }
        }

        if (drugRenderer != null && entity.worldObj.isRemote)
        {
            drugRenderer.update(this, entity);
        }

        changeDrugModifierMultiply(entity, SharedMonsterAttributes.movementSpeed, getSpeedModifier(entity));

        ticksExisted++;

        if (hasChanges)
        {
            hasChanges = false;

            if (!entity.worldObj.isRemote)
                PSNetworkHelperServer.sendEEPUpdatePacket(entity, "DrugHelper", "DrugData", Psychedelicraft.network);
        }
    }

    public void updateHallucinations(EntityLivingBase entity)
    {
        float hallucinationChance = DrugEffectInterpreter.getHallucinationStrength(this, 1.0f) * 0.05f;
        if (hallucinationChance > 0.0f)
        {
            if (entity.getRNG().nextInt((int) (1F / hallucinationChance)) == 0)
            {
                if (entity instanceof EntityPlayer)
                {
                    addRandomHallucination((EntityPlayer) entity);
                }
            }
        }

        ArrayList<DrugHallucination> toRemove = null;
        for (DrugHallucination h : hallucinations)
        {
            h.update();

            if (h.isDead())
            {
                if (toRemove == null)
                {
                    toRemove = new ArrayList<DrugHallucination>();
                }
                toRemove.add(h);
            }
        }

        if (toRemove != null)
        {
            hallucinations.removeAll(toRemove);
        }
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound, boolean fromPacket)
    {
        for (String key : drugs.keySet())
        {
            NBTTagCompound cmp = par1NBTTagCompound.getCompoundTag(key);

            if (cmp != null)
            {
                drugs.get(key).readFromNBT(cmp);
            }
        }

        influences.clear();
        NBTTagList influenceTagList = par1NBTTagCompound.getTagList("drugInfluences", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < influenceTagList.tagCount(); i++)
        {
            NBTTagCompound compound = influenceTagList.getCompoundTagAt(i);

            Class<? extends DrugInfluence> influenceClass = DrugInfluence.keyToClass.get(compound.getString("influenceClass"));

            if (influenceClass != null)
            {
                DrugInfluence inf = null;

                try
                {
                    inf = influenceClass.newInstance();
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
                    inf.readFromNBT(compound);
                    addToDrug(inf);
                }
            }
        }

        this.ticksExisted = par1NBTTagCompound.getInteger("drugsTicksExisted");

        if (fromPacket)
        {
            hasChanges = true;
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        for (String key : drugs.keySet())
        {
            NBTTagCompound cmp = new NBTTagCompound();
            drugs.get(key).writeToNBT(cmp);
            par1NBTTagCompound.setTag(key, cmp);
        }

        NBTTagList influenceTagList = new NBTTagList();
        for (DrugInfluence influence : influences)
        {
            NBTTagCompound compound = new NBTTagCompound();
            influence.writeToNBT(compound);
            compound.setString("influenceClass", DrugInfluence.classToKey.get(influence.getClass()));
            influenceTagList.appendTag(compound);
        }
        par1NBTTagCompound.setTag("drugInfluences", influenceTagList);

        par1NBTTagCompound.setInteger("drugsTicksExisted", ticksExisted);
    }

    public NBTTagCompound createNBTTagCompound()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return tagCompound;
    }

    public void wakeUp(EntityLivingBase entity)
    {
        for (String key : drugs.keySet())
        {
            drugs.get(key).resetDrugValue();
        }
    }

    public void receiveChatMessage(EntityLivingBase entity, String message)
    {
        for (DrugHallucination h : hallucinations)
        {
            h.receiveChatMessage(message, entity);
        }
    }

    public float getSpeedModifier(EntityLivingBase entity)
    {
        float modifier = 1.0F;

        modifier *= (1.0F - getDrugValue("Cannabis")) * 0.5F + 0.5F;
        modifier *= 1.0F + getDrugValue("Cocaine") * 0.15F;
        modifier *= 1.0F + getDrugValue("Caffeine") * 0.20F;

        return modifier;
    }

    public float getDigSpeedModifier(EntityLivingBase entity)
    {
        float modifier = 1.0F;

        modifier *= (1.0F - getDrugValue("Cannabis")) * 0.5F + 0.5F;
        modifier *= 1.0F + getDrugValue("Cocaine") * 0.15F;
        modifier *= 1.0F + getDrugValue("Caffeine") * 0.20F;

        return modifier;
    }

    public EntityPlayer.EnumStatus getDrugSleepStatus()
    {
        if (getDrugValue("Cocaine") > 0.4f)
        {
            return Psychedelicraft.sleepStatusDrugs;
        }

        if (getDrugValue("Caffeine") > 0.1f)
        {
            return Psychedelicraft.sleepStatusDrugs;
        }

        return null;
    }

    public float getSoundMultiplier()
    {
        return 1.0f - getDrugValue("Power");
    }

    public float[] getDigitalEffectPixelResize()
    {
        return digitalEffectPixelRescale;
    }

    public void changeDrugModifierMultiply(EntityLivingBase entity, IAttribute attribute, double value)
    {
        // 2: ret *= 1.0 + value
        changeDrugModifier(entity, attribute, value - 1.0, 2);
    }

    public void changeDrugModifier(EntityLivingBase entity, IAttribute attribute, double value, int mode)
    {
        IAttributeInstance speedInstance = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        AttributeModifier oldModifier = speedInstance.getModifier(DrugHelper.drugUUID);
        if (oldModifier != null)
        {
            speedInstance.removeModifier(oldModifier);
        }
        AttributeModifier newModifier = new AttributeModifier(DrugHelper.drugUUID, "Drug Effects", value, mode);
        speedInstance.applyModifier(newModifier);
    }

    // IExtendedEntityProperties

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        compound.setTag("drugData", createNBTTagCompound());
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
        readFromNBT(compound.getCompoundTag("drugData"), true);
    }

    @Override
    public void init(Entity entity, World world)
    {

    }

    @Override
    public void writeUpdateData(ByteBuf buffer, String context, Object... params)
    {
        if ("DrugData".equals(context))
        {
            NBTTagCompound compound = new NBTTagCompound();
            writeToNBT(compound);
            ByteBufUtils.writeTag(buffer, compound);
        }
    }

    @Override
    public void readUpdateData(ByteBuf buffer, String context)
    {
        if ("DrugData".equals(context))
        {
            readFromNBT(ByteBufUtils.readTag(buffer), true);
        }
    }
}
