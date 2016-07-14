package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.List;

public class TargetCarcassEntityAI extends EntityAIBase
{
    private DinosaurEntity entity;
    private DinosaurEntity targetEntity;

    public TargetCarcassEntityAI(DinosaurEntity entity)
    {
        this.entity = entity;
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
        entity.resetAttackCooldown();
    }

    @Override
    public void startExecuting()
    {
        entity.setAttackTarget(targetEntity);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!entity.getMetabolism().isHungry())
        {
            return false;
        }

        if (this.entity.getRNG().nextInt(10) != 0)
        {
            return false;
        }

        if (!(entity.herd != null && entity.herd.fleeing) && !entity.isSleeping())
        {
            List<DinosaurEntity> entities = this.entity.worldObj.getEntitiesWithinAABB(DinosaurEntity.class, entity.getEntityBoundingBox().expand(16, 16, 16));

            if (entities.size() > 0)
            {
                targetEntity = null;
                int bestScore = Integer.MAX_VALUE;

                for (DinosaurEntity entity : entities)
                {
                    if (entity.isCarcass())
                    {
                        int score = (int) this.entity.getDistanceSqToEntity(entity);

                        if (score < bestScore)
                        {
                            bestScore = score;
                            targetEntity = entity;
                        }
                    }
                }

                return targetEntity != null;
            }
        }

        return false;
    }
}
