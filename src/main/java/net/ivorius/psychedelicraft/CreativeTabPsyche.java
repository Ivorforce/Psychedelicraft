package net.ivorius.psychedelicraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabPsyche extends CreativeTabs
{
	public CreativeTabPsyche()
	{
		super(CreativeTabs.getNextID(), Psychedelicraft.MODID);
	}

	@Override
	public Item getTabIconItem()
	{
		return Psychedelicraft.itemCannabisLeaf;
	}
}