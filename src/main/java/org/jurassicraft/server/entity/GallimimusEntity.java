package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;

public class GallimimusEntity extends DefensiveDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public GallimimusEntity(World world)
    {
        super(world);

        injuredSounds = new String[] { "gallimimus_hurt_1", "gallimimus_hurt_2" };
        idleSounds = new String[] { "gallimimus_living_1", "gallimimus_living_2" };
        dyingSounds = new String[] { "gallimimus_death_1", "gallimimus_death_2" };
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
