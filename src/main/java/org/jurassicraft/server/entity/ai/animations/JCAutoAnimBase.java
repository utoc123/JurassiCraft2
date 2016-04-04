package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class JCAutoAnimBase<T extends Entity & IAnimatedEntity> extends AnimationAI<T>
{
    protected Animation animation;

    public JCAutoAnimBase(DinosaurEntity entity, Animation animation)
    {
        super((T) entity);
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
    public void startExecuting()
    {
        AnimationHandler.INSTANCE.sendAnimationMessage(entity, this.getAnimation());
    }
}
