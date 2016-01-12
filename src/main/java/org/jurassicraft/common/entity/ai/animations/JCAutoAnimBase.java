package org.jurassicraft.common.entity.ai.animations;

import net.ilexiconn.llibrary.common.animation.Animation;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import org.jurassicraft.common.animation.AIAnimation;
import org.jurassicraft.common.entity.base.EntityDinosaur;

public class JCAutoAnimBase extends AIAnimation
{
    protected EntityDinosaur animatingEntity;
    protected int duration;
    protected Animation animation;

    public JCAutoAnimBase(IAnimated entity, int duration, Animation animation)
    {
        super(entity);
        this.duration = duration;
        animatingEntity = (EntityDinosaur) entity;
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
