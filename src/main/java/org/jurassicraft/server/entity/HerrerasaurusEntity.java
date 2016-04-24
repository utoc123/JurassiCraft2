package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class HerrerasaurusEntity extends AggressiveDinosaurEntity
{
    public HerrerasaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }

    @Override
    public float getSoundVolume()
    {
        return (float) transitionFromAge(1.3F, 2.0F);
    }
}
