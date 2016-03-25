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

        tasks.addTask(2, new JCNonAutoAnimBase(this, 35, Animations.SCRATCHING.get(), 60)); // Scratch Animation
    }
}
