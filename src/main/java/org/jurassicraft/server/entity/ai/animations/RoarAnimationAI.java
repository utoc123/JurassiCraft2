package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class RoarAnimationAI extends EntityAIBase
{
    protected DinosaurEntity animatingEntity;

    public RoarAnimationAI(IAnimatedEntity entity)
    {
        super();
        this.animatingEntity = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return !animatingEntity.isDead && animatingEntity.getAttackTarget() == null && animatingEntity.getAgePercentage() > 75 && !animatingEntity.isSleeping() && animatingEntity.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting()
    {
        animatingEntity.setAnimation(DinosaurAnimation.ROARING.get());
        animatingEntity.playSound(animatingEntity.getSoundForAnimation(DinosaurAnimation.ROARING.get()), animatingEntity.getSoundVolume() + 1.25F, animatingEntity.getSoundPitch());
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
