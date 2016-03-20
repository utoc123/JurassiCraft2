package org.jurassicraft.server.entity.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public abstract class AggressiveFlyingDinosaurEntity extends AggressiveDinosaurEntity
{
    public AggressiveFlyingDinosaurEntity(World world)
    {
        super(world);
        this.moveHelper = new AggressiveFlyingDinosaurEntity.FlyingMoveHelper();
        this.tasks.addTask(5, new AggressiveFlyingDinosaurEntity.AIRandomFly());
        this.tasks.addTask(7, new AggressiveFlyingDinosaurEntity.AILookAround());
    }

    @Override
    public void fall(float distance, float damageMultiplier)
    {
        // TODO slow itself down when landing, if falling too fast, take damage
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float strafe, float forward)
    {
        if (this.isInWater())
        {
            this.moveFlying(strafe, forward, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }
        else if (this.isInLava())
        {
            this.moveFlying(strafe, forward, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }
        else
        {
            float f2 = 0.91F;

            if (this.onGround)
            {
                f2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            this.moveFlying(strafe, forward, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;

            if (this.onGround)
            {
                f2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) f2;
            this.motionY *= (double) f2;
            this.motionZ *= (double) f2;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return false;
    }

    class AIRandomFly extends EntityAIBase
    {
        private AggressiveFlyingDinosaurEntity dino = AggressiveFlyingDinosaurEntity.this;

        public AIRandomFly()
        {
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityMoveHelper moveHelper = this.dino.getMoveHelper();

            if (!moveHelper.isUpdating())
            {
                return true;
            }
            else
            {
                double d0 = moveHelper.getX() - this.dino.posX;
                double d1 = moveHelper.getY() - this.dino.posY;
                double d2 = moveHelper.getZ() - this.dino.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean continueExecuting()
        {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            Random random = this.dino.getRNG();
            double d0 = this.dino.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.dino.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.dino.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.dino.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }

    class FlyingMoveHelper extends EntityMoveHelper
    {
        private AggressiveFlyingDinosaurEntity parentEntity = AggressiveFlyingDinosaurEntity.this;
        private int timer;

        public FlyingMoveHelper()
        {
            super(AggressiveFlyingDinosaurEntity.this);
        }

        public void onUpdateMoveHelper()
        {
            if (this.field_188491_h == EntityMoveHelper.Action.MOVE_TO)
            {
                double distanceX = this.posX - this.parentEntity.posX;
                double distanceY = this.posY - this.parentEntity.posY;
                double distanceZ = this.posZ - this.parentEntity.posZ;
                double distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

                if (this.timer-- <= 0)
                {
                    this.timer += this.parentEntity.getRNG().nextInt(5) + 2;
                    distance = (double)MathHelper.sqrt_double(distance);

                    if (this.isNotColliding(this.posX, this.posY, this.posZ, distance))
                    {
                        this.parentEntity.motionX += distanceX / distance * 0.1D;
                        this.parentEntity.motionY += distanceY / distance * 0.1D;
                        this.parentEntity.motionZ += distanceZ / distance * 0.1D;
                    }
                    else
                    {
                        this.field_188491_h = EntityMoveHelper.Action.WAIT;
                    }
                }
            }
        }

        /**
         * Checks if entity bounding box is not colliding with terrain
         */
        private boolean isNotColliding(double x, double y, double z, double distance)
        {
            double d0 = (x - this.parentEntity.posX) / distance;
            double d1 = (y - this.parentEntity.posY) / distance;
            double d2 = (z - this.parentEntity.posZ) / distance;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double)i < distance; ++i)
            {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!this.parentEntity.worldObj.getCubes(this.parentEntity, axisalignedbb).isEmpty())
                {
                    return false;
                }
            }

            return true;
        }
    }

    class AILookAround extends EntityAIBase
    {
        private AggressiveFlyingDinosaurEntity dino = AggressiveFlyingDinosaurEntity.this;

        public AILookAround()
        {
            this.setMutexBits(2);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return true;
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            if (this.dino.getAttackTarget() == null)
            {
                this.dino.renderYawOffset = this.dino.rotationYaw = -((float) Math.atan2(this.dino.motionX, this.dino.motionZ)) * 180.0F / (float) Math.PI;
            }
            else
            {
                EntityLivingBase attackTarget = this.dino.getAttackTarget();
                double maxDistance = 64.0D;

                if (attackTarget.getDistanceSqToEntity(this.dino) < maxDistance * maxDistance)
                {
                    double diffX = attackTarget.posX - this.dino.posX;
                    double diffZ = attackTarget.posZ - this.dino.posZ;
                    this.dino.renderYawOffset = this.dino.rotationYaw = -((float) Math.atan2(diffX, diffZ)) * 180.0F / (float) Math.PI;
                }
            }
        }
    }
}
