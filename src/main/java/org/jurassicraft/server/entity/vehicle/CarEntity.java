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
import org.jurassicraft.client.sound.CarSound;
import org.jurassicraft.server.entity.vehicle.modules.SeatEntity;
import org.jurassicraft.server.message.UpdateCarControlMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CarEntity extends Entity
{
    private Map<Integer, SeatEntity> seats = new HashMap<>();

    public static final DataParameter<Byte> WATCHER_STATE = EntityDataManager.createKey(CarEntity.class, DataSerializers.BYTE);

    protected int interpProgress;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;

    public float wheelRotation;
    public float wheelRotateAmount;
    public float prevWheelRotateAmount;

    @SideOnly(Side.CLIENT)
    private CarSound sound;

    public CarEntity(World world)
    {
        super(world);
        this.setSize(3.0F, 2.5F);

        this.stepHeight = 1.0F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!isEntityInvulnerable(source))
        {
            this.setDead();
            return true;
        }

        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double dist)
    {
        return true;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (seats.size() == 0)
        {
            if (!worldObj.isRemote)
            {
                addSeat(new SeatEntity(worldObj, this, 0, -0.5F, 0.8F, 0.0F, 1.0F, 1.5F, null));
                addSeat(new SeatEntity(worldObj, this, 1, 0.5F, 0.8F, 0.0F, 1.0F, 1.5F, null));
                addSeat(new SeatEntity(worldObj, this, 2, -0.5F, 1.05F, 2.2F, 1.0F, 1.5F, null));
                addSeat(new SeatEntity(worldObj, this, 3, 0.5F, 1.05F, 2.2F, 1.0F, 1.5F, null));

                for (Map.Entry<Integer, SeatEntity> entry : seats.entrySet())
                {
                    worldObj.spawnEntityInWorld(entry.getValue());
                }
            }
        }

        for (Map.Entry<Integer, SeatEntity> entry : seats.entrySet())
        {
            entry.getValue().updateParent();
        }

        if (this.interpProgress > 0)
        {
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

        if (worldObj.isRemote)
        {
            updateKeyStates();
        }
        else if (getSeat(0) != null && getSeat(0).getControllingPassenger() == null)
        {
            setState((byte) 0);
        }

        float moveAmount = 0.0F;

        if ((left() || right()) && !(forward() || backward()))
        {
            moveAmount += 0.05F;
        }

        if (forward())
        {
            moveAmount += 0.1F;
        }
        else if (backward())
        {
            moveAmount -= 0.05F;
        }

        if (left())
        {
            this.rotationYaw -= 26.0F * moveAmount;
        }
        else if (right())
        {
            this.rotationYaw += 26.0F * moveAmount;
        }

        this.motionX += MathHelper.sin(-this.rotationYaw * 0.017453292F) * moveAmount;
        this.motionZ += MathHelper.cos(this.rotationYaw * 0.017453292F) * moveAmount;

        motionY -= 0.1F;

        motionX *= 0.85F;
        motionY *= 0.85F;
        motionZ *= 0.85F;

        moveEntity(motionX, motionY, motionZ);

        this.prevWheelRotateAmount = this.wheelRotateAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        float delta = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ) * 4.0F;

        if (delta > 1.0F)
        {
            delta = 1.0F;
        }

        this.wheelRotateAmount += (delta - this.wheelRotateAmount) * 0.4F;
        this.wheelRotation += this.wheelRotateAmount;
    }

    private void updateKeyStates()
    {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (getSeat(0) != null && player == getSeat(0).getControllingPassenger())
        {
            MovementInput movementInput = player.movementInput;

            byte previous = getState();

            left(movementInput.leftKeyDown);
            right(movementInput.rightKeyDown);
            forward(movementInput.forwardKeyDown);
            backward(movementInput.backKeyDown);

            if (getState() != previous)
            {
                JurassiCraft.NETWORK_WRAPPER.sendToServer(new UpdateCarControlMessage(this));
            }
        }
    }

    public boolean left()
    {
        return (dataManager.get(WATCHER_STATE) & 1) == 1;
    }

    public boolean right()
    {
        return (dataManager.get(WATCHER_STATE) >> 1 & 1) == 1;
    }

    public boolean forward()
    {
        return (dataManager.get(WATCHER_STATE) >> 2 & 1) == 1;
    }

    public boolean backward()
    {
        return (dataManager.get(WATCHER_STATE) >> 3 & 1) == 1;
    }

    public void left(boolean left)
    {
        setStateField(0, left);
    }

    public void right(boolean right)
    {
        setStateField(1, right);
    }

    public void forward(boolean forward)
    {
        setStateField(2, forward);
    }

    public void backward(boolean backward)
    {
        setStateField(3, backward);
    }

    private void setStateField(int i, boolean newState)
    {
        byte prevState = dataManager.get(WATCHER_STATE);

        if (newState)
        {
            dataManager.set(WATCHER_STATE, (byte) (prevState | (1 << i)));
        }
        else
        {
            dataManager.set(WATCHER_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    public byte getState()
    {
        return dataManager.get(WATCHER_STATE);
    }

    public void setState(byte state)
    {
        dataManager.set(WATCHER_STATE, state);
    }

    @Override
    protected void entityInit()
    {
        dataManager.register(WATCHER_STATE, (byte) 0);

        if (worldObj.isRemote)
        {
            updateSound(true);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {

    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpTargetPitch = pitch;
        this.interpProgress = posRotationIncrements;
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand)
    {
        Entity pointedEntity = null;

        double reach = 5.0;

        Vec3d look = player.getLook(0.0F);
        Vec3d eyePosition = player.getPositionEyes(0.0F);
        Vec3d vec3d2 = eyePosition.addVector(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach);

        List<Entity> entities = worldObj.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().addCoord(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach).expand(1.0F, 1.0F, 1.0F), Predicates.and(EntitySelectors.NOT_SPECTATING, entity -> entity != null && entity.canBeCollidedWith() && entity instanceof SeatEntity));
        double distance = reach;

        for (Entity entity : entities)
        {
            AxisAlignedBB bounds = entity.getEntityBoundingBox().expandXyz((double) entity.getCollisionBorderSize());
            RayTraceResult result = bounds.calculateIntercept(eyePosition, vec3d2);

            if (bounds.isVecInside(eyePosition))
            {
                if (distance >= 0.0D)
                {
                    pointedEntity = entity;
                    distance = 0.0D;
                }
            }
            else if (result != null)
            {
                double vecDistance = eyePosition.distanceTo(result.hitVec);

                if (vecDistance < distance || distance == 0.0D)
                {
                    if (entity.getLowestRidingEntity() == player.getLowestRidingEntity() && !player.canRiderInteract())
                    {
                        if (distance == 0.0D)
                        {
                            pointedEntity = entity;
                        }
                    }
                    else
                    {
                        pointedEntity = entity;
                        distance = vecDistance;
                    }
                }
            }
        }

        if (pointedEntity != null)
        {
            return pointedEntity.applyPlayerInteraction(player, vec, stack, hand);
        }

        return EnumActionResult.PASS;
    }

    public void addSeat(SeatEntity entity)
    {
        seats.put(entity.getId(), entity);
    }

    public Entity getSeat(int id)
    {
        return seats.get(id);
    }

    @Override
    public void setDead()
    {
        super.setDead();

        if (!worldObj.isRemote)
        {
            dropItems();
        }
        else
        {
            this.updateSound(false);
        }
    }

    private void updateSound(boolean start)
    {
        if (start)
        {
            sound = new CarSound(this);
            Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        }
        else
        {
            Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
        }
    }

    public abstract void dropItems();
}
