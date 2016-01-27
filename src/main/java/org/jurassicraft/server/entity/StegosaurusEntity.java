package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.ProvokableDinosaurEntity;

public class StegosaurusEntity extends ProvokableDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    private static final String[] hurtSounds = new String[] { "stegosaurus_hurt_1", "stegosaurus_hurt_2" };
    private static final String[] livingSounds = new String[] { "stegosaurus_living_1", "stegosaurus_living_2" };
    private static final String[] deathSounds = new String[] { "stegosaurus_death_1", "stegosaurus_death_2" };

    public StegosaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }

    public String getLivingSound()
    {
        return randomSound(livingSounds);
    }

    public String getHurtSound()
    {
        return randomSound(hurtSounds);
    }

    public String getDeathSound()
    {
        return randomSound(deathSounds);
    }
}
