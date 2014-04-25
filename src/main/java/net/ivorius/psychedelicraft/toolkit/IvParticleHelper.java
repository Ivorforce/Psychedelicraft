/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;

public class IvParticleHelper
{
    public static void spawnParticle(EntityFX particle)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }
}
