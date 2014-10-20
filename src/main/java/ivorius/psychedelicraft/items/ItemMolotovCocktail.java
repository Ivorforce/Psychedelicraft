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
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class ItemMolotovCocktail extends Item
{
    public static float getAlcohol(ItemStack stack)
    {
        DrinkInformation drinkInfo = null;

        if (stack.getItem() instanceof ItemDrinkHolder)
            drinkInfo = ((ItemDrinkHolder) stack.getItem()).getDrinkInfo(stack);
        else if (stack.getItem() instanceof ItemMolotovCocktail)
            drinkInfo = getDrinkInfo(stack);

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

    public static DrinkInformation getDrinkInfo(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("drinkInfo", Constants.NBT.TAG_COMPOUND) ? new DrinkInformation(stack.getTagCompound().getCompoundTag("drinkInfo")) : null;
    }

    public static ItemStack createMolotovStack(Item item, int stackSize, DrinkInformation drinkInformation)
    {
        ItemStack stack = new ItemStack(item, stackSize);
        stack.setTagInfo("drinkInfo", drinkInformation.writeToNBT());
        return stack;
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
        float partUsed = MathHelper.clamp_float((float)(getMaxItemUseDuration(stack) - timeUsed) / 30.0f, 0.0f, 1.0f);

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

        return this.getUnlocalizedNameInefficiently(stack) + ".empty.name";
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4)
    {
        super.addInformation(itemStack, player, list, par4);

        DrinkInformation drinkInfo = getDrinkInfo(itemStack);

        if (drinkInfo != null)
        {
            String translationKey = drinkInfo.getFullTranslationKey();
            if (translationKey != null)
                list.add(StatCollector.translateToLocal(translationKey).trim());
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        super.getSubItems(item, tab, list);

        for (IDrink drink : DrinkRegistry.getAllDrinks())
        {
            for (NBTTagCompound compound : drink.creativeTabInfos(item, tab))
            {
                ItemStack stack = createMolotovStack(item, 1, new DrinkInformation(DrinkRegistry.getDrinkID(drink), 1, compound));
                list.add(stack);
            }
        }
    }
}
