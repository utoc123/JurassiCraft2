package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.ProvokableDinosaurEntity;

public class AnkylosaurusEntity extends ProvokableDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    private static final String[] hurtSounds = new String[] { "ankylosaurus_hurt_1", "ankylosaurus_hurt_2" };
    private static final String[] livingSounds = new String[] { "ankylosaurus_living_1", "ankylosaurus_living_2", "ankylosaurus_living_3", "ankylosaurus_living_4" };

    public AnkylosaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 5;
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
        return randomSound(hurtSounds);
    }
}
