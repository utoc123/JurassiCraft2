package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.RaptorLeapEntityAI;
import org.jurassicraft.server.entity.ai.LeapingMeleeEntityAI;

public class VelociraptorEntity extends DinosaurEntity {
    public VelociraptorEntity(World world) {
        super(world);
        this.target(EntityPlayer.class, EntityAnimal.class, EntityVillager.class, DilophosaurusEntity.class, GallimimusEntity.class, ParasaurolophusEntity.class, TriceratopsEntity.class, MicroraptorEntity.class);
        this.tasks.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
    }

    @Override
    public EntityAIBase getAttackAI() {
        return new RaptorLeapEntityAI(this);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (this.getAnimation() != DinosaurAnimation.RAPTOR_LAND.get()) {
            super.fall(distance, damageMultiplier);
        }
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (DinosaurAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.VELOCIRAPTOR_LIVING;
            case DYING:
                return SoundHandler.VELOCIRAPTOR_DEATH;
            case INJURED:
                return SoundHandler.VELOCIRAPTOR_HURT;
            case CALLING:
                return SoundHandler.VELOCIRAPTOR_CALL;
            case ATTACKING:
                return SoundHandler.VELOCIRAPTOR_ATTACK;
        }

        return null;
    }

    @Override
    public SoundEvent getBreathingSound() {
        return SoundHandler.VELOCIRAPTOR_BREATHING;
    }
}
