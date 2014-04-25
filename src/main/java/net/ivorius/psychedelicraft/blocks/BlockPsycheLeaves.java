package net.ivorius.psychedelicraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ivorius.psychedelicraft.Psychedelicraft;
import net.ivorius.psychedelicraft.items.PSItems;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;


public class BlockPsycheLeaves extends BlockLeaves
{
    public static final String[][] field_150130_N = new String[][]{{Psychedelicraft.textureBase + "psycheLeaves_juniper_opaque", Psychedelicraft.textureBase + "psycheLeaves_juniperBerries_opaque"}};
    public static final String[] field_150131_O = new String[]{Psychedelicraft.textureBase + "psycheLeaves_juniper", Psychedelicraft.textureBase + "psycheLeaves_juniperBerries"};

    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_)
    {
        return (p_149741_1_ & 3) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((p_149741_1_ & 3) == 2 ? ColorizerFoliage.getFoliageColorBirch() : super.getRenderColor(p_149741_1_));
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        int l = p_149720_1_.getBlockMetadata(p_149720_2_, p_149720_3_, p_149720_4_);
        return super.colorMultiplier(p_149720_1_, p_149720_2_, p_149720_3_, p_149720_4_);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return (p_149691_2_ & 3) == 1 ? this.field_150129_M[this.field_150127_b][1] : ((p_149691_2_ & 3) == 3 ? this.field_150129_M[this.field_150127_b][3] : ((p_149691_2_ & 3) == 2 ? this.field_150129_M[this.field_150127_b][2] : this.field_150129_M[this.field_150127_b][0]));
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        for (int i = 0; i < field_150130_N.length; ++i)
        {
            this.field_150129_M[i] = new IIcon[field_150130_N[i].length];

            for (int j = 0; j < field_150130_N[i].length; ++j)
            {
                this.field_150129_M[i][j] = p_149651_1_.registerIcon(field_150130_N[i][j]);
            }
        }
    }

    public String[] func_150125_e()
    {
        return field_150131_O;
    }

    // ----------------


    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random p_149674_5_)
    {
        if (!par1World.isRemote)
        {
            int m = par1World.getBlockMetadata(par2, par3, par4);
            if ((m & 3) == 0 && par1World.rand.nextFloat() < 0.01f)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, m + 1, 3);
            }
        }

        super.updateTick(par1World, par2, par3, par4, p_149674_5_);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if ((par1World.getBlockMetadata(par2, par3, par4) & 3) == 1)
        {
            if (!par1World.isRemote)
            {
                dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(PSItems.itemJuniperBerries));
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4) - 1, 3);

            return true;
        }

        return false;
    }
}