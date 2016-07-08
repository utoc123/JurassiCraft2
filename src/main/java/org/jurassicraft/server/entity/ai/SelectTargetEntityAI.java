package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

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
    public boolean shouldExecute()
    {
        if (!(EntityPlayer.class.isAssignableFrom(targetClass) || DinosaurEntity.class.isAssignableFrom(targetClass) && entity.getDinosaur().getDiet().isCarnivorous()))
        {
            if (!entity.getMetabolism().isHungry())
            {
                return false;
            }
        }

        return entity.getAgePercentage() > 75 && entity.getOwner() == null && !entity.isSleeping() && entity.getAttackCooldown() <= 0 && super.shouldExecute();
    }
}
