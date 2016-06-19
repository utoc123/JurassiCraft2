package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class TargetCarcassEntityAI extends EntityAINearestAttackableTarget<DinosaurEntity>
{
    private DinosaurEntity entity;

    public TargetCarcassEntityAI(DinosaurEntity entity)
    {
        super(entity, DinosaurEntity.class, true);
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return entity.getMetabolism().isHungry() && super.shouldExecute();
    }

    @Override
    public boolean continueExecuting()
    {
        return !(target != null && target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass()) || super.continueExecuting();
    }
}
