package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.List;

public class CallAnimationAI extends EntityAIBase
{
    protected DinosaurEntity animatingEntity;

    public CallAnimationAI(IAnimatedEntity entity)
    {
        super();
        animatingEntity = (DinosaurEntity) entity;
    }

    public List<Entity> getEntitiesWithinDistance(Entity entity, double width, double height)
    {
        return entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, new AxisAlignedBB(entity.posX - width, entity.posY - height, entity.posZ - width, entity.posX + width, entity.posY + height, entity.posZ + width));
    }

    @Override
    public boolean shouldExecute()
    {
        if (animatingEntity.getRNG().nextDouble() < 0.003)
        {
            List<Entity> entities = getEntitiesWithinDistance(animatingEntity, 50, 10);

            for (Entity entity : entities)
            {
                if (animatingEntity.getClass().isInstance(entity))
                {
                    animatingEntity.playSound(animatingEntity.getSoundForAnimation(Animations.CALLING.get()), animatingEntity.getSoundVolume() + 1.25F, animatingEntity.getSoundPitch());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        AnimationHandler.INSTANCE.sendAnimationMessage(animatingEntity, Animations.CALLING.get());
        animatingEntity.getNavigator().clearPathEntity();
    }
}
