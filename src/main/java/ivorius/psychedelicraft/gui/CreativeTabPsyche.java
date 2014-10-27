/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by lukas on 25.04.14.
 */
public class CreativeTabPsyche extends CreativeTabs
{
    public Item tabIcon;

    public CreativeTabPsyche(String par2Str)
    {
        super(par2Str);
    }

    @Override
    public Item getTabIconItem()
    {
        return tabIcon;
    }
}
