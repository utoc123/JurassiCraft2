package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class DinosaurAttackMeleeEntityAI extends EntityAIAttackMelee
{
    public DinosaurAttackMeleeEntityAI(EntityCreature creature, double speed, boolean useLongMemory)
    {
        super(creature, speed, useLongMemory);
    }

    @Override
    public void updateTask()
    {
        if (attacker.getAttackTarget() != null)
        {
            super.updateTask();
        }
    }
}
