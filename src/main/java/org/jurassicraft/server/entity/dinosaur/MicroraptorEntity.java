package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.LeapingMeleeEntityAI;
import org.jurassicraft.server.entity.ai.RaptorLeapEntityAI;
import org.jurassicraft.server.entity.ai.animations.BirdPreenAnimationAI;
import org.jurassicraft.server.entity.ai.navigate.ClimbPathNavigate;

public class MicroraptorEntity extends DinosaurEntity {
    public static final DataParameter<Boolean> CLIMBING = EntityDataManager.createKey(MicroraptorEntity.class, DataSerializers.BOOLEAN);

    private boolean prevClimbing;
    private boolean climbing;

    public MicroraptorEntity(World world) {
        super(world);
        this.target(EntityPlayer.class, EntityAnimal.class, EntityVillager.class, GallimimusEntity.class, MussaurusEntity.class);
        this.tasks.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.animationTasks.addTask(3, new BirdPreenAnimationAI(this));
        this.moveHelper = new ClimbMoveHelper(this);
    }

    @Override
    protected PathNavigate getNewNavigator(World world) {
        return new ClimbPathNavigate(this, world);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, false);
    }

    @Override
    public EntityAIBase getAttackAI() {
        return new RaptorLeapEntityAI(this);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (this.getAnimation() != DinosaurAnimation.RAPTOR_LAND.get()) {
            super.fall(distance / 2.0F, damageMultiplier);
        }
    }

    @Override
    public void onUpdate() {
        this.climbing = false;
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            if (this.climbing && !this.prevClimbing) {
                this.setAnimation(DinosaurAnimation.START_CLIMBING.get());
            }
            this.dataManager.set(CLIMBING, this.climbing);
            this.prevClimbing = this.climbing;
        }
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (DinosaurAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.MICRORAPTOR_LIVING;
            case DYING:
                return SoundHandler.MICRORAPTOR_DEATH;
            case INJURED:
                return SoundHandler.MICRORAPTOR_HURT;
            case ATTACKING:
                return SoundHandler.MICRORAPTOR_ATTACK;
            case CALLING:
                return SoundHandler.MICRORAPTOR_LIVING;
        }

        return null;
    }

    @Override
    public int getMaxFallHeight() {
        return super.getMaxFallHeight() * 10;
    }

    @Override
    public boolean isClimbing() {
        return this.dataManager.get(CLIMBING);
    }

    public class ClimbMoveHelper extends EntityMoveHelper {
        private MicroraptorEntity microraptor;

        public ClimbMoveHelper(MicroraptorEntity microraptor) {
            super(microraptor);
            this.microraptor = microraptor;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.STRAFE) {
                float movementSpeed = (float) this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
                float speed = (float) this.speed * movementSpeed;
                float moveForward = this.moveForward;
                float moveStrafe = this.moveStrafe;
                float move = MathHelper.sqrt_float(moveForward * moveForward + moveStrafe * moveStrafe);
                if (move < 1.0F) {
                    move = 1.0F;
                }
                move = speed / move;
                moveForward = moveForward * move;
                moveStrafe = moveStrafe * move;
                float sin = MathHelper.sin(this.entity.rotationYaw * 0.017453292F);
                float cos = MathHelper.cos(this.entity.rotationYaw * 0.017453292F);
                float moveX = moveForward * cos - moveStrafe * sin;
                float moveZ = moveStrafe * cos + moveForward * sin;
                PathNavigate navigator = this.entity.getNavigator();
                if (navigator != null) {
                    NodeProcessor nodeProcessor = navigator.getNodeProcessor();
                    if (nodeProcessor != null && nodeProcessor.getPathNodeType(this.entity.worldObj, MathHelper.floor_double(this.entity.posX + moveX), MathHelper.floor_double(this.entity.posY), MathHelper.floor_double(this.entity.posZ + moveZ)) != PathNodeType.WALKABLE) {
                        this.moveForward = 1.0F;
                        this.moveStrafe = 0.0F;
                        speed = movementSpeed;
                    }
                }
                this.entity.setAIMoveSpeed(speed);
                this.entity.setMoveForward(this.moveForward);
                this.entity.setMoveStrafing(this.moveStrafe);
                this.action = EntityMoveHelper.Action.WAIT;
            } else if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double deltaX = this.posX - this.entity.posX;
                double deltaZ = this.posZ - this.entity.posZ;
                double deltaY = this.posY - this.entity.posY;
                double delta = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
                if (delta < 0.00000025) {
                    this.entity.setMoveForward(0.0F);
                    return;
                }
                float targetAngle = (float) (MathHelper.atan2(deltaZ, deltaX) * (180.0 / Math.PI)) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, targetAngle, 90.0F);
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                if ((deltaY > this.entity.stepHeight || !this.entity.onGround) && deltaX * deltaX + deltaZ * deltaZ < Math.max(1.0F, this.entity.width)) {
                    if ((deltaY > 1.1F || !this.entity.onGround) && this.entity.isCollidedHorizontally) {
                        this.entity.motionY = 0.2;
                        this.entity.fallDistance = 0.0F;
                        this.microraptor.climbing = true;
                    } else {
                        this.entity.getJumpHelper().setJumping();
                    }
                }
            } else {
                this.entity.setMoveForward(0.0F);
            }
        }
    }
}
