package org.jurassicraft.server.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.animation.Animations;

public abstract class AIAnimation extends EntityAIBase
{
    public AIAnimation(IAnimatedEntity entity)
    {
        animatedEntity = entity;
        setMutexBits(7);
    }

    public abstract Animation getAnimation();

    public <T extends EntityLiving> T getEntity()
    {
        return (T) animatedEntity;
    }

    public abstract boolean isAutomatic();

    public abstract int getDuration();

    public boolean shouldAnimate()
    {
        return false;
    }

    @Override
    public boolean shouldExecute()
    {
        if (isAutomatic())
        {
            return animatedEntity.getAnimation() == getAnimation();
        }
        return shouldAnimate();
    }

    @Override
    public void startExecuting()
    {
        if (!isAutomatic())
        {
            AnimationHandler.INSTANCE.sendAnimationMessage(animatedEntity, getAnimation());
        }
        animatedEntity.setAnimationTick(0);
    }

    @Override
    public boolean continueExecuting()
    {
        return animatedEntity.getAnimationTick() < getDuration();
    }

    @Override
    public void resetTask()
    {
        AnimationHandler.INSTANCE.sendAnimationMessage(animatedEntity, Animations.IDLE.get());
    }

    private final IAnimatedEntity animatedEntity;
}
