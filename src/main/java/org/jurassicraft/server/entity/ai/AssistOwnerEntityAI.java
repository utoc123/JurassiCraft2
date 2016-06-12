package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.UUID;

public class AssistOwnerEntityAI extends EntityAIBase
{
    private DinosaurEntity entity;
    private EntityPlayer owner;
    private EntityLivingBase target;

    public AssistOwnerEntityAI(DinosaurEntity entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        if (entity.getAgePercentage() > 75)
        {
            UUID ownerId = entity.getOwner();

            if (ownerId != null)
            {
                owner = entity.worldObj.getPlayerEntityByUUID(ownerId);

                if (owner != null)
                {
                    target = owner.getAITarget();

                    return target != null && entity.getOrder() == DinosaurEntity.Order.FOLLOW;
                }
            }
        }

        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        return !target.isDead && entity.getOrder() == DinosaurEntity.Order.FOLLOW;
    }

    @Override
    public void updateTask()
    {
        entity.setAttackTarget(target);
    }

    @Override
    public void resetTask()
    {
        entity.setAttackTarget(null);
    }
}
