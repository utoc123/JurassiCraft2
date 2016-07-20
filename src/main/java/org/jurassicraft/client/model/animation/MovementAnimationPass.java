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
        float degree;

        if (animation == DinosaurAnimation.WALKING.get() || animation == DinosaurAnimation.RUNNING.get() || animation == DinosaurAnimation.SWIMMING.get())
        {
            if (entity.isInWater() || entity.isInLava())
            {
                degree = limbSwingAmount * 4.0F;
            }
            else
            {
                degree = limbSwingAmount * 1.0F;
            }
        }
        else
        {
            return super.getAnimationDegree(entity);
        }

        return Math.min(1.0F, degree);
    }

    @Override
    protected Animation getRequestedAnimation(DinosaurEntity entity)
    {
        if (entity.isCarcass())
        {
            return DinosaurAnimation.IDLE.get();
        }
        else
        {
            if (isMoving(entity))
            {
                if (entity.isSwimming())
                {
                    return DinosaurAnimation.SWIMMING.get();
                }
                else
                {
                    if (limbSwingAmount > 1.0)
                    {
                        return DinosaurAnimation.RUNNING.get();
                    }
                    else
                    {
                        return DinosaurAnimation.WALKING.get();
                    }
                }
            }
            else
            {
                return DinosaurAnimation.IDLE.get();
            }
        }
    }

    private boolean isMoving(DinosaurEntity entity)
    {
        double deltaX = entity.posX - entity.prevPosX;
        double deltaZ = entity.posZ - entity.prevPosZ;
        return deltaX * deltaX + deltaZ * deltaZ > 0;
    }

    @Override
    public boolean isLooping()
    {
        return true;
    }
}