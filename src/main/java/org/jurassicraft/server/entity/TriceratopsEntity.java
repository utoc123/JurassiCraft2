package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.ProvokableDinosaurEntity;

public class TriceratopsEntity extends ProvokableDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    public TriceratopsEntity(World world)
    {
        super(world);
        
        hurtSounds = new String[] { "triceratops_hurt_1" };
        idleSounds = new String[] { "triceratops_living_1", "triceratops_living_2", "triceratops_living_3" };
        deathSounds = new String[] { "triceratops_death_1" };
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
