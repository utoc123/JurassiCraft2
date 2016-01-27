package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveFlyingDinosaurEntity;

public class TropeognathusEntity extends AggressiveFlyingDinosaurEntity // implements IEntityAIFlyingCreature, ICarnivore
{
    public TropeognathusEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 0;
    }
}
