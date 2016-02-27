package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.animations.JCNonAutoAnimBase;
import org.jurassicraft.server.entity.ai.animations.JCNonAutoAnimSoundBase;
import org.jurassicraft.server.entity.base.AggressiveFlyingDinosaurEntity;

public class PteranodonEntity extends AggressiveFlyingDinosaurEntity // implements IEntityAIFlyingCreature, ICarnivore
{
    public PteranodonEntity(World world)
    {
        super(world);

        injuredSounds = new String[] { "pteranodon_hurt_1" };
        idleSounds = new String[] { "pteranodon_living_1" };
        dyingSounds = new String[] { "pteranodon_death_1" };
        callingsounds = new String[] { "pteranodon_call_1" };

        tasks.addTask(2, new JCNonAutoAnimBase(this, 25, Animations.LOOKING_RIGHT.get(), 100)); // Head twitch right
        tasks.addTask(2, new JCNonAutoAnimBase(this, 25, Animations.LOOKING_LEFT.get(), 100)); // Head twitch left
        tasks.addTask(2, new JCNonAutoAnimSoundBase(this, 34, Animations.CALLING.get(), 100, "jurassicraft:" + getCallingSound(), 2.5F)); // Call              
    }

    @Override
    public int getTailBoxCount()
    {
        return 0;
    }
}
