package net.ivorius.psychedelicraft.items;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemWoodenMug extends Item
{
    public IIcon filledIcons[] = new IIcon[3];
    public String filledNames[] = new String[3];
    public DrugInfluence drugInfluences[][] = new DrugInfluence[3][];

    public ItemWoodenMug()
    {
        super();

        setHasSubtypes(true);
        setMaxStackSize(16);

        filledNames[0] = "beer";
        drugInfluences[0] = new DrugInfluence[]{new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.06f)};

        filledNames[1] = "jenever";
        drugInfluences[1] = new DrugInfluence[]{new DrugInfluence("Alcohol", 20, 0.002, 0.001, 0.25f)};

        filledNames[2] = "warmCoffee";
        drugInfluences[2] = new DrugInfluence[]{new DrugInfluence("Caffeine", 20, 0.002, 0.001, 0.3f), new DrugInfluence("Warmth", 0, 0.00, 0.1, 0.8f)};
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        int dmg = par1ItemStack.getItemDamage();

        if (dmg - 1 >= 0 && dmg - 1 < drugInfluences.length)
        {
            for (DrugInfluence influence : drugInfluences[dmg - 1])
            {
                DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(influence.clone());
            }

            if (dmg - 1 == 2) //Coffee
            {
                if (par3EntityPlayer.getFoodStats().needFood())
                {
                    par3EntityPlayer.getFoodStats().addStats(1, 0.1f);
                }
            }
        }

        par1ItemStack.stackSize--;

        par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(this, 1, 0));

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.getItemDamage() > 0)
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);

        filledIcons[0] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "woodenMugBeer");
        filledIcons[1] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "woodenMugJenever");
        filledIcons[2] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "woodenMugCoffee");
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        if (filledIcons.length > par1 - 1 && par1 > 0)
        {
            return filledIcons[par1 - 1];
        }

        return super.getIconFromDamage(par1);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (filledNames.length > par1ItemStack.getItemDamage() - 1 && par1ItemStack.getItemDamage() > 0)
        {
            return super.getUnlocalizedName(par1ItemStack) + "." + filledNames[par1ItemStack.getItemDamage() - 1];
        }

        return super.getUnlocalizedName(par1ItemStack);
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        super.getSubItems(par1, par2CreativeTabs, par3List);

        for (int i = 0; i < filledNames.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, 1 + i));
        }
    }
}
