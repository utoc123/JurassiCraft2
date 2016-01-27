package org.jurassicraft.server.entity.base;

import net.minecraft.world.World;

public abstract class FlyingDinosaurEntity extends DinosaurEntity
{
    public FlyingDinosaurEntity(World world)
    {
        super(world);
    }

    public void fall(float f, float damageMultiplier)
    {
        // TODO slow itself down when landing, if falling too fast, take damage
    }
}
