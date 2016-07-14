package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.ai.Herd;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class PeckGroundAnimationAI extends EntityAIBase
{
    protected DinosaurEntity dinosaur;

    public PeckGroundAnimationAI(IAnimatedEntity entity)
    {
        super();
        this.dinosaur = (DinosaurEntity) entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return !(dinosaur.isDead || dinosaur.getAttackTarget() != null || dinosaur.isSleeping() || (dinosaur.herd != null && dinosaur.herd.state == Herd.State.MOVING) || dinosaur.getAnimation() != DinosaurAnimation.IDLE.get()) && !dinosaur.isSwimming() && dinosaur.getRNG().nextDouble() < 0.02;
    }

    @Override
    public void startExecuting()
    {
        dinosaur.setAnimation(DinosaurAnimation.PECKING.get());
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
