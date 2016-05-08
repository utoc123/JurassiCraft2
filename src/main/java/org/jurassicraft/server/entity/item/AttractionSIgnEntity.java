package org.jurassicraft.server.entity.item;

import com.google.common.base.Predicate;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.Validate;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.item.ItemHandler;

public class AttractionSignEntity extends EntityHanging implements IEntityAdditionalSpawnData
{
    private static final Predicate<Entity> IS_ATTRACTION_SIGN = entity -> entity instanceof AttractionSignEntity;

    public AttractionSignType type;

    public AttractionSignEntity(World world)
    {
        super(world);
    }

    public AttractionSignEntity(World world, BlockPos pos, EnumFacing side, AttractionSignType type)
    {
        super(world, pos);
        this.type = type;
        this.updateFacingWithBoundingBox(side);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("Type", (byte) this.type.ordinal());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        this.type = AttractionSignType.values()[compound.getByte("Type")];

        super.readEntityFromNBT(compound);
    }

    @Override
    public int getWidthPixels()
    {
        return this.type.sizeX;
    }

    @Override
    public int getHeightPixels()
    {
        return this.type.sizeY / 2;
    }

    @Override
    public void onBroken(Entity entity)
    {
        if (this.worldObj.getGameRules().getBoolean("doTileDrops"))
        {
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) entity;

                if (player.capabilities.isCreativeMode)
                {
                    return;
                }
            }

            this.entityDropItem(new ItemStack(ItemHandler.INSTANCE.attraction_sign, 1, type.ordinal()), 0.0F);
        }
    }

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
    {
        BlockPos locationOffset = new BlockPos(x - this.posX, y - this.posY, z - this.posZ);
        BlockPos newPosition = this.hangingPosition.add(locationOffset);
        this.setPosition(newPosition.getX(), newPosition.getY(), newPosition.getZ());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeByte(type.ordinal());
        buffer.writeLong(hangingPosition.toLong());
        buffer.writeByte(facingDirection.getHorizontalIndex());
    }

    @Override
    public void readSpawnData(ByteBuf buf)
    {
        type = AttractionSignType.values()[buf.readByte()];
        hangingPosition = BlockPos.fromLong(buf.readLong());
        updateFacingWithBoundingBox(EnumFacing.getHorizontal(buf.readByte()));
    }

    @Override
    public boolean onValidSurface()
    {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty())
        {
            return false;
        }

        int width = this.getWidthPixels() / 16;
        int height = this.getHeightPixels() / 16;

        EnumFacing facing = this.facingDirection.rotateYCCW();

        BlockPos pos = this.hangingPosition.offset(facingDirection.getOpposite()).offset(facing, -(width / 2) + 1);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                BlockPos partPos = pos.offset(facing, x).down(y);
                IBlockState state = worldObj.getBlockState(partPos);

                if (!(state.getBlock().isSideSolid(worldObj, partPos, this.facingDirection) && state.getBlock().getMaterial().isSolid()))
                {
                    return false;
                }
            }
        }

        return this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_ATTRACTION_SIGN).isEmpty();
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        BlockPos prevPos = this.hangingPosition;
        this.hangingPosition = new BlockPos(x, y, z);

        if (!this.hangingPosition.equals(prevPos))
        {
            this.updateBoundingBox();
            this.isAirBorne = true;
        }
    }

    @Override
    protected void updateFacingWithBoundingBox(EnumFacing direction)
    {
        Validate.notNull(direction);
        Validate.isTrue(direction.getAxis().isHorizontal());
        this.facingDirection = direction;
        this.prevRotationYaw = this.rotationYaw = (float) (this.facingDirection.getHorizontalIndex() * 90);
        this.updateBoundingBox();
    }

    protected void updateBoundingBox()
    {
        if (this.facingDirection != null)
        {
            double x = this.hangingPosition.getX() + 0.5D;
            double y = this.hangingPosition.getY() + 0.5D;
            double z = this.hangingPosition.getZ() + 0.5D;
            double offsetXZ = this.getWidthPixels() % 32 == 0 ? 0.5D : 0.0D;
            double offsetY = this.getHeightPixels() % 32 == 0 ? 0.5D : 0.0D;
            x -= this.facingDirection.getFrontOffsetX() * 0.46875D;
            z -= this.facingDirection.getFrontOffsetZ() * 0.46875D;
            y += offsetY;
            EnumFacing facing = this.facingDirection.rotateYCCW();
            x += offsetXZ * facing.getFrontOffsetX();
            z += offsetXZ * facing.getFrontOffsetZ();
            this.posX = x;
            this.posY = y;
            this.posZ = z;
            double sizeX = this.getWidthPixels();
            double sizeY = this.getHeightPixels();
            double sizeZ = this.getWidthPixels();

            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z)
            {
                sizeZ = 1.0D;
            }
            else
            {
                sizeX = 1.0D;
            }

            sizeX /= 32.0D;
            sizeY /= 16.0D;
            sizeZ /= 32.0D;

            this.setEntityBoundingBox(new AxisAlignedBB(x - sizeX, y, z - sizeZ, x + sizeX, y - sizeY, z + sizeZ));
        }
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return new ItemStack(ItemHandler.INSTANCE.attraction_sign, 1, this.type.ordinal());
    }

    public enum AttractionSignType
    {
        AQUARIUM,
        AQUARIUM_CORAL,
        AVIARY,
        AVIARY_PLANTS,
        GALLIMIMUS_VALLEY,
        GALLIMIMUS_VALLEY_PLANTS,
        GENTLE_GIANTS,
        GENTLE_GIANTS_PLANTS;

        public final int sizeX;
        public final int sizeY;
        public final ResourceLocation texture;
        public final ResourceLocation texturePopout;

        AttractionSignType()
        {
            this(128, 128);
        }

        AttractionSignType(int xSize, int ySize)
        {
            this.sizeX = xSize;
            this.sizeY = ySize;
            this.texture = new ResourceLocation(JurassiCraft.MODID, "textures/attraction_sign/" + this.name().toLowerCase() + ".png");
            this.texturePopout = new ResourceLocation(JurassiCraft.MODID, "textures/attraction_sign/" + this.name().toLowerCase() + "_popout.png");
        }
    }
}
