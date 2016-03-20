package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.animations.JCAutoAnimBase;
import org.jurassicraft.server.entity.ai.metabolism.FindPlantEntityAI;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class DodoEntity extends DinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public DodoEntity(World world)
    {
        super(world);

        injuredSounds = new String[] { "dodo_hurt_1", "dodo_hurt_2" };
        idleSounds = new String[] { "dodo_living_1", "dodo_living_2", "dodo_living_3" };
        dyingSounds = new String[] { "dodo_death_1" };

        tasks.addTask(2, new JCAutoAnimBase(this, 18, Animations.EATING.get()));
        tasks.addTask(1, new FindPlantEntityAI(this));
    }

    @Override
    public int getTailBoxCount()
    {
        return 0;
    }
}
