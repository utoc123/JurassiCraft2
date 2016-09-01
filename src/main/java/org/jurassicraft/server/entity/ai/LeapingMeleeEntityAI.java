package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import org.jurassicraft.server.entity.DinosaurEntity;

public class LeapingMeleeEntityAI extends DinosaurAttackMeleeEntityAI {
    public LeapingMeleeEntityAI(DinosaurEntity entity, double speed) {
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
        float range = this.attacker.width * 6.0F;
        return distance < range - 1.0F || distance > range;
    }
}
