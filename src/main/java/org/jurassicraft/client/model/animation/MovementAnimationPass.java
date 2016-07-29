package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import org.jurassicraft.server.entity.DinosaurEntity;

import java.util.Map;

public class MovementAnimationPass extends AnimationPass {
    public MovementAnimationPass(Map<Animation, float[][]> poseSequences, PosedCuboid[][] poses, boolean useInertialTweens) {
        super(poseSequences, poses, useInertialTweens);
    }

    @Override
    protected boolean isEntityAnimationDependent() {
        return false;
    }

    @Override
    protected float getAnimationSpeed(DinosaurEntity entity) {
        return this.isMoving(entity) ? this.getAnimationDegree(entity) : 3.0F;
    }

    @Override
    protected float getAnimationDegree(DinosaurEntity entity) {
        float degree;

        if (this.animation == DinosaurAnimation.WALKING.get() || this.animation == DinosaurAnimation.RUNNING.get() || this.animation == DinosaurAnimation.SWIMMING.get()) {
            if (entity.isInWater() || entity.isInLava()) {
                degree = this.limbSwingAmount * 4.0F;
            } else {
                degree = this.limbSwingAmount;
            }
        } else {
            return super.getAnimationDegree(entity);
        }

        return Math.max(this.isMoving(entity) ? 0.5F : 0.0F, Math.min(1.0F, degree));
    }

    @Override
    protected Animation getRequestedAnimation(DinosaurEntity entity) {
        if (entity.isCarcass()) {
            return DinosaurAnimation.IDLE.get();
        } else {
            if (this.isMoving(entity)) {
                if (entity.isSwimming()) {
                    return DinosaurAnimation.SWIMMING.get();
                } else {
                    if (entity.isRunning()) {
                        return DinosaurAnimation.RUNNING.get();
                    } else {
                        return DinosaurAnimation.WALKING.get();
                    }
                }
            } else {
                return DinosaurAnimation.IDLE.get();
            }
        }
    }

    private boolean isMoving(DinosaurEntity entity) {
        float deltaX = (float) (entity.posX - entity.prevPosX);
        float deltaZ = (float) (entity.posZ - entity.prevPosZ);
        return deltaX * deltaX + deltaZ * deltaZ > 0.001F;
    }

    @Override
    public boolean isLooping() {
        return true;
    }
}