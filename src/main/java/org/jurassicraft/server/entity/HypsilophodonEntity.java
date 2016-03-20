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

        injuredSounds = new String[] { "hypsilophodon_hurt_1", "hypsilophodon_hurt_2" };
        dyingSounds = new String[] { "hypsilophodon_hurt_1", "hypsilophodon_hurt_2" };
        idleSounds = new String[] { "hypsilophodon_living_1", "hypsilophodon_living_2", "hypsilophodon_living_3", "hypsilophodon_living_4" };

        tasks.addTask(2, new JCNonAutoAnimBase(this, 35, Animations.SCRATCHING.get(), 60)); // Scratch Animation
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
