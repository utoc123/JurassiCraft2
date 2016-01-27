package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveSwimmingDinosaurEntity;

import java.util.Random;

public class TylosaurusEntity extends AggressiveSwimmingDinosaurEntity // implements IEntityAISwimmingCreature, ICarnivore
{
    private static final Class[] targets = { CoelacanthEntity.class, MegapiranhaEntity.class };

    public TylosaurusEntity(World world)
    {
        super(world);

        for (int i = 0; i < targets.length; i++)
        {
            this.addAIForAttackTargets(targets[i], new Random().nextInt(3) + 1);
        }
    }

    @Override
    public int getTailBoxCount()
    {
        return 9;
    }
}
