package net.ivorius.psychedelicraft.items;

import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.ivorius.psychedelicraft.entities.DrugInfluence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHashMuffin extends ItemFood
{
    public ItemHashMuffin()
    {
        super(2, 0.1f, false);
    }

    @Override
    protected void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        DrugHelper.getDrugHelper(par3EntityPlayer).addToDrug(new DrugInfluence("Cannabis", 60, 0.004, 0.002, 0.7f));
    }
}
