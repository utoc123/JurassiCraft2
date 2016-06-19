package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.ai.util.HerdManager;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.ArrayList;
import java.util.List;

public class RespondToAttackEntityAI extends EntityAIBase
{
    private DinosaurEntity entity;
    private List<EntityLivingBase> attackers;
    private List<DinosaurEntity> herdEntities;
    private HerdManager.Herd herd;

    public RespondToAttackEntityAI(DinosaurEntity entity)
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
            herdEntities = new ArrayList<>();

            boolean hasHerd = false;

            if (attacker instanceof DinosaurEntity)
            {
                HerdManager.Herd herd = HerdManager.INSTANCE.getHerd((DinosaurEntity) attacker);

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

            HerdManager.Herd herd = HerdManager.INSTANCE.getHerd(entity);

            if (herd != null)
            {
                this.herdEntities.addAll(herd.getAllDinosaurs());
                this.herd = herd;

                for (DinosaurEntity entity : herdEntities)
                {
                    entity.getNavigator().clearPathEntity();
                }
            }
            else
            {
                this.herdEntities.add(entity);
            }

            if (!entity.getDinosaur().shouldFlee() && entity.getAgePercentage() > 75)
            {
                entity.setAttackTarget(attacker);
            }

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
        for (DinosaurEntity entity : herdEntities)
        {
            if (entity.getDinosaur().shouldFlee() && entity.getNavigator().noPath())
            {
                //TODO Fix fleeing AI

                EntityLivingBase attacker = attackers.get(entity.getRNG().nextInt(attackers.size()));
                Vec3d escape = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, 16, 7, new Vec3d(attacker.posX, attacker.posY, attacker.posZ));

                if (escape != null)
                {
                    entity.getNavigator().tryMoveToXYZ(escape.xCoord, escape.yCoord, escape.zCoord, entity.getDinosaur().getAttackSpeed());
                }
            }
            else
            {
                EntityLivingBase attackTarget = entity.getAttackTarget();

                if ((attackTarget == null || isDead(attackTarget)) && entity.getAgePercentage() > 75)
                {
                    entity.setAttackTarget(attackers.get(entity.getRNG().nextInt(attackers.size())));
                }
                else if (herd != null && (entity.getAgePercentage() <= 75))
                {
                    HerdManager.Cluster cluster = herd.getCluster(entity);

                    if (cluster != null)
                    {
                        BlockPos center = cluster.getCenter();

                        entity.getNavigator().tryMoveToXYZ(center.getX(), center.getY(), center.getZ(), 1.0);
                    }
                }
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
        return !attacker.isEntityAlive() || (attacker instanceof DinosaurEntity && ((DinosaurEntity) attacker).isCarcass());
    }

    @Override
    public void resetTask()
    {
        for (DinosaurEntity entity : herdEntities)
        {
            entity.setAttackTarget(null);
        }
    }
}
