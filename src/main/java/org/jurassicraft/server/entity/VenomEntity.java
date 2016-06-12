package org.jurassicraft.server.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.dinosaur.DilophosaurusEntity;
import org.jurassicraft.server.entity.particle.VenomParticle;

public class VenomEntity extends EntityThrowable
{
    public VenomEntity(World world)
    {
        super(world);

        if (world.isRemote)
        {
            spawnParticles();
        }
    }

    public VenomEntity(World world, DilophosaurusEntity entity)
    {
        super(world, entity);

        if (world.isRemote)
        {
            spawnParticles();
        }
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        EntityLivingBase thrower = getThrower();

        if (thrower instanceof DilophosaurusEntity)
        {
            DilophosaurusEntity spitter = (DilophosaurusEntity) thrower;

            if (result.entityHit != null && result.entityHit instanceof EntityLivingBase && result.entityHit != spitter && (result.entityHit == spitter.getAttackTarget() || !(result.entityHit instanceof DilophosaurusEntity)))
            {
                EntityLivingBase entityHit = (EntityLivingBase) result.entityHit;

                entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 4.0F);
                entityHit.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("blindness"), 300, 1, false, false));

                if (!this.worldObj.isRemote)
                {
                    this.setDead();
                }
            }
        }
    }

    private void spawnParticles()
    {
        ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;

        float size = 0.35F;

        for (int i = 0; i < 16; ++i)
        {
            particleManager.addEffect(new VenomParticle(worldObj, size * Math.random() - size / 2, size * Math.random() - size / 2, size * Math.random() - size / 2, 0.0F, 0.0F, 0.0F, 1.0F, this));
        }
    }
}
