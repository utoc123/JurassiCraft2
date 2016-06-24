package org.jurassicraft.server.entity.vehicle.modules;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.CarEntity;

import javax.vecmathimpl.Matrix4d;
import javax.vecmathimpl.Vector3d;
import java.util.List;

public class SeatEntity extends Entity implements IEntityAdditionalSpawnData
{
    private int id;
    private CarEntity parent;
    private float offsetX, offsetY, offsetZ;
    private Controller controller;

    protected int interpProgress;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;

    private boolean hasParent;
    private int parentId;

    private boolean parentUpdating;

    public SeatEntity(World world)
    {
        super(world);
    }

    public SeatEntity(World world, CarEntity parent, int id, float offsetX, float offsetY, float offsetZ, float width, float height, Controller controller)
    {
        super(world);
        this.setSize(width, height);
        this.id = id;
        this.parent = parent;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.controller = controller;
        this.updatePosition();
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (hasParent && parent == null)
        {
            setParent(worldObj.getEntityByID(parentId));
        }

        if ((hasParent && parent == null) || (parent != null && parent.isDead) || (!parentUpdating && !worldObj.isRemote))
        {
            this.setDead();
            return;
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

        if (parent != null)
        {
            updatePosition();
        }
        else
        {
            System.out.println("w0t");
        }

        Entity controllingPassenger = getControllingPassenger();

        if (controller != null && controllingPassenger != null)
        {
            controller.control(controllingPassenger, this);
        }

        parentUpdating = false;
    }

    @Override
    public Entity getControllingPassenger()
    {
        List<Entity> passengers = getPassengers();

        return passengers.size() > 0 ? passengers.get(0) : null;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    public void updatePosition()
    {
        Matrix4d matrix = new Matrix4d();
        matrix.setIdentity();
        Matrix4d transform = new Matrix4d();
        transform.setIdentity();
        transform.setTranslation(new Vector3d(parent.posX, parent.posY, parent.posZ));
        matrix.mul(transform);
        transform.setIdentity();
        transform.rotY(Math.toRadians(180.0F - parent.rotationYaw));
        matrix.mul(transform);
        transform.setIdentity();
        transform.setTranslation(new Vector3d(offsetX, offsetY, offsetZ));
        matrix.mul(transform);
        this.setPositionAndRotation(matrix.m03, matrix.m13, matrix.m23, parent.rotationYaw, 0);
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d hitPosition, ItemStack stack, EnumHand hand)
    {
        if (getControllingPassenger() == null)
        {
            player.startRiding(this);

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    protected void entityInit()
    {
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
    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);

        if (parent != null && this.isPassenger(passenger))
        {
            passenger.rotationYaw += parent.rotationYaw - parent.prevRotationYaw;
            applyPassengerRotation(passenger);
        }
    }

    protected void applyPassengerRotation(Entity passenger)
    {
        passenger.setRenderYawOffset(this.rotationYaw);
        float deltaYaw = MathHelper.wrapDegrees(passenger.rotationYaw - this.rotationYaw);
        float clampedDeltaYaw = MathHelper.clamp_float(deltaYaw, -105.0F, 105.0F);
        passenger.prevRotationYaw += clampedDeltaYaw - deltaYaw;
        passenger.rotationYaw += clampedDeltaYaw - deltaYaw;
        passenger.setRotationYawHead(passenger.rotationYaw);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate)
    {
        this.applyPassengerRotation(entityToUpdate);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        if (parent != null)
        {
            buffer.writeByte(id);
            buffer.writeFloat(offsetX);
            buffer.writeFloat(offsetY);
            buffer.writeFloat(offsetZ);
            buffer.writeFloat(width);
            buffer.writeFloat(height);
            buffer.writeInt(parent.getEntityId());
            hasParent = true;
        }
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        if (buffer.isReadable())
        {
            id = buffer.readByte();
            offsetX = buffer.readFloat();
            offsetY = buffer.readFloat();
            offsetZ = buffer.readFloat();
            setSize(buffer.readFloat(), buffer.readFloat());

            parentId = buffer.readInt();

            Entity parent = worldObj.getEntityByID(parentId);

            setParent(parent);
            hasParent = true;
        }
    }

    private void setParent(Entity parent)
    {
        if (parent instanceof CarEntity)
        {
            this.parent = (CarEntity) parent;
            this.updatePosition();
            this.parent.addSeat(this);
            this.hasParent = true;
            this.parentUpdating = true;
        }
        else if (parent == null)
        {
            this.parent = null;
            this.hasParent = false;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return getEntityBoundingBox();
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
    public double getMountedYOffset()
    {
        return 0.0;
    }

    public int getId()
    {
        return id;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void updateParent()
    {
        parentUpdating = true;
    }

    public interface Controller
    {
        void control(Entity entity, SeatEntity seat);
    }
}
