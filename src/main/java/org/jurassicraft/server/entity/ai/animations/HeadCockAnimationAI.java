package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class HeadCockAnimationAI extends EntityAIBase
{
    protected DinosaurEntity dinosaur;

    public HeadCockAnimationAI(IAnimatedEntity entity)
    {
        super();
        this.dinosaur = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return !dinosaur.isDead && !dinosaur.isCarcass() && dinosaur.getAttackTarget() == null && !dinosaur.isSwimming() && dinosaur.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting()
    {
        dinosaur.setAnimation(DinosaurAnimation.HEAD_COCKING.get());
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
