package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class HeadCockAnimationAI extends EntityAIBase
{
    protected DinosaurEntity animatingEntity;

    public HeadCockAnimationAI(IAnimatedEntity entity)
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
        AnimationHandler.INSTANCE.sendAnimationMessage(animatingEntity, Animations.HEAD_COCKING.get());
        animatingEntity.getNavigator().clearPathEntity();
    }
}
