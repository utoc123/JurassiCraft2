package org.jurassicraft.server.entity.particle;

import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class HurtParticle extends ParticleDigging
{
    public HurtParticle(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
    {
        super(world, x, y, z, motionX, motionY, motionZ, Blocks.REDSTONE_BLOCK.getDefaultState());
        this.particleMaxAge += 550;
    }
}
