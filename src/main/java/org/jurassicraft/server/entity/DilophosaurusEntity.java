package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class DilophosaurusEntity extends AggressiveDinosaurEntity // implements IEntityAICreature, ICarnivore
{
    public DilophosaurusEntity(World world)
    {
        super(world);

        injuredSounds = new String[] { "dilophosaurus_hurt_1", "dilophosaurus_hurt_2" };
        idleSounds = new String[] { "dilophosaurus_living_1", "dilophosaurus_living_2", "dilophosaurus_living_3" };
        dyingSounds = new String[] { "dilophosaurus_death_1" };
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
