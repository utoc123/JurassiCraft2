package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.List;

public class CallAnimationAI extends EntityAIBase
{
    protected DinosaurEntity animatingEntity;

    public CallAnimationAI(IAnimatedEntity entity)
    {
        super();
        this.animatingEntity = (DinosaurEntity) entity;
    }

    public List<Entity> getEntitiesWithinDistance(Entity entity, double width, double height)
    {
        return entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, new AxisAlignedBB(entity.posX - width, entity.posY - height, entity.posZ - width, entity.posX + width, entity.posY + height, entity.posZ + width));
    }

    @Override
    public boolean shouldExecute()
    {
        if (animatingEntity.isDead || animatingEntity.getAttackTarget() != null || animatingEntity.isSleeping())
        {
            return false;
        }

        if (animatingEntity.getRNG().nextDouble() < 0.003)
        {
            List<Entity> entities = this.getEntitiesWithinDistance(animatingEntity, 50, 10);

            for (Entity entity : entities)
            {
                if (this.animatingEntity.getClass().isInstance(entity))
                {
                    this.animatingEntity.playSound(animatingEntity.getSoundForAnimation(DinosaurAnimation.CALLING.get()), animatingEntity.getSoundVolume() + 1.25F, animatingEntity.getSoundPitch());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void startExecuting()
    {
        animatingEntity.setAnimation(DinosaurAnimation.CALLING.get());
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
