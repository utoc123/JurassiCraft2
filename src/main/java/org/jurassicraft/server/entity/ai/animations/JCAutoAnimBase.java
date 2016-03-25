package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import org.jurassicraft.server.animation.AIAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class JCAutoAnimBase extends AIAnimation
{
    protected DinosaurEntity animatingEntity;
    protected int duration;
    protected Animation animation;

    public JCAutoAnimBase(IAnimatedEntity entity, int duration, Animation animation)
    {
        super(entity);
        this.duration = duration;
        animatingEntity = (DinosaurEntity) entity;
        this.animation = animation;
    }

    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    @Override
    public boolean isAutomatic()
    {
        return true;
    }

    @Override
    public int getDuration()
    {
        return duration;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        animatingEntity.currentAnim = this;
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
        animatingEntity.currentAnim = null;
    }
}
