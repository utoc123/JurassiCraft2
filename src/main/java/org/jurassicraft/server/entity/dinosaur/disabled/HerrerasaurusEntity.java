package org.jurassicraft.server.entity.dinosaur.disabled;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class HerrerasaurusEntity extends DinosaurEntity
{
    public HerrerasaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public float getSoundVolume()
    {
        return (float) transitionFromAge(1.3F, 2.0F);
    }
}
