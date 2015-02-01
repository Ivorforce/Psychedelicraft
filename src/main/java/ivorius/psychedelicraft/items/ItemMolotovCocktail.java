/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.EntityMolotovCocktail;
import ivorius.psychedelicraft.fluids.ExplodingFluid;
import ivorius.psychedelicraft.fluids.FluidHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

public class ItemMolotovCocktail extends ItemBottle
{
    public static FluidStack getExplodingFluid(ItemStack stack)
    {
        if (stack.getItem() instanceof IFluidContainerItem)
        {
            FluidStack fluidStack = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
            if (fluidStack != null && fluidStack.getFluid() instanceof ExplodingFluid)
                return fluidStack;
        }

        return null;
    }

    public ItemMolotovCocktail(int capacity)
    {
        super(capacity);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeUsed)
    {
        stack.stackSize--;
        if (stack.stackSize <= 0)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            float partUsed = MathHelper.clamp_float((float) (getMaxItemUseDuration(stack) - timeUsed) / 30.0f, 0.0f, 1.0f);

            EntityMolotovCocktail molotovCocktail = new EntityMolotovCocktail(world, player);

            molotovCocktail.molotovStack = stack.copy();
            molotovCocktail.molotovStack.stackSize = 1;

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
        FluidStack fluidStack = getFluid(stack);

        if (fluidStack != null)
        {
            int quality = 0;

            if (fluidStack.getFluid() instanceof ExplodingFluid)
            {
                ExplodingFluid explodingFluid = (ExplodingFluid) fluidStack.getFluid();
                float explStr = explodingFluid.explosionStrength(fluidStack) * 0.8f;
                float fireStr = explodingFluid.fireStrength(fluidStack) * 0.6f;

                quality = MathHelper.clamp_int(MathHelper.floor_float((fireStr + explStr) + 0.5f), 0, 7);
            }

            return StatCollector.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".quality" + quality + ".name");
        }

        return StatCollector.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".empty.name");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean aBool)
    {
        super.addInformation(stack, player, list, aBool);

        FluidStack fluidStack = getFluid(stack);

        if (fluidStack != null)
            list.add(fluidStack.getLocalizedName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int dmg = 0; dmg < 16; dmg++)
        {
            list.add(new ItemStack(item, 1, dmg));

            for (FluidStack fluidStack : FluidHelper.allFluids(ExplodingFluid.SUBTYPE, capacity))
            {
                ItemStack stack = new ItemStack(item, 1, dmg);
                fill(stack, fluidStack, true);
                list.add(stack);
            }
        }
    }
}
