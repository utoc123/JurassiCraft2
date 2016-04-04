package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.animations.JCAutoAnimBase;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class CompsognathusEntity extends AggressiveDinosaurEntity // implements IEntityAICreature, ICarnivore
{
    public CompsognathusEntity(World world)
    {
        super(world);

        animationTasks.addTask(2, new JCAutoAnimBase(this, Animations.BEGGING.get())); // Beg
    }
}
