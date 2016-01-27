package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.common.animation.Animation;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class LookAnimationAI extends EntityAIBase
{
    protected DinosaurEntity animatingEntity;

    public LookAnimationAI(IAnimated entity)
    {
        super();
        animatingEntity = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute()
    {
        if (animatingEntity.getRNG().nextDouble() < 0.01)
        {
            return true;
        }

        return false;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        Animation.sendAnimationPacket(animatingEntity, animatingEntity.getRNG().nextBoolean() ? Animations.LOOKING_LEFT.get() : Animations.LOOKING_RIGHT.get());
        animatingEntity.getNavigator().clearPathEntity();
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
        animatingEntity.currentAnim = null;
    }
}
