/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.client.rendering;

import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by lukas on 17.02.14.
 */
public interface IDrugRenderer
{
    public void update(DrugProperties drugProperties, EntityLivingBase entity);

    public void distortScreen(float par1, EntityLivingBase entity, int rendererUpdateCount, DrugProperties drugProperties);

    public void renderOverlaysAfterShaders(float par1, EntityLivingBase entity, int updateCounter, int width, int height, DrugProperties drugProperties);

    public void renderOverlaysBeforeShaders(float par1, EntityLivingBase entity, int updateCounter, int width, int height, DrugProperties drugProperties);

    public void renderAllHallucinations(float par1, DrugProperties drugProperties);

    public float getCurrentHeatDistortion();

    public float getCurrentWaterDistortion();

    public float getCurrentWaterScreenDistortion();
}
