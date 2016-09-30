package org.jurassicraft.server.entity.ai.navigate;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import org.jurassicraft.server.block.BlockHandler;

import java.util.EnumSet;
import java.util.Set;

public class ClimbNodeProcessor extends NodeProcessor {
    private float avoidsWater;

    @Override
    public void initProcessor(IBlockAccess source, EntityLiving entity) {
        super.initProcessor(source, entity);
        this.avoidsWater = entity.getPathPriority(PathNodeType.WATER);
    }

    @Override
    public void postProcess() {
        this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
        super.postProcess();
    }

    @Override
    public PathPoint getStart() {
        int y;
        AxisAlignedBB entityBounds = this.entity.getEntityBoundingBox();
        if (this.getCanSwim() && this.entity.isInWater()) {
            y = (int) entityBounds.minY;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor_double(this.entity.posX), y, MathHelper.floor_double(this.entity.posZ));
            for (Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock()) {
                ++y;
                blockpos$mutableblockpos.setPos(MathHelper.floor_double(this.entity.posX), y, MathHelper.floor_double(this.entity.posZ));
            }
        } else if (this.entity.onGround) {
            y = MathHelper.floor_double(entityBounds.minY + 0.5D);
        } else {
            BlockPos pos = new BlockPos(this.entity);
            while ((this.blockaccess.getBlockState(pos).getMaterial() == Material.AIR || this.blockaccess.getBlockState(pos).getBlock().isPassable(this.blockaccess, pos)) && pos.getY() > 0) {
                pos = pos.down();
            }
            y = pos.up().getY();
        }
        BlockPos entityPos = new BlockPos(this.entity);
        PathNodeType nodeType = this.getPathNodeType(this.entity, entityPos.getX(), y, entityPos.getZ());
        if (this.entity.getPathPriority(nodeType) < 0.0F) {
            Set<BlockPos> possibleNodes = Sets.newHashSet();
            possibleNodes.add(new BlockPos(entityBounds.minX, y, entityBounds.minZ));
            possibleNodes.add(new BlockPos(entityBounds.minX, y, entityBounds.maxZ));
            possibleNodes.add(new BlockPos(entityBounds.maxX, y, entityBounds.minZ));
            possibleNodes.add(new BlockPos(entityBounds.maxX, y, entityBounds.maxZ));
            for (BlockPos node : possibleNodes) {
                PathNodeType pathnodetype = this.getPathNodeType(this.entity, node);
                if (this.entity.getPathPriority(pathnodetype) >= 0.0F) {
                    return this.openPoint(node.getX(), node.getY(), node.getZ());
                }
            }
        }
        return this.openPoint(entityPos.getX(), y, entityPos.getZ());
    }

    @Override
    public PathPoint getPathPointToCoords(double x, double y, double z) {
        return this.openPoint(MathHelper.floor_double(x - (double) (this.entity.width / 2.0F)), MathHelper.floor_double(y), MathHelper.floor_double(z - (double) (this.entity.width / 2.0F)));
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int option = 0;
        int maxStepY = 0;
        PathNodeType currentNodeType = this.getPathNodeType(this.entity, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord);
        if (this.entity.getPathPriority(currentNodeType) >= 0.0F) {
            maxStepY = MathHelper.floor_float(Math.max(1.0F, 20));
        }
        PathPoint pointSouth = this.getSafePoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, maxStepY, EnumFacing.SOUTH);
        PathPoint pointWest = this.getSafePoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, maxStepY, EnumFacing.WEST);
        PathPoint pointEast = this.getSafePoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, maxStepY, EnumFacing.EAST);
        PathPoint pointNorth = this.getSafePoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, maxStepY, EnumFacing.NORTH);
        if (pointSouth != null && !pointSouth.visited && pointSouth.distanceTo(targetPoint) < maxDistance) {
            pathOptions[option++] = pointSouth;
        }
        if (pointWest != null && !pointWest.visited && pointWest.distanceTo(targetPoint) < maxDistance) {
            pathOptions[option++] = pointWest;
        }
        if (pointEast != null && !pointEast.visited && pointEast.distanceTo(targetPoint) < maxDistance) {
            pathOptions[option++] = pointEast;
        }
        if (pointNorth != null && !pointNorth.visited && pointNorth.distanceTo(targetPoint) < maxDistance) {
            pathOptions[option++] = pointNorth;
        }
        boolean travelNorth = pointNorth == null || pointNorth.nodeType == PathNodeType.OPEN || pointNorth.costMalus != 0.0F;
        boolean travelSouth = pointSouth == null || pointSouth.nodeType == PathNodeType.OPEN || pointSouth.costMalus != 0.0F;
        boolean travelEast = pointEast == null || pointEast.nodeType == PathNodeType.OPEN || pointEast.costMalus != 0.0F;
        boolean travelWest = pointWest == null || pointWest.nodeType == PathNodeType.OPEN || pointWest.costMalus != 0.0F;
        if (travelNorth && travelWest) {
            PathPoint northWest = this.getSafePoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord - 1, maxStepY, EnumFacing.NORTH);
            if (northWest != null && !northWest.visited && northWest.distanceTo(targetPoint) < maxDistance) {
                pathOptions[option++] = northWest;
            }
        }
        if (travelNorth && travelEast) {
            PathPoint northEast = this.getSafePoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord - 1, maxStepY, EnumFacing.NORTH);
            if (northEast != null && !northEast.visited && northEast.distanceTo(targetPoint) < maxDistance) {
                pathOptions[option++] = northEast;
            }
        }
        if (travelSouth && travelWest) {
            PathPoint southWest = this.getSafePoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord + 1, maxStepY, EnumFacing.SOUTH);
            if (southWest != null && !southWest.visited && southWest.distanceTo(targetPoint) < maxDistance) {
                pathOptions[option++] = southWest;
            }
        }
        if (travelSouth && travelEast) {
            PathPoint southEast = this.getSafePoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord + 1, maxStepY, EnumFacing.SOUTH);
            if (southEast != null && !southEast.visited && southEast.distanceTo(targetPoint) < maxDistance) {
                pathOptions[option++] = southEast;
            }
        }
        return option;
    }

    private PathPoint getSafePoint(int x, int y, int z, int maxStepY, EnumFacing direction) {
        PathPoint point = null;
        BlockPos currentPos = new BlockPos(x, y, z);
        PathNodeType nodeType = this.getPathNodeType(this.entity, x, y, z);
        float priority = this.entity.getPathPriority(nodeType);
        double d1 = (double) this.entity.width / 2.0D;
        if (priority >= 0.0F) {
            point = this.openPoint(x, y, z);
            point.nodeType = nodeType;
            point.costMalus = Math.max(point.costMalus, priority);
        }
        if (nodeType == PathNodeType.WALKABLE) {
            return point;
        } else {
            if (point == null && maxStepY > 0 && nodeType != PathNodeType.FENCE && nodeType != PathNodeType.TRAPDOOR) {
                point = this.getSafePoint(x, y + 1, z, maxStepY - 1, direction);
                if (point != null && (point.nodeType == PathNodeType.OPEN || point.nodeType == PathNodeType.WALKABLE) && this.entity.width < 1.0F) {
                    double d2 = (x - direction.getFrontOffsetX()) + 0.5;
                    double d3 = (z - direction.getFrontOffsetZ()) + 0.5;
                    AxisAlignedBB axisalignedbb = new AxisAlignedBB(d2 - d1, y + 0.001, d3 - d1, d2 + d1, y + this.entity.height, d3 + d1);
                    AxisAlignedBB axisalignedbb1 = this.blockaccess.getBlockState(currentPos).getBoundingBox(this.blockaccess, currentPos);
                    AxisAlignedBB axisalignedbb2 = axisalignedbb.addCoord(0.0, axisalignedbb1.maxY - 0.002, 0.0);
                    if (this.entity.worldObj.collidesWithAnyBlock(axisalignedbb2)) {
                        point = null;
                    }
                }
            }
            if (nodeType == PathNodeType.OPEN) {
                AxisAlignedBB axisalignedbb3 = new AxisAlignedBB((double) x - d1 + 0.5D, (double) y + 0.001D, (double) z - d1 + 0.5D, (double) x + d1 + 0.5D, (double) ((float) y + this.entity.height), (double) z + d1 + 0.5D);
                if (this.entity.worldObj.collidesWithAnyBlock(axisalignedbb3)) {
                    return null;
                }
                if (this.entity.width >= 1.0F) {
                    PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, x, y - 1, z);
                    if (pathnodetype1 == PathNodeType.BLOCKED) {
                        point = this.openPoint(x, y, z);
                        point.nodeType = PathNodeType.WALKABLE;
                        point.costMalus = Math.max(point.costMalus, priority);
                        return point;
                    }
                }
                int fallHeight = 0;
                while (y > 0 && nodeType == PathNodeType.OPEN) {
                    --y;
                    if (fallHeight++ >= this.entity.getMaxFallHeight()) {
                        return null;
                    }
                    nodeType = this.getPathNodeType(this.entity, x, y, z);
                    priority = this.entity.getPathPriority(nodeType);
                    if (nodeType != PathNodeType.OPEN && priority >= 0.0F) {
                        point = this.openPoint(x, y, z);
                        point.nodeType = nodeType;
                        point.costMalus = Math.max(point.costMalus, priority);
                        break;
                    }
                    if (priority < 0.0F) {
                        return null;
                    }
                }
            }
            return point;
        }
    }

    @Override
    public PathNodeType getPathNodeType(IBlockAccess access, int x, int y, int z, EntityLiving entity, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
        EnumSet<PathNodeType> nodeTypes = EnumSet.noneOf(PathNodeType.class);
        PathNodeType currentNodeType = PathNodeType.BLOCKED;
        BlockPos blockpos = new BlockPos(entity);
        for (int offsetX = 0; offsetX < xSize; ++offsetX) {
            for (int offsetY = 0; offsetY < ySize; ++offsetY) {
                for (int offsetZ = 0; offsetZ < zSize; ++offsetZ) {
                    int actualX = offsetX + x;
                    int actualY = offsetY + y;
                    int actualZ = offsetZ + z;
                    PathNodeType actualNodeType = this.getPathNodeType(access, actualX, actualY, actualZ);
                    if (actualNodeType == PathNodeType.DOOR_WOOD_CLOSED && canBreakDoorsIn && canEnterDoorsIn) {
                        actualNodeType = PathNodeType.WALKABLE;
                    }
                    if (actualNodeType == PathNodeType.DOOR_OPEN && !canEnterDoorsIn) {
                        actualNodeType = PathNodeType.BLOCKED;
                    }
                    if (actualNodeType == PathNodeType.RAIL && !(access.getBlockState(blockpos).getBlock() instanceof BlockRailBase) && !(access.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase)) {
                        actualNodeType = PathNodeType.FENCE;
                    }
                    if (offsetX == 0 && offsetY == 0 && offsetZ == 0) {
                        currentNodeType = actualNodeType;
                    }
                    nodeTypes.add(actualNodeType);
                }
            }
        }
        if (nodeTypes.contains(PathNodeType.FENCE)) {
            return PathNodeType.FENCE;
        } else {
            PathNodeType bestNodeType = PathNodeType.BLOCKED;
            for (PathNodeType nodeType : nodeTypes) {
                if (entity.getPathPriority(nodeType) < 0.0F) {
                    return nodeType;
                }
                if (entity.getPathPriority(nodeType) >= entity.getPathPriority(bestNodeType)) {
                    bestNodeType = nodeType;
                }
            }
            if (currentNodeType == PathNodeType.OPEN && entity.getPathPriority(bestNodeType) == 0.0F) {
                return PathNodeType.OPEN;
            } else {
                return bestNodeType;
            }
        }
    }

    private PathNodeType getPathNodeType(EntityLiving entity, BlockPos pos) {
        return this.getPathNodeType(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    private PathNodeType getPathNodeType(EntityLiving entity, int x, int y, int z) {
        return this.getPathNodeType(this.blockaccess, x, y, z, entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanBreakDoors(), this.getCanEnterDoors());
    }

    @Override
    public PathNodeType getPathNodeType(IBlockAccess access, int x, int y, int z) {
        PathNodeType type = this.getPathNodeTypeRaw(access, x, y, z);
        if (type == PathNodeType.OPEN && y >= 1) {
            Block block = access.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            PathNodeType groundType = this.getPathNodeTypeRaw(access, x, y - 1, z);
            type = groundType != PathNodeType.WALKABLE && groundType != PathNodeType.OPEN && groundType != PathNodeType.WATER && groundType != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
            if (groundType == PathNodeType.DAMAGE_FIRE || block == Blocks.field_189877_df) {
                type = PathNodeType.DAMAGE_FIRE;
            }
            if (groundType == PathNodeType.DAMAGE_CACTUS) {
                type = PathNodeType.DAMAGE_CACTUS;
            }
        }
        BlockPos.PooledMutableBlockPos pos = BlockPos.PooledMutableBlockPos.retain();
        if (type == PathNodeType.WALKABLE) {
            for (int offsetX = -1; offsetX <= 1; ++offsetX) {
                for (int offsetZ = -1; offsetZ <= 1; ++offsetZ) {
                    if (offsetX != 0 || offsetZ != 0) {
                        Block block = access.getBlockState(pos.setPos(offsetX + x, y, offsetZ + z)).getBlock();
                        if (block == Blocks.CACTUS) {
                            type = PathNodeType.DANGER_CACTUS;
                        } else if (block == Blocks.FIRE || block == BlockHandler.LOW_SECURITY_FENCE_WIRE || block == BlockHandler.LOW_SECURITY_FENCE_POLE) {
                            type = PathNodeType.DANGER_FIRE;
                        }
                    }
                }
            }
        }
        pos.release();
        return type;
    }

    private PathNodeType getPathNodeTypeRaw(IBlockAccess access, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = access.getBlockState(pos);
        Block block = state.getBlock();
        Material material = state.getMaterial();
        if (material == Material.AIR) {
            return PathNodeType.OPEN;
        } else {
            if (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY) {
                if (block == Blocks.CACTUS || block == BlockHandler.LOW_SECURITY_FENCE_WIRE || block == BlockHandler.LOW_SECURITY_FENCE_POLE) {
                    return PathNodeType.DAMAGE_CACTUS;
                } else {
                    if (block == Blocks.FIRE) {
                        return PathNodeType.DAMAGE_FIRE;
                    } else {
                        if (block instanceof BlockDoor && material == Material.WOOD && !state.getValue(BlockDoor.OPEN)) {
                            return PathNodeType.DOOR_WOOD_CLOSED;
                        } else {
                            if (block instanceof BlockDoor && material == Material.IRON && !state.getValue(BlockDoor.OPEN)) {
                                return PathNodeType.DOOR_IRON_CLOSED;
                            } else {
                                if (block instanceof BlockDoor && state.getValue(BlockDoor.OPEN)) {
                                    return PathNodeType.DOOR_OPEN;
                                } else {
                                    if (block instanceof BlockRailBase) {
                                        return PathNodeType.RAIL;
                                    } else {
                                        if (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || state.getValue(BlockFenceGate.OPEN))) {
                                            if (block.isPassable(access, pos)) {
                                                if (material == Material.WATER) {
                                                    return PathNodeType.WATER;
                                                } else if (material == Material.LAVA) {
                                                    return PathNodeType.LAVA;
                                                } else {
                                                    return PathNodeType.OPEN;
                                                }
                                            } else {
                                                if (material == Material.WATER) {
                                                    return PathNodeType.WATER;
                                                } else if (material == Material.LAVA) {
                                                    return PathNodeType.LAVA;
                                                } else {
                                                    return PathNodeType.BLOCKED;
                                                }
                                            }
                                        } else {
                                            return PathNodeType.FENCE;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                return PathNodeType.TRAPDOOR;
            }
        }
    }
}