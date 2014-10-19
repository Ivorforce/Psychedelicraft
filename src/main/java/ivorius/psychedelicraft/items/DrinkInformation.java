/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

/**
 * Created by lukas on 19.10.14.
 */
public class DrinkInformation
{
    private String drinkID;
    private int fillings;
    private NBTTagCompound drinkInfo;

    public DrinkInformation(String drinkID, int fillings)
    {
        this.drinkID = drinkID;
        this.fillings = fillings;
    }

    public DrinkInformation(String drinkID, int fillings, NBTTagCompound drinkInfo)
    {
        this.drinkID = drinkID;
        this.fillings = fillings;
        this.drinkInfo = drinkInfo;
    }

    public DrinkInformation(NBTTagCompound tagCompound)
    {
        drinkID = tagCompound.getString("drinkID");
        fillings = tagCompound.getInteger("fillings");
        if (tagCompound.hasKey("drinkInfo", Constants.NBT.TAG_COMPOUND))
            drinkInfo = (NBTTagCompound) tagCompound.getCompoundTag("drinkInfo").copy();
    }

    public String getDrinkID()
    {
        return drinkID;
    }

    public IDrink getDrink()
    {
        return DrinkRegistry.getDrink(drinkID);
    }

    public void setFillings(int fillings)
    {
        this.fillings = fillings;
    }

    public void decrementFillings(int fillings)
    {
        this.fillings -= fillings;
    }

    public int getFillings()
    {
        return fillings;
    }

    public NBTTagCompound getDrinkInfo()
    {
        return drinkInfo != null ? drinkInfo : new NBTTagCompound();
    }

    public String getSpecialTranslationKey()
    {
        IDrink drink = getDrink();
        return drink != null ? drink.getSpecialTranslationKey(getDrinkInfo()) : null;
    }

    public String getFullTranslationKey()
    {
        IDrink drink = getDrink();
        return drink != null ? DrinkRegistry.getDrinkTranslationKey(drink, getDrinkInfo()) : null;
    }

    public String getSpecialIcon(Item item)
    {
        IDrink drink = getDrink();
        return drink != null ? DrinkRegistry.getDrinkSpecialIcon(drinkID, item) : null;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getDrinkIcon()
    {
        IDrink drink = getDrink();
        return drink != null ? drink.getDrinkIcon(getDrinkInfo()) : null;
    }

    public List<DrugInfluence> getDrugInfluences()
    {
        IDrink drink = getDrink();
        return drink != null ? drink.getDrugInfluences(getDrinkInfo()) : Collections.<DrugInfluence>emptyList();
    }

    public Pair<Integer, Float> getFoodLevel()
    {
        IDrink drink = getDrink();
        return drink != null ? drink.getFoodLevel(getDrinkInfo()) : null;
    }

    public void applyToEntity(EntityLivingBase entityPlayer, World world)
    {
        IDrink drink = getDrink();
        if (drink != null)
            drink.applyToEntity(getDrinkInfo(), entityPlayer, world);
    }

    public ItemStack createItemStack(ItemDrinkHolder item, int ticksFermented)
    {
        IDrink drink = getDrink();
        return drink != null ? drink.createItemStack(item, getDrinkInfo(), ticksFermented) : null;
    }

    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("drinkID", drinkID);
        tagCompound.setInteger("fillings", fillings);
        if (drinkInfo != null)
            tagCompound.setTag("drinkInfo", drinkInfo.copy());
        return tagCompound;
    }

    @Override
    public DrinkInformation clone()
    {
        return new DrinkInformation(drinkID, fillings, drinkInfo != null ? (NBTTagCompound) drinkInfo.copy() : null);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrinkInformation that = (DrinkInformation) o;

        if (fillings != that.fillings) return false;
        if (drinkID != null ? !drinkID.equals(that.drinkID) : that.drinkID != null) return false;
        if (drinkInfo != null ? !drinkInfo.equals(that.drinkInfo) : that.drinkInfo != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = drinkID != null ? drinkID.hashCode() : 0;
        result = 31 * result + fillings;
        result = 31 * result + (drinkInfo != null ? drinkInfo.hashCode() : 0);
        return result;
    }
}
