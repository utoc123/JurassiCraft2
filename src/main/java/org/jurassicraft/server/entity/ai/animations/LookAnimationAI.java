package org.jurassicraft.server.entity.ai.animations;

import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class LookAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public LookAnimationAI(DinosaurEntity dinosaur) {
        super();
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute() {
        return !this.dinosaur.isDead && this.dinosaur.getAttackTarget() == null && !this.dinosaur.isSleeping() && !this.dinosaur.isSwimming() && this.dinosaur.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(this.dinosaur.getRNG().nextBoolean() ? DinosaurAnimation.LOOKING_LEFT.get() : DinosaurAnimation.LOOKING_RIGHT.get());
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }
}