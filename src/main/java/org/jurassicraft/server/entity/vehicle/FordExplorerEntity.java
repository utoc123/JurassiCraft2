package org.jurassicraft.server.entity.vehicle;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Locale;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;

import org.jurassicraft.server.block.TourRailBlock;

public class FordExplorerEntity extends CarEntity {
    private static final double SQRT_2 = Math.sqrt(2);

    private static final double SQRT_2_OVER_2 = SQRT_2 / 2;

    private static final float LENGTH = 4.5F;

    private static final float RAIL_HEIGHT = 0.15F;

    private State state = new StateOffRail();

    private double headX, headY, headZ;

    private double tailX, tailY, tailZ;

    private final float speed = 0.3F;

    public FordExplorerEntity(World world) {
        super(world);
    }

    private void setState(State state) {
        this.state = state;
    }

    @Override
    public Entity getControllingPassenger() {
        return this.state.getControllingPassenger();
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (!this.world.isRemote) {
            this.state.update();
        }
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        this.setBodyToPos();
    }

    public void pull() {
        double deltaX = this.tailX - this.headX;
        double deltaY = this.tailY - this.headY;
        double deltaZ = this.tailZ - this.headZ;
        double len = FordExplorerEntity.LENGTH / Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        this.tailX = this.headX + deltaX * len;
        this.tailY = this.headY + deltaY * len;
        this.tailZ = this.headZ + deltaZ * len;
        this.setPosToBody();
    }

    private void setBodyToPos() {
        Vec3d v = getVectorForRotation(this.rotationPitch, this.rotationYaw);
        double dx = v.xCoord * (FordExplorerEntity.LENGTH / 2);
        double dy = v.yCoord * (FordExplorerEntity.LENGTH / 2);
        double dz = v.zCoord * (FordExplorerEntity.LENGTH / 2);
        this.setHead(this.posX + dx, this.posY + dy, this.posZ + dz);
        this.tailX = this.posX - dx;
        this.tailY = this.posY - dy;
        this.tailZ = this.posZ - dz;
    }

    public void setHead(double headX, double headY, double headZ) {
        this.headX = headX;
        this.headY = headY;
        this.headZ = headZ;
        if (this.state != null) { // state only null during Entity#<init> setPosition
            BlockPos pos = new BlockPos(headX, headY, headZ);
            if (this.world.isBlockLoaded(pos)) {
                if (this.state.isRail(this.world.getBlockState(pos.down()))) {
                    this.headY -= 0.5;
                } else if (!this.state.isRail(this.world.getBlockState(pos)) && this.state.isRail(this.world.getBlockState(pos.up()))) {
                    this.headY += 0.5;
                }
            }
            if (!this.world.isRemote) {
                this.state.onMove(new BlockPos(this.headX, this.headY, this.headZ));
            }
        }
    }

    private void setPosToBody() {
        double dx = this.tailX - this.headX;
        double dy = this.tailY - this.headY;
        double dz = this.tailZ - this.headZ;
        float yaw = (float) Math.toDegrees(MathHelper.atan2(dz, dx)) + 90;
        float pitch = (float) Math.toDegrees(MathHelper.atan2(dy, MathHelper.sqrt(dx * dx + dz * dz)));
        this.setRotation(yaw, pitch);
        super.setPosition((this.headX + this.tailX) / 2, (this.headY + this.tailY) / 2 - FordExplorerEntity.RAIL_HEIGHT, (this.headZ + this.tailZ) / 2);
    }

    @Override
    public void dropItems() {
        //this.dropItem(ItemHandler.FORD_EXPLORER, 1);
    }

    protected Seat[] createSeats() {
        Seat frontLeft = new Seat(0, 0.563F, 0.35F, 0.35F, 0.5F, 0.25F);
        Seat frontRight = new Seat(1, -0.563F, 0.35F, 0.35F, 0.5F, 0.25F);
        Seat backLeft = new Seat(2, 0.5F, 0.35F, -0.97F, 0.4F, 0.25F);
        Seat backRight = new Seat(3, -0.5F, 0.35F, -0.97F, 0.4F, 0.25F);
        return new Seat[] { frontLeft, frontRight, backLeft, backRight };
    }

    private abstract class State {
        private BlockPos prevHead;

        private void onMove(BlockPos pos) {
            if (FordExplorerEntity.this.world.isBlockLoaded(pos)) {
                IBlockState state = FordExplorerEntity.this.world.getBlockState(pos);
                if (this.applies(this.isRail(state))) {
                    if (!pos.equals(this.prevHead)) {
                        this.onMove(pos, state);
                    }
                } else {
                    FordExplorerEntity.this.setState(this.getNextState(pos, state));
                }
                this.prevHead = pos;
            } else {
                this.prevHead = null;
            }
        }

        boolean isRail(IBlockState state) {
            return state.getBlock() instanceof TourRailBlock;
        }

        protected abstract Entity getControllingPassenger();

        protected abstract boolean applies(boolean isRail);

        protected abstract State getNextState(BlockPos pos, IBlockState state);

        protected void onMove(BlockPos pos, IBlockState state) {}

        protected void update() {}
    }

    private final class StateOffRail extends State {
        @Override
        protected Entity getControllingPassenger() {
            return FordExplorerEntity.super.getControllingPassenger();
        }

        @Override
        protected boolean applies(boolean isRail) {
            return !isRail;
        }

        @Override
        protected State getNextState(BlockPos pos, IBlockState state) {
            return new StateOnRail(pos, state);
        }

        @Override
        protected void update() {
            FordExplorerEntity.this.noClip = false;
            FordExplorerEntity.this.setBodyToPos();
        }
    }

    private final class StateOnRail extends State {
        private BlockPos pos;

        private OrientatedRail rail;

        private Direction direction;

        private float position;

        private StateOnRail(BlockPos pos, IBlockState state) {
            this.setPosition(pos, state);
            this.direction = Direction.FORWARD;
        }

        private OrientatedRail setPosition(BlockPos pos, IBlockState state) {
            this.pos = pos;
            this.rail = OrientatedRail.get(state);
            return this.rail;
        }

        @Override
        protected Entity getControllingPassenger() {
            return null;
        }

        @Override
        protected boolean applies(boolean isRail) {
            return isRail;
        }

        @Override
        protected State getNextState(BlockPos pos, IBlockState state) {
            return new StateOffRail();
        }

        @Override
        protected void onMove(BlockPos pos, IBlockState state) {
            OrientatedRail fromRail = this.rail;
            OrientatedRail toRail = this.setPosition(pos, state);
            if (this.direction == Direction.FORWARD ? fromRail.getOutputVector() != toRail.getInputVector() : fromRail.getInputVector() != toRail.getOutputVector()) {
                this.direction = this.direction.opposite();
            }
            if (this.position > fromRail.getLength()) {
                this.position -= fromRail.getLength();
            } else {
                this.position = 0;
            }
        }

        @Override
        protected void update() {
            FordExplorerEntity.this.noClip = true;
            Vec3d point = this.rail.pointAt(this.direction, this.position);
            this.position += FordExplorerEntity.this.speed;
            double x = this.pos.getX() + point.xCoord;
            double y = this.pos.getY() + point.yCoord;
            double z = this.pos.getZ() + point.zCoord;
            FordExplorerEntity.this.setHead(x, y + FordExplorerEntity.RAIL_HEIGHT, z);
            FordExplorerEntity.this.pull();
        }
    }

    public static final class OrientatedRail {
        private static final Vec3d CENTER = new Vec3d(0.5, 0, 0.5);

        private final RailShape shape;

        private final AxisRotation rotation;

        private OrientatedRail(RailShape shape, AxisRotation rotation) {
            this.shape = shape;
            this.rotation = rotation;
        }

        private EnumFacing getInputVector() {
            return this.rotation.apply(this.shape.getInput());
        }

        private EnumFacing getOutputVector() {
            return this.rotation.apply(this.shape.getOutput());
        }

        private Vec3d pointAt(Direction direction, double distance) {
            double d = direction.apply(this.shape.getLength(), distance);
            return OrientatedRail.CENTER.add(this.rotation.apply(this.shape.pointAt(d)));
        }

        public double getLength() {
            return this.shape.getLength();
        }

        @Override
        public String toString() {
            return String.format("%s rail rotated %s", this.shape.name().toLowerCase(Locale.ROOT), this.rotation.name().toLowerCase(Locale.ROOT));
        }

        public static OrientatedRail get(IBlockState state) {
            BlockRailBase.EnumRailDirection stateShape = state.getValue(TourRailBlock.SHAPE);
            RailShape shape;
            AxisRotation rotation;
            switch (stateShape) {
                case NORTH_SOUTH:
                    shape = RailShape.STRAIGHT;
                    rotation = AxisRotation.NONE;
                    break;
                case EAST_WEST:
                    shape = RailShape.STRAIGHT;
                    rotation = AxisRotation.COUNTERCLOCKWISE_90;
                    break;
                case ASCENDING_EAST:
                    shape = RailShape.ASCENDING;
                    rotation = AxisRotation.CLOCKWISE_90;
                    break;
                case ASCENDING_WEST:
                    shape = RailShape.ASCENDING;
                    rotation = AxisRotation.COUNTERCLOCKWISE_90;
                    break;
                case ASCENDING_NORTH:
                    shape = RailShape.ASCENDING;
                    rotation = AxisRotation.CLOCKWISE_180;
                    break;
                case ASCENDING_SOUTH:
                    shape = RailShape.ASCENDING;
                    rotation = AxisRotation.NONE;
                    break;
                case SOUTH_EAST:
                    shape = RailShape.CURVE;
                    rotation = AxisRotation.CLOCKWISE_180;
                    break;
                case SOUTH_WEST:
                    shape = RailShape.CURVE;
                    rotation = AxisRotation.CLOCKWISE_90;
                    break;
                case NORTH_WEST:
                    shape = RailShape.CURVE;
                    rotation = AxisRotation.NONE;
                    break;
                case NORTH_EAST:
                    shape = RailShape.CURVE;
                    rotation = AxisRotation.COUNTERCLOCKWISE_90;
                    break;
                default:
                    throw new IllegalStateException();
            }
            return new OrientatedRail(shape, rotation);
        }
    }

    public enum Direction {
        FORWARD((l, v) -> v) {
            @Override
            public Direction opposite() {
                return BACKWARD;
            }
        },
        BACKWARD((l, v) -> l - v) {
            @Override
            public Direction opposite() {
                return FORWARD;
            }
        };

        private final DoubleBinaryOperator function;

        Direction(DoubleBinaryOperator function) {
            this.function = function;
        }

        public final double apply(double length, double value) {
            return this.function.applyAsDouble(length, value);
        }

        public abstract Direction opposite();
    }

    private enum RailShape {
        STRAIGHT(d -> new Vec3d(0, 0, d - 0.5), 1, EnumFacing.SOUTH),
        ASCENDING(d -> new Vec3d(0, d * SQRT_2_OVER_2, d * SQRT_2_OVER_2 - 0.5), SQRT_2, EnumFacing.SOUTH),
        CURVE(d -> new Vec3d(Math.cos(d) / 2 - 0.5, 0, Math.sin(d) / 2 - 0.5), Math.PI / 2, EnumFacing.SOUTH, EnumFacing.EAST);

        private final DoubleFunction<Vec3d> curve;

        private final double length;

        private final EnumFacing input;

        private final EnumFacing output;

        RailShape(DoubleFunction<Vec3d> curve, double length, EnumFacing input) {
            this(curve, length, input, input);
        }

        RailShape(DoubleFunction<Vec3d> curve, double length, EnumFacing input, EnumFacing output) {
            this.curve = curve;
            this.length = length;
            this.input = input;
            this.output = output;
        }

        public final Vec3d pointAt(double distance) {
            return this.curve.apply(distance);
        }

        public final double getLength() {
            return this.length;
        }

        public final EnumFacing getInput() {
            return input;
        }

        public final EnumFacing getOutput() {
            return output;
        }
    }

    private enum AxisRotation {
        NONE(1, 0, 0, 1),
        CLOCKWISE_90(0, 1, -1, 0),
        CLOCKWISE_180(-1, 0, 0, -1),
        COUNTERCLOCKWISE_90(0, -1, 1, 0);

        private final double xx, xz, zx, zz;

        AxisRotation(double xx, double xz, double zx, double zz) {
            this.xx = xx;
            this.xz = xz;
            this.zx = zx;
            this.zz = zz;
        }

        public final double getX(double x, double z) {
            return x * this.xx + z * this.xz;
        }

        public final double getZ(double x, double z) {
            return x * this.zx + z * this.zz;
        }

        public final Vec3d apply(Vec3d vec) {
            return new Vec3d(this.getX(vec.xCoord, vec.zCoord), vec.yCoord, this.getZ(vec.xCoord, vec.zCoord));
        }

        public final EnumFacing apply(EnumFacing facing) {
            return EnumFacing.getHorizontal(facing.getHorizontalIndex() + this.ordinal());
        }
    }
}
