package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.AxisAlignedBB;
import org.jurassicraft.client.animation.Animations;
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

    public List<Entity> getEntitiesWithinDistance(Entity e, double xz, double y)
    {
        return e.worldObj.getEntitiesWithinAABBExcludingEntity(e, AxisAlignedBB.fromBounds(e.posX - xz, e.posY - y, e.posZ - xz, e.posX + xz, e.posY + y, e.posZ + xz));
    }

    @Override
    public boolean shouldExecute()
    {
        if (animatingEntity.getRNG().nextDouble() < 0.003)
        {
            List<Entity> entities = this.getEntitiesWithinDistance(animatingEntity, 50, 10);

            for (Entity entity : entities)
            {
                if (this.animatingEntity.getClass().isInstance(entity))
                {
                    this.animatingEntity.playSound(animatingEntity.getSoundForAnimation(Animations.CALLING.get()), animatingEntity.getSoundVolume() + 1.25F, animatingEntity.getSoundPitch());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void startExecuting()
    {
        animatingEntity.setAnimation(Animations.CALLING.get());
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
