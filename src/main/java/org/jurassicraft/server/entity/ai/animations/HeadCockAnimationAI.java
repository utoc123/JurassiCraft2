package org.jurassicraft.server.entity.ai.animations;

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
        this.animatingEntity = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return animatingEntity.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting()
    {
        animatingEntity.setAnimation(Animations.HEAD_COCKING.get());
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
