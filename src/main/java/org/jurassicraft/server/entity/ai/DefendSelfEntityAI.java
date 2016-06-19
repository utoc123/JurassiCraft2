package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
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

        if (attacker != null && !attacker.isDead && !(attacker instanceof EntityPlayer && ((EntityPlayer) attacker).capabilities.isCreativeMode))
        {
            attackers = new ArrayList<>();
            herd = new ArrayList<>();

            boolean hasHerd = false;

            if (attacker instanceof DinosaurEntity)
            {
                HerdManager.Herd herd = HerdManager.getInstance().getHerd((DinosaurEntity) attacker);

                if (herd != null)
                {
                    attackers.addAll(herd.getAllDinosaurs());
                    hasHerd = true;
                }
            }

            if (!hasHerd)
            {
                attackers.add(attacker);
            }

            HerdManager.Herd herd = HerdManager.getInstance().getHerd(entity);

            if (herd != null)
            {
                this.herd.addAll(herd.getAllDinosaurs());
            }
            else
            {
                this.herd.add(entity);
            }

            entity.setAttackTarget(attacker);

            return true;
        }

        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        if (attackers.size() == 0)
        {
            return false;
        }

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
