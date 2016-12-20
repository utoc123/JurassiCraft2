package org.jurassicraft.server.entity.vehicle;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.proxy.ClientProxy;
import org.jurassicraft.client.sound.CarSound;
import org.jurassicraft.server.entity.vehicle.modules.SeatEntity;
import org.jurassicraft.server.message.UpdateCarControlMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CarEntity extends Entity {
    public static final DataParameter<Byte> WATCHER_STATE = EntityDataManager.createKey(CarEntity.class, DataSerializers.BYTE);
    public static final DataParameter<Float> WATCHER_HEALTH = EntityDataManager.createKey(CarEntity.class, DataSerializers.FLOAT);
    public static final float MAX_HEALTH = 40.0F;

    public float wheelRotation;
    public float wheelRotateAmount;
    public float prevWheelRotateAmount;
    protected int interpProgress;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    private Map<Integer, SeatEntity> seats = new HashMap<>();

    @SideOnly(Side.CLIENT)
    public CarSound sound;

    public float health;

    private float healAmount;
    private int healCooldown = 40;

    private boolean droppedItems;

    public CarEntity(World world) {
        super(world);
        this.setSize(3.0F, 2.5F);

        this.stepHeight = 1.5F;

        if (world.isRemote) {
            this.updateSound();
        }

        this.health = MAX_HEALTH;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.isEntityInvulnerable(source)) {
            if (source.getEntity() instanceof EntityPlayer) {
                amount *= 10.0F;
                this.healAmount += amount;
                this.healCooldown = 40;
            }

            this.health -= amount;

            if (this.health < 0.0F) {
                this.setDead();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double dist) {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote) {
            if (this.healCooldown > 0) {
                this.healCooldown--;
            } else if (this.healAmount > 0) {
                this.health++;
                this.healAmount--;

                if (this.health > MAX_HEALTH) {
                    this.health = MAX_HEALTH;
                    this.healAmount = 0;
                }
            }
        }

        if (this.seats.size() == 0) {
            if (!this.world.isRemote) {
                this.addSeat(new SeatEntity(this.world, this, 0, -0.5F, 0.8F, 0.0F, 1.0F, 1.5F));
                this.addSeat(new SeatEntity(this.world, this, 1, 0.5F, 0.8F, 0.0F, 1.0F, 1.5F));
                this.addSeat(new SeatEntity(this.world, this, 2, -0.5F, 1.05F, 2.2F, 1.0F, 1.5F));
                this.addSeat(new SeatEntity(this.world, this, 3, 0.5F, 1.05F, 2.2F, 1.0F, 1.5F));

                for (Map.Entry<Integer, SeatEntity> entry : this.seats.entrySet()) {
                    this.worldObj.spawnEntityInWorld(entry.getValue());
                }
            }
        }

        if (this.interpProgress > 0) {
            double interpolatedX = this.posX + (this.interpTargetX - this.posX) / (double) this.interpProgress;
            double interpolatedY = this.posY + (this.interpTargetY - this.posY) / (double) this.interpProgress;
            double interpolatedZ = this.posZ + (this.interpTargetZ - this.posZ) / (double) this.interpProgress;
            double deltaYaw = MathHelper.wrapDegrees(this.interpTargetYaw - (double) this.rotationYaw);
            this.rotationYaw = (float) ((double) this.rotationYaw + deltaYaw / (double) this.interpProgress);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.interpTargetPitch - (double) this.rotationPitch) / (double) this.interpProgress);
            this.interpProgress--;
            this.setPosition(interpolatedX, interpolatedY, interpolatedZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }

        for (Map.Entry<Integer, SeatEntity> entry : this.seats.entrySet()) {
            entry.getValue().updateParent(this);
        }

        if (this.world.isRemote) {
            this.updateKeyStates();
        } else if (this.getSeat(0) != null && this.getSeat(0).getControllingPassenger() == null) {
            this.setState((byte) 0);
        }

        if (!this.isInWater()) {
            float moveAmount = 0.0F;

            if ((this.left() || this.right()) && !(this.forward() || this.backward())) {
                moveAmount += 0.05F;
            }

            if (this.forward()) {
                moveAmount += 0.1F;
            } else if (this.backward()) {
                moveAmount -= 0.05F;
            }

            if (this.left()) {
                this.rotationYaw -= 26.0F * moveAmount;
            } else if (this.right()) {
                this.rotationYaw += 26.0F * moveAmount;
            }

            this.motionX += MathHelper.sin(-this.rotationYaw * 0.017453292F) * moveAmount;
            this.motionZ += MathHelper.cos(this.rotationYaw * 0.017453292F) * moveAmount;
        }

        this.motionY -= 0.1F;

        this.motionX *= 0.85F;
        this.motionY *= 0.85F;
        this.motionZ *= 0.85F;

        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        this.prevWheelRotateAmount = this.wheelRotateAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 4.0F;

        if (delta > 1.0F) {
            delta = 1.0F;
        }

        this.wheelRotateAmount += (delta - this.wheelRotateAmount) * 0.4F;
        this.wheelRotation += this.wheelRotateAmount;

        if (!this.world.isRemote) {
            this.dataManager.set(WATCHER_HEALTH, this.health);
        } else {
            this.health = this.dataManager.get(WATCHER_HEALTH);
        }
    }

    private void updateKeyStates() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (this.getSeat(0) != null && player == this.getSeat(0).getControllingPassenger()) {
            MovementInput movementInput = player.movementInput;

            byte previous = this.getState();

            this.left(movementInput.leftKeyDown);
            this.right(movementInput.rightKeyDown);
            this.forward(movementInput.forwardKeyDown);
            this.backward(movementInput.backKeyDown);

            if (this.getState() != previous) {
                JurassiCraft.NETWORK_WRAPPER.sendToServer(new UpdateCarControlMessage(this));
            }
        }
    }

    public boolean left() {
        return (this.dataManager.get(WATCHER_STATE) & 1) == 1;
    }

    public boolean right() {
        return (this.dataManager.get(WATCHER_STATE) >> 1 & 1) == 1;
    }

    public boolean forward() {
        return (this.dataManager.get(WATCHER_STATE) >> 2 & 1) == 1;
    }

    public boolean backward() {
        return (this.dataManager.get(WATCHER_STATE) >> 3 & 1) == 1;
    }

    public void left(boolean left) {
        this.setStateField(0, left);
    }

    public void right(boolean right) {
        this.setStateField(1, right);
    }

    public void forward(boolean forward) {
        this.setStateField(2, forward);
    }

    public void backward(boolean backward) {
        this.setStateField(3, backward);
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = this.dataManager.get(WATCHER_STATE);

        if (newState) {
            this.dataManager.set(WATCHER_STATE, (byte) (prevState | (1 << i)));
        } else {
            this.dataManager.set(WATCHER_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    public byte getState() {
        return this.dataManager.get(WATCHER_STATE);
    }

    public void setState(byte state) {
        this.dataManager.set(WATCHER_STATE, state);
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(WATCHER_STATE, (byte) 0);
        this.dataManager.register(WATCHER_HEALTH, 40.0F);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.health = compound.getFloat("Health");
        this.healAmount = compound.getFloat("HealAmount");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("Health", this.health);
        compound.setFloat("HealAmount", this.healAmount);
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpTargetPitch = pitch;
        this.interpProgress = posRotationIncrements;
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand) {
        Entity pointedEntity = null;

        double reach = 5.0;

        Vec3d look = player.getLook(0.0F);
        Vec3d eyePosition = new Vec3d(player.posX, player.posY + player.eyeHeight, player.posZ);
        Vec3d vec3d2 = eyePosition.addVector(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach);

        List<Entity> entities = this.world.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().addCoord(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach).expand(1.0F, 1.0F, 1.0F), Predicates.and(EntitySelectors.NOT_SPECTATING, entity -> entity != null && entity.canBeCollidedWith() && entity instanceof SeatEntity));
        double distance = reach;

        for (Entity entity : entities) {
            AxisAlignedBB bounds = entity.getEntityBoundingBox().expandXyz((double) entity.getCollisionBorderSize());
            RayTraceResult result = bounds.calculateIntercept(eyePosition, vec3d2);

            if (bounds.isVecInside(eyePosition)) {
                if (distance >= 0.0D) {
                    pointedEntity = entity;
                    distance = 0.0D;
                }
            } else if (result != null) {
                double vecDistance = eyePosition.distanceTo(result.hitVec);

                if (vecDistance < distance || distance == 0.0D) {
                    if (entity.getLowestRidingEntity() == player.getLowestRidingEntity() && !player.canRiderInteract()) {
                        if (distance == 0.0D) {
                            pointedEntity = entity;
                        }
                    } else {
                        pointedEntity = entity;
                        distance = vecDistance;
                    }
                }
            }
        }

        if (pointedEntity != null) {
            return pointedEntity.applyPlayerInteraction(player, vec, stack, hand);
        }

        return EnumActionResult.PASS;
    }

    public void addSeat(SeatEntity entity) {
        this.seats.put(entity.getId(), entity);
    }

    public Entity getSeat(int id) {
        return this.seats.get(id);
    }

    @Override
    public void setDead() {
        super.setDead();

        if (!this.world.isRemote) {
            if (!this.droppedItems) {
                this.dropItems();
                this.droppedItems = true;
            }
        } else {
            this.updateSound();
        }
    }

    private void updateSound() {
        if (!this.isDead) {
            this.sound = new CarSound(this);
            ClientProxy.playSound(this);
        } else {
            ClientProxy.stopSound(this);
        }
    }

    public abstract void dropItems();
}
