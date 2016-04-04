package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;

public class LeaellynasauraEntity extends DefensiveDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public LeaellynasauraEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
