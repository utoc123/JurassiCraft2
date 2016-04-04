package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.animations.JCNonAutoAnimBase;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;

public class HypsilophodonEntity extends DefensiveDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public HypsilophodonEntity(World world)
    {
        super(world);

        animationTasks.addTask(2, new JCNonAutoAnimBase(this, Animations.SCRATCHING.get(), 60)); // Scratch Animation
    }
}
