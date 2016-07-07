package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.SoundEvent;
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

    private boolean ticked = false;

    public VelociraptorLeapEntityAI(VelociraptorEntity entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase target = entity.getAttackTarget();

        if (target != null && target.isEntityAlive() && !(target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass()))
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
        ticked = false;
    }

    @Override
    public void updateTask()
    {
        int tick = entity.getAnimationTick();

        if (animation == DinosaurAnimation.VELOCIRAPTOR_PREPARE_POUNCE && tick < prevTick)
        {
            animation = DinosaurAnimation.VELOCIRAPTOR_LEAP;
            entity.setAnimation(animation.get());

            SoundEvent sound = entity.getHurtSound();

            if (sound != null)
            {
                entity.playSound(sound, entity.getSoundVolume(), entity.getSoundPitch());
            }

            double targetSpeedX = target.posX - (!ticked ? target.prevPosX : targetPrevPosX);
            double targetSpeedZ = target.posZ - (!ticked ? target.prevPosZ : targetPrevPosZ);

            double length = 6.0;

            double destX = target.posX + targetSpeedX * length;
            double destZ = target.posZ + targetSpeedZ * length;

            double delta = Math.sqrt((destX - entity.posX) * (destX - entity.posX) + (destZ - entity.posZ) * (destZ - entity.posZ));
            double angle = Math.atan2(destZ - entity.posZ, destX - entity.posX);

            this.entity.motionX = delta / length * Math.cos(angle);
            this.entity.motionZ = (delta / length * Math.sin(angle));
            this.entity.motionY = Math.min(0.3, Math.max(0, (target.posY - entity.posY) * 0.1)) + 0.6;
        }
        else if (animation == DinosaurAnimation.VELOCIRAPTOR_LEAP && entity.motionY < 0)
        {
            animation = DinosaurAnimation.VELOCIRAPTOR_LAND;
            entity.setAnimation(animation.get());
        }
        else if (animation == DinosaurAnimation.VELOCIRAPTOR_LAND && tick < prevTick && entity.onGround)
        {
            animation = DinosaurAnimation.IDLE;
            entity.setAnimation(animation.get());

            if (entity.getEntityBoundingBox() != null && target.getEntityBoundingBox() != null && entity.getEntityBoundingBox().intersectsWith(target.getEntityBoundingBox().expand(2.0, 2.0, 2.0)))
            {
                entity.attackEntityAsMob(target);
            }
        }

        targetPrevPosX = target.posX;
        targetPrevPosZ = target.posZ;
        ticked = true;

        if (entity.getAnimation() != animation.get())
        {
            entity.setAnimation(animation.get());
            entity.setAnimationTick(prevTick + 1);
        }

        prevTick = tick;
    }

    @Override
    public void resetTask()
    {
        entity.setAnimation(DinosaurAnimation.IDLE.get());
    }

    @Override
    public boolean continueExecuting()
    {
        return !target.isDead && !(target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass()) && animation != DinosaurAnimation.IDLE;
    }
}
