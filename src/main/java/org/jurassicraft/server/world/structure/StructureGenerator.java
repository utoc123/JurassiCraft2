package org.jurassicraft.server.world.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public abstract class StructureGenerator extends WorldGenerator {
    protected Rotation rotation;
    protected Mirror mirror;
    protected int horizontalPos;
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    protected StructureGenerator(Random rand, int sizeX, int sizeY, int sizeZ) {
        this.horizontalPos = -1;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        Rotation[] rotations = Rotation.values();
        this.rotation = rotations[rand.nextInt(rotations.length)];
        Mirror[] mirrors = Mirror.values();
        this.mirror = mirrors[rand.nextInt(mirrors.length)];
    }

    protected BlockPos placeOnGround(World world, BlockPos pos, int yOffset) {
        if (this.horizontalPos >= 0) {
            return new BlockPos(pos.getX(), this.horizontalPos, pos.getZ());
        } else {
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;
            BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos();
            BlockPos corner = this.transformPos(new BlockPos(this.sizeX, 0, this.sizeZ), this.mirror, this.rotation);
            for (int z = 0; z <= corner.getZ(); ++z) {
                for (int x = 0; x <= corner.getX(); ++x) {
                    currentPos.setPos(x + pos.getX(), 64, z + pos.getZ());
                    int level = Math.max(this.getGround(world, currentPos).getY(), world.provider.getAverageGroundLevel());
                    if (level < minHeight) {
                        minHeight = level;
                    }
                    if (level > maxHeight) {
                        maxHeight = level;
                    }
                }
            }
            if (maxHeight - minHeight > 5) {
                return null;
            }
            this.horizontalPos = minHeight + yOffset;
            return new BlockPos(pos.getX(), this.horizontalPos, pos.getZ());
        }
    }

    protected BlockPos getGround(World world, BlockPos pos) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        BlockPos currentPos;
        BlockPos ground;
        for (currentPos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); currentPos.getY() >= 0; currentPos = ground) {
            ground = currentPos.down();
            IBlockState state = chunk.getBlockState(ground);
            Block block = state.getBlock();
            if (state.getMaterial().blocksMovement() && state.isFullBlock() && !block.isLeaves(state, world, ground) && !block.isFoliage(world, ground) && !block.isWood(world, ground)) {
                break;
            }
        }
        return currentPos;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos position) {
        position = this.placeOnGround(world, position, -4);
        if (position != null) {
            this.generateStructure(world, random, position);
            return true;
        }
        return false;
    }

    protected BlockPos transformPos(BlockPos pos, Mirror mirror, Rotation rotation) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        boolean mirrored = true;
        switch (mirror) {
            case LEFT_RIGHT:
                z = -z;
                break;
            case FRONT_BACK:
                x = -x;
                break;
            default:
                mirrored = false;
        }
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(z, y, -x);
            case CLOCKWISE_90:
                return new BlockPos(-z, y, x);
            case CLOCKWISE_180:
                return new BlockPos(-x, y, -z);
            default:
                return mirrored ? new BlockPos(x, y, z) : pos;
        }
    }

    protected abstract void generateStructure(World world, Random random, BlockPos position);
}
