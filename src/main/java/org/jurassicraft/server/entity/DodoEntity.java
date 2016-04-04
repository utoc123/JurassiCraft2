package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.animations.JCAutoAnimBase;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class DodoEntity extends DinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public DodoEntity(World world)
    {
        super(world);

        animationTasks.addTask(2, new JCAutoAnimBase(this, Animations.EATING.get()));
    }
}
