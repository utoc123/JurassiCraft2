package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveFlyingDinosaurEntity;

public class DimorphodonEntity extends AggressiveFlyingDinosaurEntity
{
    public DimorphodonEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 0;
    }
}
