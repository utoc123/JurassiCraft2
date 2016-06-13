package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.server.entity.ai.util.HerdManager;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.ArrayList;
import java.util.List;

public class DefendSelfEntityAI extends EntityAIBase
{
    private DinosaurEntity entity;
    private List<EntityLivingBase> attackers;
    private List<DinosaurEntity> herd;

    public DefendSelfEntityAI(DinosaurEntity entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase attacker = entity.getAITarget();

        if (attacker != null && !attacker.isDead)
        {
            attackers = new ArrayList<>();
            herd = new ArrayList<>();

            if (attacker instanceof DinosaurEntity)
            {
                attackers.addAll(HerdManager.getInstance().getHerd((DinosaurEntity) attacker).getAllDinosaurs());
            }
            else
            {
                attackers.add(attacker);
            }

            herd.addAll(HerdManager.getInstance().getHerd(entity).getAllDinosaurs());

            return true;
        }

        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        for (EntityLivingBase attacker : attackers)
        {
            if (!isDead(attacker))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateTask()
    {
        for (DinosaurEntity entity : herd)
        {
            EntityLivingBase attackTarget = entity.getAttackTarget();

            if ((attackTarget == null || isDead(attackTarget)) && entity.getAgePercentage() > 75)
            {
                entity.setAttackTarget(attackers.get(entity.getRNG().nextInt(attackers.size())));
            }
        }

        List<EntityLivingBase> deadAttackers = new ArrayList<>();

        for (EntityLivingBase attacker : attackers)
        {
            if (isDead(attacker))
            {
                deadAttackers.add(attacker);
            }
        }

        attackers.removeAll(deadAttackers);
    }

    private boolean isDead(EntityLivingBase attacker)
    {
        return !attacker.isEntityAlive() || (attacker instanceof DinosaurEntity && ((DinosaurEntity) attacker).getDinosaur().getDiet().doesEatMeat() && ((DinosaurEntity) attacker).isCarcass());
    }

    @Override
    public void resetTask()
    {
        for (DinosaurEntity entity : herd)
        {
            entity.setAttackTarget(null);
        }
    }
}
