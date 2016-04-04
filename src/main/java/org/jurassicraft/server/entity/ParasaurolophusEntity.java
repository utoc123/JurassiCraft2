package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.animations.JCNonAutoAnimBase;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;

public class ParasaurolophusEntity extends DefensiveDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public ParasaurolophusEntity(World world)
    {
        super(world);
        tasks.addTask(2, new JCNonAutoAnimBase(this, Animations.CALLING.get(), 100)); // Call
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
