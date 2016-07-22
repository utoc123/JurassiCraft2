package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import org.jurassicraft.server.entity.dinosaur.VelociraptorEntity;

public class VelociraptorMeleeEntityAI extends DinosaurAttackMeleeEntityAI {
    public VelociraptorMeleeEntityAI(VelociraptorEntity entity, double speed) {
        super(entity, speed, false);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = this.attacker.getAttackTarget();

        return super.shouldExecute() && target != null && this.isInRange(target);
    }

    @Override
    public boolean continueExecuting() {
        return this.shouldExecute() && super.continueExecuting();
    }

    private boolean isInRange(EntityLivingBase target) {
        float distance = this.attacker.getDistanceToEntity(target);

        return distance < 5 || distance > 6;
    }
}
