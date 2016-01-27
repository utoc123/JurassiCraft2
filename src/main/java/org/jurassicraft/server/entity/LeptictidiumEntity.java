package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;

public class LeptictidiumEntity extends DefensiveDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public LeptictidiumEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 0;
    }
}
