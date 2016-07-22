package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class RoarAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public RoarAnimationAI(IAnimatedEntity entity) {
        super();
        this.dinosaur = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute() {
        return !this.dinosaur.isDead && this.dinosaur.getAttackTarget() == null && this.dinosaur.getAgePercentage() > 75 && !this.dinosaur.isSleeping() && this.dinosaur.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(DinosaurAnimation.ROARING.get());
        this.dinosaur.playSound(this.dinosaur.getSoundForAnimation(DinosaurAnimation.ROARING.get()), this.dinosaur.getSoundVolume() > 0.0F ? this.dinosaur.getSoundVolume() + 1.25F : 0.0F, this.dinosaur.getSoundPitch());
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }
}
