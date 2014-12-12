/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.entities.drugs.DrugInfluence;
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
        DrugProperties drugProperties = DrugProperties.getDrugProperties(par3EntityPlayer);
        if (drugProperties != null)
            drugProperties.addToDrug(new DrugInfluence("Cannabis", 60, 0.004, 0.002, 0.7f));
    }
}
