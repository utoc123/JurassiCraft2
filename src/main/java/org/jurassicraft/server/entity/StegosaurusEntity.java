package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.ProvokableDinosaurEntity;

public class StegosaurusEntity extends ProvokableDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public StegosaurusEntity(World world)
    {
        super(world);

        injuredSounds = new String[] { "stegosaurus_hurt_1", "stegosaurus_hurt_2" };
        idleSounds = new String[] { "stegosaurus_living_1", "stegosaurus_living_2" };
        dyingSounds = new String[] { "stegosaurus_death_1", "stegosaurus_death_2" };
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
