package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.LinkedList;
import java.util.List;

public class SelectTargetEntityAI<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
{
    private DinosaurEntity entity;

    public SelectTargetEntityAI(DinosaurEntity entity, Class<T> classTarget, boolean checkSight)
    {
        super(entity, classTarget, checkSight);
        this.entity = entity;
        this.setMutexBits(0);
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
        super.startExecuting();

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
        if (!(EntityPlayer.class.isAssignableFrom(targetClass) || (DinosaurEntity.class.isAssignableFrom(targetClass) && entity.getDinosaur().getDiet().isCarnivorous())))
        {
            if (!entity.getMetabolism().isHungry())
            {
                return false;
            }
        }

        return !(entity.herd != null && entity.herd.fleeing) && entity.getAgePercentage() > 75 && entity.getOwner() == null && !entity.isSleeping() && entity.getAttackCooldown() <= 0 && super.shouldExecute();
    }
}
