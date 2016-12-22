package org.jurassicraft.server.entity.ai;

import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.model.animation.PoseHandler;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.GrowthStage;
import org.jurassicraft.server.entity.dinosaur.VelociraptorEntity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class VelociraptorLeapEntityAI extends EntityAIBase {
    private VelociraptorEntity entity;
    private EntityLivingBase target;

    private int prevTick;
    private DinosaurAnimation animation;

    private double targetPrevPosX;
    private double targetPrevPosZ;

    private boolean ticked = false;

    public VelociraptorLeapEntityAI(VelociraptorEntity entity) {
        this.entity = entity;
        this.setMutexBits(Mutex.ATTACK | Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.herd != null && this.entity.herd.fleeing) {
            return false;
        }

        EntityLivingBase target = this.entity.getAttackTarget();

        if (target != null && target.isEntityAlive() && !(target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass())) {
            float distance = this.entity.getDistanceToEntity(target);

            if (distance >= 5 && distance <= 6 && this.entity.onGround) {
                this.target = target;

                return true;
            }
        }

        return false;
    }

    @Override
    public void startExecuting() {
        this.animation = DinosaurAnimation.VELOCIRAPTOR_PREPARE_POUNCE;
        PoseHandler poseHandler = this.entity.getDinosaur().getPoseHandler();
        GrowthStage growthStage = this.entity.getGrowthStage();
        poseHandler.getAnimationLength(DinosaurAnimation.VELOCIRAPTOR_LEAP.get(), growthStage);
        this.entity.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
        this.ticked = false;
    }

    @Override
    public void updateTask() {
        int tick = this.entity.getAnimationTick();

        if (this.animation == DinosaurAnimation.VELOCIRAPTOR_PREPARE_POUNCE && tick < this.prevTick) {
            this.animation = DinosaurAnimation.VELOCIRAPTOR_LEAP;
            this.entity.setAnimation(this.animation.get());

            this.entity.playSound(SoundHandler.VELOCIRAPTOR_ATTACK, this.entity.getSoundVolume(), this.entity.getSoundPitch());

            double targetSpeedX = this.target.posX - (!this.ticked ? this.target.prevPosX : this.targetPrevPosX);
            double targetSpeedZ = this.target.posZ - (!this.ticked ? this.target.prevPosZ : this.targetPrevPosZ);

            double length = 6.0;

            double destX = this.target.posX + targetSpeedX * length;
            double destZ = this.target.posZ + targetSpeedZ * length;

            double delta = Math.sqrt((destX - this.entity.posX) * (destX - this.entity.posX) + (destZ - this.entity.posZ) * (destZ - this.entity.posZ));
            double angle = Math.atan2(destZ - this.entity.posZ, destX - this.entity.posX);

            this.entity.motionX = delta / length * Math.cos(angle);
            this.entity.motionZ = (delta / length * Math.sin(angle));
            this.entity.motionY = Math.min(0.3, Math.max(0, (this.target.posY - this.entity.posY) * 0.1)) + 0.6;
        } else if (this.animation == DinosaurAnimation.VELOCIRAPTOR_LEAP && this.entity.motionY < 0) {
            this.animation = DinosaurAnimation.VELOCIRAPTOR_LAND;
            this.entity.setAnimation(this.animation.get());
        } else if (this.animation == DinosaurAnimation.VELOCIRAPTOR_LAND && (this.entity.onGround || this.entity.isSwimming())) {
            this.animation = DinosaurAnimation.IDLE;
            this.entity.setAnimation(this.animation.get());

            if (this.entity.getEntityBoundingBox() != null && this.target.getEntityBoundingBox() != null && this.entity.getEntityBoundingBox().intersectsWith(this.target.getEntityBoundingBox().expand(2.0, 2.0, 2.0))) {
                this.entity.attackEntityAsMob(this.target);
            }
        }

        this.targetPrevPosX = this.target.posX;
        this.targetPrevPosZ = this.target.posZ;
        this.ticked = true;

        if (this.entity.getAnimation() != this.animation.get()) {
            this.entity.setAnimation(this.animation.get());
            this.entity.setAnimationTick(this.prevTick + 1);
        }

        this.prevTick = tick;
    }

    @Override
    public void resetTask() {
        this.entity.setAnimation(DinosaurAnimation.IDLE.get());
    }

    @Override
    public boolean continueExecuting() {
        return !this.target.isDead && !(this.target instanceof DinosaurEntity && ((DinosaurEntity) this.target).isCarcass()) && this.animation != DinosaurAnimation.IDLE;
    }
}
