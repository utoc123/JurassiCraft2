package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.DinosaurEntity;

/**
 * Created by jnad325 on 7/23/15.
 */
public class JCNonAutoAnimBase extends AnimationAI
{
    protected DinosaurEntity animatingEntity;
    protected Animation animation;
    protected int chance;

    public JCNonAutoAnimBase(DinosaurEntity entity, Animation animation, int chance)
    {
        super(entity);
        this.animatingEntity = entity;
        this.animation = animation;
        this.chance = chance;
    }

    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    @Override
    public boolean isAutomatic()
    {
        return false;
    }

    @Override
    public boolean shouldExecute()
    {
        return animatingEntity.getAnimation() == Animations.IDLE.get() && animatingEntity.getRNG().nextInt(chance) == 0;
    }
}
