package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.DinosaurEntity;

public class SleepEntityAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    protected Path path;
    protected int giveUpTime;

    public SleepEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute() {
        World world = this.dinosaur.worldObj;

        if ((this.dinosaur.onGround || this.dinosaur.isRiding()) && !this.dinosaur.isDead && !world.isRemote && this.dinosaur.shouldSleep() && !this.dinosaur.isSleeping() && this.dinosaur.getStayAwakeTime() <= 0) {
            int range = 8;

            int posX = (int) this.dinosaur.posX;
            int posZ = (int) this.dinosaur.posZ;

            for (int x = posX - range; x < posX + range; x++) {
                for (int z = posZ - range; z < posZ + range; z++) {
                    BlockPos possiblePos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));

                    if (world.isAirBlock(possiblePos) && world.getBlockState(possiblePos.add(0, -1, 0)).getBlock() != Blocks.WATER) {
                        if (this.canFit(possiblePos) && !world.canSeeSky(possiblePos) && this.dinosaur.setSleepLocation(possiblePos, true)) {
                            this.path = this.dinosaur.getNavigator().getPath();
                            return true;
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    private boolean canFit(BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        AxisAlignedBB boundingBox = new AxisAlignedBB(x, y, z, x + this.dinosaur.width, y + this.dinosaur.height, z + this.dinosaur.width);

        return this.dinosaur.worldObj.getCollisionBoxes(this.dinosaur, boundingBox).isEmpty() && this.dinosaur.worldObj.getEntitiesWithinAABBExcludingEntity(this.dinosaur, boundingBox).isEmpty();
    }

    @Override
    public void startExecuting() {
        this.giveUpTime = 400;
    }

    @Override
    public void updateTask() {
        Path currentPath = this.dinosaur.getNavigator().getPath();

        if (this.path != null) {
            PathPoint finalPathPoint = this.path.getFinalPathPoint();

            if (currentPath == null || !currentPath.getFinalPathPoint().equals(finalPathPoint)) {
                Path path = this.dinosaur.getNavigator().getPathToXYZ(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord);
                this.dinosaur.getNavigator().setPath(path, 1.0);
                this.path = path;
            }
        }

        if (this.giveUpTime <= 0 || (this.dinosaur.getStayAwakeTime() <= 0 && (this.path == null || this.path.isFinished()))) {
            this.dinosaur.setSleeping(true);
        }

        this.giveUpTime--;
    }

    @Override
    public boolean continueExecuting() {
        return this.dinosaur != null && !this.dinosaur.isCarcass() && !this.dinosaur.isSleeping() && this.dinosaur.shouldSleep();
    }

    @Override
    public void resetTask() {
        this.dinosaur.setSleeping(true);
    }
}
