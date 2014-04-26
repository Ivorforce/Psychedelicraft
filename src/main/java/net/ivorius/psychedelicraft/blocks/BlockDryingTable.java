/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.blocks;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockDryingTable extends BlockContainer
{
    public IIcon bottomIcon;
    public IIcon topIcon;

    public BlockDryingTable()
    {
        super(Material.wood);

        setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
        this.setLightOpacity(0);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);

        bottomIcon = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "dryingTableBottom");
        topIcon = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "dryingTableTop");
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        if (par1 == 0)
        {
            return bottomIcon;
        }
        if (par1 == 1)
        {
            return topIcon;
        }

        return super.getIcon(par1, par2);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (!par1World.isRemote)
        {
            par5EntityPlayer.openGui(Psychedelicraft.instance, Psychedelicraft.dryingTableContainerID, par1World, par2, par3, par4);
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityDryingTable();
    }
}
