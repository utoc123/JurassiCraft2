package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import org.jurassicraft.server.entity.dinosaur.VelociraptorEntity;

public class VelociraptorMeleeEntityAI extends DinosaurAttackMeleeEntityAI
{
    public VelociraptorMeleeEntityAI(VelociraptorEntity entity, double speed)
    {
        super(entity, speed, false);
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase target = attacker.getAttackTarget();

        return super.shouldExecute() && target != null && isInRange(target);
    }

    @Override
    public boolean continueExecuting()
    {
        return shouldExecute() && super.continueExecuting();
    }

    private boolean isInRange(EntityLivingBase target)
    {
        float distance = attacker.getDistanceToEntity(target);

        return distance < 5 || distance > 6;
    }
}
