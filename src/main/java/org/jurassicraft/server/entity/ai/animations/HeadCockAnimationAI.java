package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.Mutex;

public class HeadCockAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public HeadCockAnimationAI(IAnimatedEntity entity) {
        super();
        this.dinosaur = (DinosaurEntity) entity;
        this.setMutexBits(Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        return !this.dinosaur.isDead && !this.dinosaur.isSwimming() && !this.dinosaur.isCarcass() && this.dinosaur.getAttackTarget() == null && !this.dinosaur.isSwimming() && this.dinosaur.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(EntityAnimation.HEAD_COCKING.get());
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }
}
