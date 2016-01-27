package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;

public class GallimimusEntity extends DefensiveDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    private static final String[] hurtSounds = new String[] { "gallimimus_hurt_1", "gallimimus_hurt_2" };
    private static final String[] livingSounds = new String[] { "gallimimus_living_1", "gallimimus_living_2" };
    private static final String[] deathSounds = new String[] { "gallimimus_death_1", "gallimimus_death_2" };

    public GallimimusEntity(World world)
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
