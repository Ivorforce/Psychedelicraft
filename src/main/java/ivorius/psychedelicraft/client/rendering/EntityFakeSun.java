/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by lukas on 25.03.14.
 */
public class EntityFakeSun extends EntityLivingBase
{
    public EntityLivingBase prevViewEntity;

    public EntityFakeSun(World par1World)
    {
        super(par1World);
    }

    public EntityFakeSun(EntityLivingBase prevViewEntity)
    {
        super(prevViewEntity.worldObj);

        this.prevViewEntity = prevViewEntity;

        NBTTagCompound cmp = new NBTTagCompound();
        prevViewEntity.writeToNBT(cmp);
        readFromNBT(cmp);
    }

    @Override
    public ItemStack getHeldItem()
    {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int var1)
    {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int var1, ItemStack var2)
    {

    }

    @Override
    public ItemStack[] getLastActiveItems()
    {
        return new ItemStack[0];
    }
}
