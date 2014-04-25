/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package net.ivorius.psychedelicraft;

import net.ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.config.Configuration;

public class ServerProxy
{
    public void preInit(Configuration config)
    {
        Psychedelicraft.coreHandlerServer = new PSCoreHandlerServer();
        Psychedelicraft.coreHandlerServer.register();
    }

    public void registerRenderers()
    {

    }

    public void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size)
    {

    }

    public void createDrugRender(DrugHelper drugHelper)
    {

    }
}
