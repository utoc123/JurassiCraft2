package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;

public class TherizinosaurusEntity extends DefensiveDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public TherizinosaurusEntity(World world)
    {
        super(world);
        this.addAIForAttackTargets(TyrannosaurusEntity.class, 1);
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
