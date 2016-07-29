package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.Herd;

public class PeckGroundAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public PeckGroundAnimationAI(IAnimatedEntity entity) {
        super();
        this.dinosaur = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute() {
        return !(this.dinosaur.isDead || this.dinosaur.getAttackTarget() != null || this.dinosaur.isSleeping() || (this.dinosaur.herd != null && this.dinosaur.herd.state == Herd.State.MOVING) || this.dinosaur.getAnimation() != DinosaurAnimation.IDLE.get()) && !this.dinosaur.isSwimming() && this.dinosaur.getRNG().nextDouble() < 0.01;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(DinosaurAnimation.PECKING.get());
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }
}
