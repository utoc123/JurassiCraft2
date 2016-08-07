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
import org.jurassicraft.server.entity.ai.LeapingMeleeEntityAI;
import org.jurassicraft.server.entity.ai.RaptorLeapEntityAI;
import org.jurassicraft.server.entity.ai.animations.BirdPreenAnimationAI;
import org.jurassicraft.server.entity.ai.animations.HeadCockAnimationAI;

public class MicroraptorEntity extends DinosaurEntity {
    public MicroraptorEntity(World world) {
        super(world);
        this.target(EntityPlayer.class, EntityAnimal.class, EntityVillager.class, DilophosaurusEntity.class, GallimimusEntity.class, ParasaurolophusEntity.class, TriceratopsEntity.class, VelociraptorEntity.class);
        this.tasks.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.animationTasks.addTask(3, new BirdPreenAnimationAI(this));
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
                return SoundHandler.MICRORAPTOR_LIVING;
            case DYING:
                return SoundHandler.MICRORAPTOR_DEATH;
            case INJURED:
                return SoundHandler.MICRORAPTOR_HURT;
            case ATTACKING:
                return SoundHandler.MICRORAPTOR_ATTACK;
            case CALLING:
                return SoundHandler.MICRORAPTOR_LIVING;
        }

        return null;
    }
}
