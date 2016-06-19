package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.model.animation.PoseHandler;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.GrowthStage;
import org.jurassicraft.server.entity.dinosaur.VelociraptorEntity;

public class VelociraptorLeapEntityAI extends EntityAIBase
{
    private VelociraptorEntity entity;
    private EntityLivingBase target;

    private int leapLength;

    private int prevTick;
    private DinosaurAnimation animation;

    private double targetPrevPosX;
    private double targetPrevPosZ;

    public VelociraptorLeapEntityAI(VelociraptorEntity entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase target = entity.getAttackTarget();

        if (target != null)
        {
            float distance = entity.getDistanceToEntity(target);

            if (distance >= 5 && distance <= 6 && entity.onGround)
            {
                this.target = target;

                return true;
            }
        }

        return false;
    }

    @Override
    public void startExecuting()
    {
        animation = DinosaurAnimation.VELOCIRAPTOR_PREPARE_POUNCE;
        PoseHandler poseHandler = entity.getDinosaur().getPoseHandler();
        GrowthStage growthStage = entity.getGrowthStage();
        leapLength = poseHandler.getAnimationLength(DinosaurAnimation.VELOCIRAPTOR_LEAP.get(), growthStage);
        entity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
    }

    @Override
    public void updateTask()
    {
        int tick = entity.getAnimationTick();

        targetPrevPosX = target.posX;
        targetPrevPosZ = target.posZ;

        if (animation == DinosaurAnimation.VELOCIRAPTOR_PREPARE_POUNCE && tick < prevTick)
        {
            animation = DinosaurAnimation.VELOCIRAPTOR_LEAP;
            entity.setAnimation(animation.get());

            double targetSpeedX = target.posX - targetPrevPosX;
            double targetSpeedZ = target.posZ - targetPrevPosZ;

            double length = 6.0;

            double destX = target.posX + targetSpeedX * length * 2;
            double destZ = target.posZ + targetSpeedZ * length * 2;

            double delta = Math.sqrt((destX - entity.posX) * (destX - entity.posX) + (destZ - entity.posZ) * (destZ - entity.posZ));
            double angle = Math.atan2((destZ - entity.posZ), (destX - entity.posX));

            this.entity.motionX = (delta / length) * Math.cos(angle);
            this.entity.motionZ = (delta / length) * Math.sin(angle);
            this.entity.motionY = 0.6D;
        }
        else if (animation == DinosaurAnimation.VELOCIRAPTOR_LEAP && entity.motionY < 0)
        {
            animation = DinosaurAnimation.VELOCIRAPTOR_LAND;
            entity.setAnimation(animation.get());
        }
        else if (animation == DinosaurAnimation.VELOCIRAPTOR_LAND && tick < prevTick)
        {
            animation = DinosaurAnimation.IDLE;
            entity.setAnimation(animation.get());

            entity.attackEntityAsMob(target);
        }

        if (entity.getAnimation() != animation.get())
        {
            entity.setAnimation(animation.get());
            entity.setAnimationTick(prevTick + 1);
        }

        prevTick = tick;
    }

    @Override
    public boolean continueExecuting()
    {
        return !target.isDead && !(target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass()) && animation != DinosaurAnimation.IDLE;
    }
}
