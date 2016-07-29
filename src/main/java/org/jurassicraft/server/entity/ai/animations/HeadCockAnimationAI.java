package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.DinosaurEntity;

public class HeadCockAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public HeadCockAnimationAI(IAnimatedEntity entity) {
        super();
        this.dinosaur = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute() {
        return !this.dinosaur.isDead && !this.dinosaur.isSwimming() && !this.dinosaur.isCarcass() && this.dinosaur.getAttackTarget() == null && !this.dinosaur.isSwimming() && this.dinosaur.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(DinosaurAnimation.HEAD_COCKING.get());
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }
}
