package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.UUID;

public class DefendOwnerEntityAI extends EntityAIBase
{
    private DinosaurEntity entity;
    private EntityPlayer owner;
    private EntityLivingBase lastAttacker;

    public DefendOwnerEntityAI(DinosaurEntity entity)
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
                    lastAttacker = owner.getLastAttacker();

                    return lastAttacker != null && entity.getOrder() == DinosaurEntity.Order.FOLLOW;
                }
            }
        }

        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        return !lastAttacker.isDead && entity.getOrder() == DinosaurEntity.Order.FOLLOW;
    }

    @Override
    public void updateTask()
    {
        entity.setAttackTarget(lastAttacker);
    }

    @Override
    public void resetTask()
    {
        entity.setAttackTarget(null);
    }
}
