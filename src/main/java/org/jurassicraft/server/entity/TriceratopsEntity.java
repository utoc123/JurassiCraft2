package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.ProvokableDinosaurEntity;

public class TriceratopsEntity extends ProvokableDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    private static final String[] hurtSounds = new String[] { "triceratops_hurt_1" };
    private static final String[] livingSounds = new String[] { "triceratops_living_1", "triceratops_living_2", "triceratops_living_3" };
    private static final String[] deathSounds = new String[] { "triceratops_death_1" };

    public TriceratopsEntity(World world)
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
