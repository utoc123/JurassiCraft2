package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.ProvokableDinosaurEntity;

public class AnkylosaurusEntity extends ProvokableDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public AnkylosaurusEntity(World world)
    {
        super(world);

        injuredSounds = new String[] { "ankylosaurus_hurt_1", "ankylosaurus_hurt_2" };
        dyingSounds = new String[] { "ankylosaurus_hurt_1", "ankylosaurus_hurt_2" };
        idleSounds = new String[] { "ankylosaurus_living_1", "ankylosaurus_living_2", "ankylosaurus_living_3", "ankylosaurus_living_4" };
}

    @Override
    public int getTailBoxCount()
    {
        return 5;
    }
}
