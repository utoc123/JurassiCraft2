package org.jurassicraft.server.entity.base;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.ai.MoveUnderwaterEntityAI;

public abstract class AggressiveSwimmingDinosaurEntity extends AggressiveDinosaurEntity
{
    public AggressiveSwimmingDinosaurEntity(World world)
    {
        super(world);
        this.moveHelper = new AggressiveSwimmingDinosaurEntity.SwimmingMoveHelper();
        this.tasks.addTask(1, new MoveUnderwaterEntityAI(this));
        this.navigator = new PathNavigateSwimmer(this, world);
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        int i = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater())
        {
            --i;
            this.setAir(i);

            if (this.getAir() == -20)
            {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.drown, 2.0F);
            }
        }
        else
        {
            this.setAir(300);
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.isInWater() && this.onGround && this.rand.nextInt(20) == 0)
        {
            this.motionY += 0.4D;
            this.motionX += (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.2F);
            this.motionZ += (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.2F);
            this.rotationYaw = this.rand.nextFloat() * 360.0F;
            this.onGround = false;
            this.isAirBorne = true;
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public float getEyeHeight()
    {
        return this.height * 0.5F;
    }

    /**
     * Moves the entity based on the specified heading. Args: strafe, forward
     */
    public void moveEntityWithHeading(float strafe, float forward)
    {
        if (this.isServerWorld() && this.isInWater())
        {
            this.moveFlying(strafe, forward, 0.1F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.7D;
            this.motionY *= 0.7D;
            this.motionZ *= 0.7D;
        }
        else
        {
            super.moveEntityWithHeading(strafe, forward);
        }
    }

    class SwimmingMoveHelper extends EntityMoveHelper
    {
        private AggressiveSwimmingDinosaurEntity swimmingEntity = AggressiveSwimmingDinosaurEntity.this;

        public SwimmingMoveHelper()
        {
            super(AggressiveSwimmingDinosaurEntity.this);
        }

        public void onUpdateMoveHelper()
        {
            if (this.field_188491_h == EntityMoveHelper.Action.MOVE_TO && !this.swimmingEntity.getNavigator().noPath())
            {
                double distanceX = this.posX - this.swimmingEntity.posX;
                double distanceY = this.posY - this.swimmingEntity.posY;
                double distanceZ = this.posZ - this.swimmingEntity.posZ;
                double distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
                distance = (double) MathHelper.sqrt_double(distance);
                distanceY /= distance;
                float f = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.swimmingEntity.rotationYaw = this.limitAngle(this.swimmingEntity.rotationYaw, f, 30.0F);
                this.swimmingEntity.setAIMoveSpeed((float) (this.swimmingEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.5));
                this.swimmingEntity.motionY += (double) this.swimmingEntity.getAIMoveSpeed() * distanceY * 0.1D;
            }
            else
            {
                this.swimmingEntity.setAIMoveSpeed(0.0F);
            }
        }
    }
}
