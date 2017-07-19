package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.command.KeyBindingHandler;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.LeapingMeleeEntityAI;
import org.jurassicraft.server.entity.ai.RaptorHighGroundAI;
import org.jurassicraft.server.entity.ai.RaptorLeapEntityAI;
import org.jurassicraft.server.entity.ai.animations.BirdPreenAnimationAI;
import org.jurassicraft.server.entity.ai.animations.TailDisplayAnimationAI;
import org.jurassicraft.server.entity.ai.navigation.DinosaurPathNavigateClimber;
import org.jurassicraft.server.message.MicroraptorDismountMessage;
import org.jurassicraft.server.message.SetOrderMessage;

public class MicroraptorEntity extends DinosaurEntity {
    private int flyTime;
    private int groundHeight;
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(MicroraptorEntity.class, DataSerializers.BYTE);
    private EntityLookHelper glideLookHelper = new GlideLookHelper(this);

    public MicroraptorEntity(World world) {
        super(world);
        this.target(EntityPlayer.class, EntityChicken.class, EntityRabbit.class);
        this.tasks.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        // this.tasks.addTask(2, new RaptorGlideAI(this, 1.0f));
        this.tasks.addTask(3, new RaptorHighGroundAI(this, 1.0f));
        this.animationTasks.addTask(3, new BirdPreenAnimationAI(this));
        this.animationTasks.addTask(3, new TailDisplayAnimationAI(this));
        this.navigator = new DinosaurPathNavigateClimber(this, world);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(world.isRemote){
            this.updateClientControls();
        }
        Animation curAni = this.getAnimation();
        boolean landing = curAni == EntityAnimation.LEAP_LAND.get();
        boolean gliding = curAni == EntityAnimation.GLIDING.get();
        boolean leaping = curAni == EntityAnimation.LEAP.get();

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
            while (this.groundHeight <= 10) {
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
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (player.isSneaking() && hand == EnumHand.MAIN_HAND) {
            if (this.isOwner(player) && this.order == Order.SIT && player.getPassengers() != null && player.getPassengers().size() < 2) {
                this.startRiding(player);
            }
        }
        return super.processInteract(player, hand, stack);
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
    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateClimber(this, worldIn);
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

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(CONTROL_STATE, (byte) 0);
    }

    public boolean up() {
        return (dataManager.get(CONTROL_STATE) >> 1) == 1;
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = dataManager.get(CONTROL_STATE);
        if (newState) {
            dataManager.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
        } else {
            dataManager.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    public byte getControlState() {
        return dataManager.get(CONTROL_STATE);
    }

    public void setControlState(byte state) {
        dataManager.set(CONTROL_STATE, state);
    }

    public void updateRiding(Entity riding) {
        if (riding.isPassenger(this) && riding instanceof EntityPlayer) {
            int i = riding.getPassengers().indexOf(this);
            float radius = (i == 2 ? 0F : 0.4F) + (((EntityPlayer) riding).isElytraFlying() ? 2 : 0);
            float angle = (0.01745329251F * ((EntityPlayer) riding).renderYawOffset) + (i == 1 ? -90 : i == 0 ? 90 : 0);
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
            double extraY = (riding.isSneaking() ? 1.2D : 1.4D) + (i == 2 ? 0.4D : 0D);
            this.rotationYaw = ((EntityPlayer) riding).rotationYawHead;
            this.rotationYawHead = ((EntityPlayer) riding).rotationYawHead;
            this.prevRotationYaw = ((EntityPlayer) riding).rotationYawHead;
            this.setPosition(riding.posX + extraX, riding.posY + extraY, riding.posZ + extraZ);
            if (this.getControlState() == 1 << 1 || ((EntityPlayer) riding).isElytraFlying()) {
                this.dismountRidingEntity();
            }

        }
    }

    @SideOnly(Side.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getMinecraft();
        if (this.getRidingEntity() != null && this.getRidingEntity() == mc.player) {
            byte previousState = getControlState();
            setStateField(1, KeyBindingHandler.microraptor_off.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                JurassiCraft.NETWORK_WRAPPER.sendToServer(new MicroraptorDismountMessage(this.getEntityId(), controlState));
            }
        }
    }
}
