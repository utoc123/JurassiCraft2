package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.LinkedList;
import java.util.List;

public class SelectTargetEntityAI<T extends EntityLivingBase> extends EntityAIBase
{
    private DinosaurEntity entity;
    private Class<T> targetClass;
    private T targetEntity;

    public SelectTargetEntityAI(DinosaurEntity entity, Class<T> targetClass)
    {
        this.entity = entity;
        this.targetClass = targetClass;
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

        Herd herd = entity.herd;

        if (herd != null && targetEntity != null)
        {
            List<EntityLivingBase> enemies = new LinkedList<>();

            if (targetEntity instanceof DinosaurEntity && ((DinosaurEntity) targetEntity).herd != null)
            {
                enemies.addAll(((DinosaurEntity) targetEntity).herd.members);
            }
            else
            {
                enemies.add(targetEntity);
            }

            for (EntityLivingBase enemy : enemies)
            {
                if (!herd.enemies.contains(enemy))
                {
                    herd.enemies.add(enemy);
                }
            }
        }
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.entity.getRNG().nextInt(10) != 0)
        {
            return false;
        }

        if (!(EntityPlayer.class.isAssignableFrom(targetClass) || (DinosaurEntity.class.isAssignableFrom(targetClass) && entity.getDinosaur().getDiet().isCarnivorous())))
        {
            if (!entity.getMetabolism().isHungry())
            {
                return false;
            }
        }

        if (!(entity.herd != null && entity.herd.fleeing) && entity.getAgePercentage() > 50 && entity.getOwner() == null && !entity.isSleeping() && entity.getAttackCooldown() <= 0)
        {
            List<T> entities = this.entity.worldObj.getEntitiesWithinAABB(this.targetClass, entity.getEntityBoundingBox().expand(16, 16, 16));

            if (entities.size() > 0)
            {
                targetEntity = null;
                double bestScore = Double.MAX_VALUE;

                for (T entity : entities)
                {
                    if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode))
                    {
                        double score = entity.getHealth() <= 0.0F ? (this.entity.getDistanceSqToEntity(entity) / entity.getHealth()) : 0;

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
