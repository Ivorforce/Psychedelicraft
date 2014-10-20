/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.DrugInfluence;
import ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemMolotovCocktail extends ItemDrinkHolder
{
    public static float getAlcohol(ItemStack stack)
    {
        DrinkInformation drinkInfo = null;

        if (stack.getItem() instanceof ItemDrinkHolder)
            drinkInfo = ((ItemDrinkHolder) stack.getItem()).getDrinkInfo(stack);

        if (drinkInfo != null)
        {
            float alcohol = 0.0f;
            for (DrugInfluence drugInfluence : drinkInfo.getDrugInfluences())
            {
                if (drugInfluence.getDrugName().equals("Alcohol"))
                    alcohol += drugInfluence.getMaxInfluence();
            }
            return MathHelper.clamp_float(alcohol, 0.0f, 1.0f);
        }

        return 0.0f;
    }

    public ItemMolotovCocktail()
    {
        super();
        maxStackSize = 16;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeUsed)
    {
        float partUsed = MathHelper.clamp_float((float) (getMaxItemUseDuration(stack) - timeUsed) / 30.0f, 0.0f, 1.0f);

        stack.stackSize--;
        if (stack.stackSize <= 0)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            EntityMolotovCocktail molotovCocktail = new EntityMolotovCocktail(world, player);
            molotovCocktail.fireStrength = MathHelper.floor_float(getAlcohol(stack) * 32.0f);
            molotovCocktail.motionX *= partUsed;
            molotovCocktail.motionY *= partUsed;
            molotovCocktail.motionZ *= partUsed;

            world.spawnEntityInWorld(molotovCocktail);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player)
    {
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));

        return stack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 7200;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        DrinkInformation drinkInfo = getDrinkInfo(stack);

        if (drinkInfo != null)
        {
            int quality = MathHelper.clamp_int(MathHelper.floor_float(getAlcohol(stack) * 14.0f + 0.5f), 0, 6);

            if (quality == 0)
            {
                String drinkName = I18n.format(drinkInfo.getFullTranslationKey());
                return I18n.format(this.getUnlocalizedNameInefficiently(stack) + ".quality" + quality + ".name", drinkName);
            }
            else
                return I18n.format(this.getUnlocalizedNameInefficiently(stack) + ".quality" + quality + ".name");
        }

        return I18n.format(this.getUnlocalizedNameInefficiently(stack) + ".empty.name");
    }
}
