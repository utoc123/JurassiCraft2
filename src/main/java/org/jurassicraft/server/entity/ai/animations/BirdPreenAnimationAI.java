package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.Mutex;

public class BirdPreenAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public BirdPreenAnimationAI(IAnimatedEntity entity) {
        super();
        this.dinosaur = (DinosaurEntity) entity;
        this.setMutexBits(Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        return !this.dinosaur.isDead && !this.dinosaur.isSwimming() && !this.dinosaur.isCarcass() && this.dinosaur.getAttackTarget() == null && !this.dinosaur.isSwimming() && this.dinosaur.getRNG().nextDouble() < 0.005;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(DinosaurAnimation.PREENING.get());
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }
}
