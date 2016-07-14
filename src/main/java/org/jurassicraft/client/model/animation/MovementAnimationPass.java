package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.Map;

public class MovementAnimationPass extends AnimationPass
{
    public MovementAnimationPass(Map<Animation, int[][]> poseSequences, AdvancedModelRenderer[][] poses, boolean useInertialTweens)
    {
        super(poseSequences, poses, useInertialTweens);
    }

    @Override
    protected boolean isEntityAnimationDependent()
    {
        return false;
    }

    @Override
    protected float getAnimationSpeed(DinosaurEntity entity)
    {
        return isMoving(entity) ? getAnimationDegree(entity) : 3.0F;
    }

    @Override
    protected float getAnimationDegree(DinosaurEntity entity)
    {
        return Math.min(2.0F, entity.isCarcass() ? 0.0F : (animation == DinosaurAnimation.WALKING.get() || animation == DinosaurAnimation.RUNNING.get() || animation == DinosaurAnimation.SWIMMING.get()) ? limbSwingAmount * ((entity.isInWater() || entity.isInLava()) ? 4.0F : 1.0F) : super.getAnimationDegree(entity));
    }

    @Override
    protected Animation getRequestedAnimation(DinosaurEntity entity)
    {
        return isMoving(entity) ? entity.isSwimming() ? DinosaurAnimation.SWIMMING.get() : limbSwingAmount > 1.0 ? DinosaurAnimation.RUNNING.get() : DinosaurAnimation.WALKING.get() : DinosaurAnimation.IDLE.get();
    }

    private boolean isMoving(DinosaurEntity entity)
    {
        double deltaX = entity.posX - entity.prevPosX;
        double deltaZ = entity.posZ - entity.prevPosZ;
        return deltaX * deltaX + deltaZ * deltaZ > 0;
    }
}