package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class RoarAnimationAI extends EntityAIBase
{
    protected DinosaurEntity dinosaur;

    public RoarAnimationAI(IAnimatedEntity entity)
    {
        super();
        this.dinosaur = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return !dinosaur.isDead && dinosaur.getAttackTarget() == null && dinosaur.getAgePercentage() > 75 && !dinosaur.isSleeping() && dinosaur.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting()
    {
        dinosaur.setAnimation(DinosaurAnimation.ROARING.get());
        dinosaur.playSound(dinosaur.getSoundForAnimation(DinosaurAnimation.ROARING.get()), dinosaur.getSoundVolume() + 1.25F, dinosaur.getSoundPitch());
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
