package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveSwimmingDinosaurEntity;

public class TylosaurusEntity extends AggressiveSwimmingDinosaurEntity
{
    public TylosaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 9;
    }
}
