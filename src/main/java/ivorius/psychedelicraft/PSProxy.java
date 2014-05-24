/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft;

import ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by lukas on 24.05.14.
 */
public interface PSProxy
{
    void preInit(Configuration config);

    void registerRenderers();

    void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size);

    void createDrugRender(DrugHelper drugHelper);
}
