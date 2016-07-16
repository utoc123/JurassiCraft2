package org.jurassicraft.server.entity.vehicle.modules;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
    private float offsetX, offsetY, offsetZ;

    private int parentId;
    private CarEntity parent;

    public SeatEntity(World world)
    {
        super(world);
    }

    public SeatEntity(World world, CarEntity parent, int id, float offsetX, float offsetY, float offsetZ, float width, float height)
    {
        super(world);
        this.setSize(width, height + offsetY);
        this.id = id;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.updateParent(parent);
        this.updatePosition();
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (parent == null && !worldObj.isRemote)
        {
            this.setDead();
            return;
        }
        else if (parent != null)
        {
            this.updatePosition();
        }

        parent = null;
    }

    private void updatePosition()
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
        transform.setTranslation(new Vector3d(offsetX, 0.0, offsetZ));
        matrix.mul(transform);

        this.setPosition(matrix.m03, matrix.m13, matrix.m23);

        this.prevRotationYaw = parent.prevRotationYaw;
        this.rotationYaw = parent.rotationYaw;
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

    public void updateParent(CarEntity parent)
    {
        this.parent = parent;
        this.parentId = parent.getEntityId();
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

        if (this.isPassenger(passenger))
        {
            passenger.rotationYaw += this.rotationYaw - this.prevRotationYaw;
            applyPassengerRotation(passenger);
        }
    }

    protected void applyPassengerRotation(Entity passenger)
    {
        passenger.setRenderYawOffset(this.rotationYaw);

        if (passenger instanceof EntityLivingBase)
        {
            ((EntityLivingBase) passenger).prevRenderYawOffset = this.prevRotationYaw;
        }

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
        buffer.writeByte(id);
        buffer.writeFloat(offsetX);
        buffer.writeFloat(offsetY);
        buffer.writeFloat(offsetZ);
        buffer.writeFloat(width);
        buffer.writeFloat(height);
        buffer.writeInt(parentId);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        id = buffer.readByte();
        offsetX = buffer.readFloat();
        offsetY = buffer.readFloat();
        offsetZ = buffer.readFloat();
        setSize(buffer.readFloat(), buffer.readFloat());

        Entity parent = worldObj.getEntityByID(buffer.readInt());

        if (parent instanceof CarEntity)
        {
            this.parent = (CarEntity) parent;
            this.parent.addSeat(this);
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
    }

    @Override
    public double getMountedYOffset()
    {
        return offsetY;
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
}
