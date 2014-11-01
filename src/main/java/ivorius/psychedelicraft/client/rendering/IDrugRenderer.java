/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import ivorius.psychedelicraft.entities.drugs.DrugHelper;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by lukas on 17.02.14.
 */
public interface IDrugRenderer
{
    public void update(DrugHelper drugHelper, EntityLivingBase entity);

    public void distortScreen(float par1, EntityLivingBase entity, int rendererUpdateCount, DrugHelper drugHelper);

    public void renderOverlaysAfterShaders(float par1, EntityLivingBase entity, int updateCounter, int width, int height, DrugHelper drugHelper);

    public void renderOverlaysBeforeShaders(float par1, EntityLivingBase entity, int updateCounter, int width, int height, DrugHelper drugHelper);

    public void renderAllHallucinations(float par1, DrugHelper drugHelper);

    public float getCurrentHeatDistortion();

    public float getCurrentWaterDistortion();

    public float getCurrentWaterScreenDistortion();
}
