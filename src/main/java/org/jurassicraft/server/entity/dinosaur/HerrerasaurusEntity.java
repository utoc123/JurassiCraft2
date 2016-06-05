package org.jurassicraft.server.entity.dinosaur;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class HerrerasaurusEntity extends AggressiveDinosaurEntity
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
