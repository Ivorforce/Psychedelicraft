/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.blocks;

import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockPsycheLog extends BlockLog
{
    public static final String[] woodType = new String[]{"juniper"};

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.field_150167_a = new IIcon[woodType.length];
        this.field_150166_b = new IIcon[woodType.length];

        for (int i = 0; i < this.field_150167_a.length; ++i)
        {
            this.field_150167_a[i] = par1IconRegister.registerIcon(this.getTextureName() + "_" + woodType[i]);
            this.field_150166_b[i] = par1IconRegister.registerIcon(this.getTextureName() + "_" + woodType[i] + "_top");
        }
    }
}
