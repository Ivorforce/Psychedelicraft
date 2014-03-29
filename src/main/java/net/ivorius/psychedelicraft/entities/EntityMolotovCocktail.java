package net.ivorius.psychedelicraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityMolotovCocktail extends EntityThrowable
{
    public int fireStrength;

    public EntityMolotovCocktail(World par1World)
    {
        super(par1World);
    }

    public EntityMolotovCocktail(World par1World, EntityLivingBase par2EntityLiving)
    {
        super(par1World, par2EntityLiving);
    }

    public EntityMolotovCocktail(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();

        worldObj.spawnParticle("flame", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        worldObj.spawnParticle("flame", posX + motionX / 2, posY + motionY / 2, posZ + motionZ / 2, 0.0D, 0.0D, 0.0D);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        worldObj.playSoundAtEntity(this, "game.potion.smash", 1.0F, 1.0F);

        if (par1MovingObjectPosition.entityHit != null)
        {
            if (!par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 4))
            {

            }
        }

        if (fireStrength > 2)
        {
            for (int i = 0; i < fireStrength * 2; i++)
            {
                worldObj.spawnParticle("flame", posX + (rand.nextDouble() - 0.5D) * fireStrength, posY + (rand.nextDouble() - 0.5D) * fireStrength, posZ + (rand.nextDouble() - 0.5D) * fireStrength, 0.0D, 0.0D, 0.0D);

                worldObj.spawnParticle("lava", posX, posY + height, posZ, 0.0D, 0.0D, 0.0D);
            }

            if (!worldObj.isRemote)
            {
                for (int x = -fireStrength / 2; x < fireStrength / 2; x++)
                {
                    for (int y = -fireStrength / 2; y < fireStrength / 2; y++)
                    {
                        for (int z = -fireStrength / 2; z < fireStrength / 2; z++)
                        {
                            if (x * x + y * y + z * z < (fireStrength / 2) * (fireStrength / 2))
                            {
                                if (worldObj.getBlock((int) posX + x, (int) posY + y, (int) posZ + z) == Blocks.air && worldObj.getBlock((int) posX + x, (int) posY + y - 1, (int) posZ + z).isNormalCube() && rand.nextInt(2) == 0)
                                {
                                    worldObj.setBlock((int) posX + x, (int) posY + y, (int) posZ + z, Blocks.fire, 0, 3);
                                }
                            }
                        }
                    }
                }

                if (fireStrength >= 6)
                {
                    worldObj.createExplosion(this, posX, posY, posZ, fireStrength / 20.0F, true);
                }
            }
        }

        setDead();
    }
}
