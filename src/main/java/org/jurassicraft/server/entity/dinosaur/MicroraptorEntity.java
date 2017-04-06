package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.LeapingMeleeEntityAI;
import org.jurassicraft.server.entity.ai.RaptorLeapEntityAI;
import org.jurassicraft.server.entity.ai.animations.BirdPreenAnimationAI;
import org.jurassicraft.server.entity.ai.animations.TailDisplayAnimationAI;

public class MicroraptorEntity extends DinosaurEntity {
    private int flyTime;
    private int groundHeight;

    private EntityLookHelper glideLookHelper = new GlideLookHelper(this);

    public MicroraptorEntity(World world) {
        super(world);
        this.target(EntityPlayer.class, EntityChicken.class, EntityRabbit.class);
        this.tasks.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.animationTasks.addTask(3, new BirdPreenAnimationAI(this));
        this.animationTasks.addTask(3, new TailDisplayAnimationAI<>(this));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        boolean landing = this.getAnimation() == EntityAnimation.LEAP_LAND.get();
        boolean gliding = this.getAnimation() == EntityAnimation.GLIDING.get();
        boolean leaping = this.getAnimation() == EntityAnimation.LEAP.get();

        if (this.onGround || this.inWater() || this.inLava() || this.isSwimming()) {
            this.flyTime = 0;
            if (gliding || landing) {
                this.setAnimation(EntityAnimation.IDLE.get());
                this.setFlag(7, false);
            }
        } else {
            this.flyTime++;
            if (this.flyTime > 4 && !leaping) {
                if (!landing) {
                    if (!gliding) {
                        this.setAnimation(EntityAnimation.GLIDING.get());
                    } else if (!this.world.isAirBlock(this.getPosition().down())) {
                        this.setAnimation(EntityAnimation.LEAP_LAND.get());
                    }
                }
                if (gliding) {
                    this.setFlag(7, true);
                }
            }
        }

        if (this.isElytraFlying()) {
            this.groundHeight = 0;
            BlockPos pos = this.getPosition();
            while (this.groundHeight <= 30) {
                if (this.world.isSideSolid(pos, EnumFacing.UP, true)) {
                    break;
                }
                pos = pos.down();
                this.groundHeight++;
            }
        }

        if (this.isServerWorld()) {
            this.getLookHelper().onUpdateLook();
        }
    }

    @Override
    public EntityLookHelper getLookHelper() {
        if (this.getAnimation() == EntityAnimation.GLIDING.get()) {
            return this.glideLookHelper;
        }
        return super.getLookHelper();
    }

    @Override
    public Vec3d getLookVec() {
        if (this.getAnimation() == EntityAnimation.GLIDING.get()) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
        }
        return super.getLookVec();
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) {
        if (this.getAnimation() == EntityAnimation.GLIDING.get() && slot == EntityEquipmentSlot.CHEST) {
            return new ItemStack(Items.ELYTRA);
        }
        return super.getItemStackFromSlot(slot);
    }

    @Override
    public EntityAIBase getAttackAI() {
        return new RaptorLeapEntityAI(this);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            super.fall(distance / 2.0F, damageMultiplier);
        }
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
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
        return 100;
    }

    public class GlideLookHelper extends EntityLookHelper {
        private MicroraptorEntity entity;

        public GlideLookHelper(MicroraptorEntity entity) {
            super(entity);
            this.entity = entity;
        }

        @Override
        public void onUpdateLook() {
            float radians = (float) Math.toRadians(this.entity.groundHeight);
            float cos = MathHelper.cos(radians);
            float sin = MathHelper.sin(radians + 0.8F);
            this.entity.rotationPitch = (float) Math.toDegrees(((cos + 1) * sin - 1.6F) * 3.5F);
        }

        private float updateRotation(float current, float desired, float range) {
            float offset = MathHelper.wrapDegrees(desired - current);
            if (offset > range) {
                offset = range;
            }
            if (offset < -range) {
                offset = -range;
            }
            return MathHelper.wrapDegrees(current + offset);
        }
    }
}
