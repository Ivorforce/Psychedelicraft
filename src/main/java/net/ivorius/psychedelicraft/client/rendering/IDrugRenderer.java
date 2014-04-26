/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.client.rendering;

import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by lukas on 17.02.14.
 */
public interface IDrugRenderer
{
    public void update(DrugHelper drugHelper, EntityLivingBase entity);

    public void distortScreen(float par1, EntityLivingBase entity, int rendererUpdateCount, DrugHelper drugHelper);

    public void renderOverlays(float par1, EntityLivingBase entity, int updateCounter, int width, int height, DrugHelper drugHelper);

    public void renderAllHallucinations(float par1, DrugHelper drugHelper);

    public float getCurrentHeatDistortion();

    public float getCurrentWaterDistortion();

    public float getCurrentWaterScreenDistortion();
}
