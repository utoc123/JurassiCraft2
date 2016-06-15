package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class LookAnimationAI extends EntityAIBase
{
    protected DinosaurEntity animatingEntity;

    public LookAnimationAI(IAnimatedEntity entity)
    {
        super();
        this.animatingEntity = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return !animatingEntity.isDead && animatingEntity.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting()
    {
        animatingEntity.setAnimation(animatingEntity.getRNG().nextBoolean() ? DinosaurAnimation.LOOKING_LEFT.get() : DinosaurAnimation.LOOKING_RIGHT.get());
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}