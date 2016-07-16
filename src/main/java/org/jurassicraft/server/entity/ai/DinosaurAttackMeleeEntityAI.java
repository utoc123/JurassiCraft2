package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class DinosaurAttackMeleeEntityAI extends EntityAIAttackMelee
{
    protected DinosaurEntity dinosaur;

    public DinosaurAttackMeleeEntityAI(DinosaurEntity dinosaur, double speed, boolean useLongMemory)
    {
        super(dinosaur, speed, useLongMemory);
        this.dinosaur = dinosaur;
    }

    @Override
    public void updateTask()
    {
        EntityLivingBase target = attacker.getAttackTarget();

        if (target != null)
        {
            super.updateTask();
        }

        if (target == null || target.isDead || (target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass()))
        {
            super.resetTask();
            attacker.setAttackTarget(null);
        }
    }
}
