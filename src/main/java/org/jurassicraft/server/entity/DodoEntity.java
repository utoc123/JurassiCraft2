package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.animations.JCAutoAnimBase;
import org.jurassicraft.server.entity.ai.metabolism.FindPlantEntityAI;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class DodoEntity extends DinosaurEntity // implements IEntityAICreature, IHerbivore
{
    private static final String[] hurtSounds = new String[] { "dodo_hurt_1", "dodo_hurt_2" };
    private static final String[] livingSounds = new String[] { "dodo_living_1", "dodo_living_2", "dodo_living_3" };
    private static final String[] deathSounds = new String[] { "dodo_death_1" };

    public DodoEntity(World world)
    {
        super(world);
        tasks.addTask(2, new JCAutoAnimBase(this, 18, Animations.EATING.get()));
        tasks.addTask(1, new FindPlantEntityAI(this));
    }

    @Override
    public int getTailBoxCount()
    {
        return 0;
    }

    @Override
    public String getLivingSound()
    {
        return randomSound(livingSounds);
    }

    @Override
    public String getHurtSound()
    {
        return randomSound(hurtSounds);
    }

    @Override
    public String getDeathSound()
    {
        return randomSound(deathSounds);
    }
}
