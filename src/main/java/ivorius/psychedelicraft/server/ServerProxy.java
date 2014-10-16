/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.server;

import ivorius.psychedelicraft.PSCoreHandlerServer;
import ivorius.psychedelicraft.PSProxy;
import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.entities.DrugHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.config.Configuration;

public class ServerProxy implements PSProxy
{
    @Override
    public void preInit(Configuration config)
    {
        Psychedelicraft.coreHandlerServer = new PSCoreHandlerServer();
        Psychedelicraft.coreHandlerServer.register();
    }

    @Override
    public void registerRenderers()
    {

    }

    @Override
    public void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size)
    {

    }

    @Override
    public void createDrugRender(DrugHelper drugHelper)
    {

    }

    @Override
    public void loadConfig(String configID)
    {

    }
}
