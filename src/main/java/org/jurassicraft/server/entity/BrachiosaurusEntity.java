package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;

public class BrachiosaurusEntity extends DefensiveDinosaurEntity // implements IEntityAICreature, IHerbivore
{
    private int stepCount = 0;

    public BrachiosaurusEntity(World world)
    {
        super(world);
        
        idleSounds = new String[] { "brachiosaurus_living_1", "brachiosaurus_living_2", "brachiosaurus_living_3", "brachiosaurus_living_4" };
        hurtSounds = new String[] { "brachiosaurus_hurt_1", "brachiosaurus_hurt_2" };
        deathSounds = new String[] { "brachiosaurus_death_1", "brachiosaurus_death_2" };
    }

    @Override
    public int getTailBoxCount()
    {
        return 5;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        /** Step Sound */
        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound("jurassicraft:stomp", (float) transitionFromAge(0.1F, 1.0F), this.getSoundPitch());
            stepCount = 50;
        }

        this.stepCount -= this.moveForward * 9.5;
    }
}
