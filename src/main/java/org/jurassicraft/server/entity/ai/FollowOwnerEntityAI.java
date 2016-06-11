package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.UUID;

public class FollowOwnerEntityAI extends EntityAIBase
{
    private DinosaurEntity entity;
    private int recalculateTime;
    private float oldWaterCost;
    private EntityPlayer owner;

    public FollowOwnerEntityAI(DinosaurEntity entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        UUID ownerId = entity.getOwner();

        if (ownerId != null)
        {
            EntityPlayer owner = entity.worldObj.getPlayerEntityByUUID(ownerId);

            return owner != null && entity.getOrder() == DinosaurEntity.Order.FOLLOW && isOwnerFar(owner);
        }

        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        return !(entity.getCollisionBoundingBox() != null && owner.getCollisionBoundingBox() != null && entity.getCollisionBoundingBox().expand(2, 2, 2).intersectsWith(owner.getCollisionBoundingBox())) && !this.entity.getNavigator().noPath() && owner.isEntityAlive() && entity.getOrder() == DinosaurEntity.Order.FOLLOW;
    }

    @Override
    public void startExecuting()
    {
        this.recalculateTime = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
        this.owner = entity.worldObj.getPlayerEntityByUUID(entity.getOwner());
    }

    @Override
    public void resetTask()
    {
        this.owner = null;
        this.entity.getNavigator().clearPathEntity();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void updateTask()
    {
        this.entity.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float) this.entity.getVerticalFaceSpeed());

        if (!this.entity.isMovementBlocked())
        {
            if (this.recalculateTime-- <= 0)
            {
                this.recalculateTime = 10;

                if (isOwnerFar(owner))
                {
                    this.entity.getNavigator().tryMoveToEntityLiving(this.owner, 0.8F);
                }
            }
        }
    }

    private boolean isOwnerFar(EntityPlayer owner)
    {
        return entity.getDistanceSqToEntity(owner) > entity.width * 40;
    }
}
