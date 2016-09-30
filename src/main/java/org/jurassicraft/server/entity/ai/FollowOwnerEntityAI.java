package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;
import org.jurassicraft.server.entity.DinosaurEntity;

import java.util.UUID;

public class FollowOwnerEntityAI extends EntityAIBase {
    private DinosaurEntity entity;
    private int recalculateTime;
    private float oldWaterCost;
    private EntityPlayer owner;

    public FollowOwnerEntityAI(DinosaurEntity entity) {
        this.entity = entity;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.getNavigator().noPath() && this.entity.getOrder() == DinosaurEntity.Order.FOLLOW) {
            UUID ownerId = this.entity.getOwner();
            if (ownerId != null) {
                EntityPlayer owner = this.entity.worldObj.getPlayerEntityByUUID(ownerId);
                return owner != null && this.isOwnerFar(owner);
            }
        }

        return false;
    }

    @Override
    public boolean continueExecuting() {
        return !(this.entity.getCollisionBoundingBox() != null && this.owner.getCollisionBoundingBox() != null && this.entity.getCollisionBoundingBox().expand(2, 2, 2).intersectsWith(this.owner.getCollisionBoundingBox())) && !this.entity.getNavigator().noPath() && this.owner.isEntityAlive() && this.entity.getOrder() == DinosaurEntity.Order.FOLLOW;
    }

    @Override
    public void startExecuting() {
        this.recalculateTime = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
        this.owner = this.entity.worldObj.getPlayerEntityByUUID(this.entity.getOwner());
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.entity.getNavigator().clearPathEntity();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void updateTask() {
        this.entity.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float) this.entity.getVerticalFaceSpeed());

        if (!this.entity.isMovementBlocked()) {
            if (this.recalculateTime-- <= 0) {
                this.recalculateTime = 10;

                if (this.isOwnerFar(this.owner)) {
                    this.entity.getNavigator().tryMoveToEntityLiving(this.owner, 0.8F);
                }
            }
        }
    }

    private boolean isOwnerFar(EntityPlayer owner) {
        return this.entity.getDistanceSqToEntity(owner) > this.entity.width * 40;
    }
}
