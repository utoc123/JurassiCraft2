package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class SelectTargetEntityAI<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
{
    private DinosaurEntity entity;

    public SelectTargetEntityAI(DinosaurEntity entity, Class<T> classTarget, boolean checkSight)
    {
        super(entity, classTarget, checkSight);
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return entity.getAgePercentage() > 75 && entity.getOwner() == null && !(entity.shouldSleep() && entity.getStayAwakeTime() <= 0) && super.shouldExecute();
    }
}
