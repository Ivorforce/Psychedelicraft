/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Only needed for the Block constructor
 */
public class ItemLeavesForge extends ItemBlock
{
    private final BlockLeaves field_150940_b;

    public ItemLeavesForge(Block p_i45344_1_)
    {
        super(p_i45344_1_);
        this.field_150940_b = (BlockLeaves) p_i45344_1_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_ | 4;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        int i = p_77667_1_.getItemDamage();

        if (i < 0 || i >= this.field_150940_b.func_150125_e().length)
        {
            i = 0;
        }

        return super.getUnlocalizedName() + "." + this.field_150940_b.func_150125_e()[i];
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150940_b.getIcon(0, p_77617_1_);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        return this.field_150940_b.getRenderColor(p_82790_1_.getItemDamage());
    }
}
