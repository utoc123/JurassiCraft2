package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class LookAnimationAI extends EntityAIBase
{
    protected DinosaurEntity animatingEntity;

    public LookAnimationAI(IAnimatedEntity entity)
    {
        super();
        animatingEntity = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return animatingEntity.getRNG().nextDouble() < 0.01;

    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        AnimationHandler.INSTANCE.sendAnimationMessage(animatingEntity, animatingEntity.getRNG().nextBoolean() ? Animations.LOOKING_LEFT.get() : Animations.LOOKING_RIGHT.get());
        animatingEntity.getNavigator().clearPathEntity();
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
        animatingEntity.currentAnim = null;
    }
}
