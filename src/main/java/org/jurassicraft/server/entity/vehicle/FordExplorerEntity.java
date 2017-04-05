package org.jurassicraft.server.entity.vehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.vehicle.modules.SeatEntity;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.message.UpdateVehicleControlMessage;

import java.util.HashMap;
import java.util.Map;

public class FordExplorerEntity extends EntityMinecart implements VehicleEntity {
    public static final DataParameter<Byte> WATCHER_STATE = EntityDataManager.createKey(FordExplorerEntity.class, DataSerializers.BYTE);
    public static final DataParameter<Float> WATCHER_HEALTH = EntityDataManager.createKey(FordExplorerEntity.class, DataSerializers.FLOAT);
    public static final float MAX_HEALTH = 40.0F;

    private Map<Integer, SeatEntity> seats = new HashMap<>();

    public float health;

    private float healAmount;
    private int healCooldown = 40;

    private boolean droppedItems;

    public float wheelRotation;
    public float wheelRotateAmount;
    public float prevWheelRotateAmount;

    public FordExplorerEntity(World world) {
        super(world);

        this.setSize(1.0F, 2.5F);
        this.ignoreFrustumCheck = true;

        this.health = MAX_HEALTH;
    }

    @Override
    public Type getType() {
        return Type.RIDEABLE;
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

            if (this.seats.size() == 0) {
                this.addSeats();

                for (Map.Entry<Integer, SeatEntity> entry : this.seats.entrySet()) {
                    this.world.spawnEntity(entry.getValue());
                }
            }
        }

        for (Map.Entry<Integer, SeatEntity> entry : this.seats.entrySet()) {
            entry.getValue().updateParent(this);
        }

        if (!this.world.isRemote) {
            this.dataManager.set(WATCHER_HEALTH, this.health);
        } else {
            this.health = this.dataManager.get(WATCHER_HEALTH);
        }

        Vec3d pos = new Vec3d(this.posX, this.posY, this.posZ);
        if (pos != null) {
            Vec3d back = new Vec3d(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ);

            if (back == null) {
                back = pos;
            }

            if (pos.squareDistanceTo(back) > 0.01) {
                Vec3d delta = back.addVector(-pos.xCoord, -pos.yCoord, -pos.zCoord);

                this.rotationYaw = (float) (Math.atan2(delta.zCoord, delta.xCoord) * 180.0 / Math.PI) + 90;
                this.rotationPitch = (float) (Math.atan(delta.yCoord) * 76.0);

                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }

        if (this.world.isRemote) {
            this.updateKeyStates();
        } else if (this.getSeat(0) != null && this.getSeat(0).getControllingPassenger() == null) {
            this.setState((byte) 0);
        }

        this.prevWheelRotateAmount = this.wheelRotateAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 4.0F;

        if (delta > 1.0F) {
            delta = 1.0F;
        }

        this.wheelRotateAmount += (delta - this.wheelRotateAmount) * 0.4F;
        this.wheelRotation += this.wheelRotateAmount;
    }

    protected void addSeats() {
        this.addSeat(new SeatEntity(this.world, this, 0, -0.5F, 0.8F, -0.5F, 1.0F, 1.5F));
        this.addSeat(new SeatEntity(this.world, this, 1, 0.5F, 0.8F, -0.5F, 1.0F, 1.5F));
        this.addSeat(new SeatEntity(this.world, this, 2, -0.5F, 0.9F, 2.2F, 1.0F, 1.5F));
        this.addSeat(new SeatEntity(this.world, this, 3, 0.5F, 0.9F, 2.2F, 1.0F, 1.5F));
        this.addSeat(new SeatEntity(this.world, this, 4, -0.5F, 0.8F, 1.0F, 1.0F, 1.5F));
        this.addSeat(new SeatEntity(this.world, this, 5, 0.5F, 0.8F, 1.0F, 1.0F, 1.5F));
    }

    private void updateKeyStates() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (this.getSeat(0) != null && player == this.getSeat(0).getControllingPassenger()) {
            MovementInput movementInput = player.movementInput;

            byte previous = this.getState();

            this.forward(movementInput.forwardKeyDown);
            this.fast(movementInput.jump);
            this.slow(movementInput.backKeyDown);
            this.normal(!(movementInput.jump || movementInput.backKeyDown));

            if (this.getState() != previous) {
                JurassiCraft.NETWORK_WRAPPER.sendToServer(new UpdateVehicleControlMessage(this));
            }
        }
    }

    @Override
    public boolean canUseRail() {
        return super.canUseRail();
    }

    @Override
    public boolean isInRangeToRenderDist(double dist) {
        return true;
    }

    @Override
    public void setDead() {
        super.setDead();

        if (!this.world.isRemote) {
            if (!this.droppedItems) {
                this.dropItems();
                this.droppedItems = true;
            }
        }
    }

    public boolean forward() {
        return (this.dataManager.get(WATCHER_STATE) & 1) == 1;
    }

    public boolean fast() {
        return (this.dataManager.get(WATCHER_STATE) >> 1 & 1) == 1;
    }

    public boolean normal() {
        return (this.dataManager.get(WATCHER_STATE) >> 2 & 1) == 1;
    }

    public boolean slow() {
        return (this.dataManager.get(WATCHER_STATE) >> 3 & 1) == 1;
    }

    public void forward(boolean forward) {
        this.setStateField(0, forward);
    }

    public void fast(boolean fast) {
        this.setStateField(1, fast);
    }

    public void normal(boolean normal) {
        this.setStateField(2, normal);
    }

    public void slow(boolean slow) {
        this.setStateField(3, slow);
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = this.dataManager.get(WATCHER_STATE);
        if (newState) {
            this.dataManager.set(WATCHER_STATE, (byte) (prevState | (1 << i)));
        } else {
            this.dataManager.set(WATCHER_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    @Override
    public byte getState() {
        return this.dataManager.get(WATCHER_STATE);
    }

    @Override
    public void setState(byte state) {
        this.dataManager.set(WATCHER_STATE, state);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(WATCHER_STATE, (byte) 0);
        this.dataManager.register(WATCHER_HEALTH, this.health);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.health = compound.getFloat("Health");
        this.healAmount = compound.getFloat("HealAmount");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("Health", this.health);
        compound.setFloat("HealAmount", this.healAmount);
    }

    public void dropItems() {
        this.dropItem(ItemHandler.FORD_EXPLORER, 1);
    }

    @Override
    public double getX() {
        return this.posX;
    }

    @Override
    public double getY() {
        return this.posY;
    }

    @Override
    public double getZ() {
        return this.posZ;
    }

    @Override
    public float getRotationYaw() {
        return this.rotationYaw;
    }

    @Override
    public float getPrevRotationYaw() {
        return this.prevRotationYaw;
    }

    @Override
    public int getVehicleID() {
        return this.getEntityId();
    }

    @Override
    public void addSeat(SeatEntity entity) {
        this.seats.put(entity.getId(), entity);
    }

    @Override
    public boolean attackVehicle(DamageSource source, float amount) {
        return this.attackEntityFrom(source, amount);
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
    public SeatEntity getSeat(int id) {
        return this.seats.get(id);
    }

    public double speed() {
        if (!this.forward()) {
            return 0.0;
        }
        if (this.slow()) {
            return 0.5;
        } else if (this.fast()) {
            return 2.0;
        }
        return 1.0;
    }
}
