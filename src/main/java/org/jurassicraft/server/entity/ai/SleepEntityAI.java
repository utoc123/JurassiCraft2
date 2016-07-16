package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class SleepEntityAI extends EntityAIBase
{
    protected DinosaurEntity dinosaur;

    protected Path path;
    protected int giveUpTime;

    public SleepEntityAI(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        World world = dinosaur.worldObj;

        if ((dinosaur.onGround || dinosaur.isRiding()) && !dinosaur.isDead && !world.isRemote && dinosaur.shouldSleep() && !dinosaur.isSleeping() && dinosaur.getStayAwakeTime() <= 0)
        {
            int range = 8;

            int posX = (int) dinosaur.posX;
            int posZ = (int) dinosaur.posZ;

            for (int x = posX - range; x < posX + range; x++)
            {
                for (int z = posZ - range; z < posZ + range; z++)
                {
                    BlockPos possiblePos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));

                    if (world.isAirBlock(possiblePos) && world.getBlockState(possiblePos.add(0, -1, 0)).getBlock() != Blocks.WATER)
                    {
                        if (canFit(possiblePos) && !world.canSeeSky(possiblePos) && dinosaur.setSleepLocation(possiblePos, true))
                        {
                            path = dinosaur.getNavigator().getPath();
                            return true;
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    private boolean canFit(BlockPos pos)
    {
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        AxisAlignedBB boundingBox = new AxisAlignedBB(x, y, z, x + dinosaur.width, y + dinosaur.height, z + dinosaur.width);

        return dinosaur.worldObj.getCollisionBoxes(dinosaur, boundingBox).isEmpty() && dinosaur.worldObj.getEntitiesWithinAABBExcludingEntity(dinosaur, boundingBox).isEmpty();
    }

    @Override
    public void startExecuting()
    {
        this.giveUpTime = 400;
    }

    @Override
    public void updateTask()
    {
        Path currentPath = dinosaur.getNavigator().getPath();

        if (this.path != null)
        {
            PathPoint finalPathPoint = this.path.getFinalPathPoint();

            if (currentPath == null || !currentPath.getFinalPathPoint().equals(finalPathPoint))
            {
                Path path = dinosaur.getNavigator().getPathToXYZ(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord);
                dinosaur.getNavigator().setPath(path, 1.0);
                this.path = path;
            }
        }

        if (giveUpTime <= 0 || (dinosaur.getStayAwakeTime() <= 0 && (this.path == null || this.path.isFinished())))
        {
            dinosaur.setSleeping(true);
        }

        giveUpTime--;
    }

    @Override
    public boolean continueExecuting()
    {
        return dinosaur != null && !dinosaur.isCarcass() && !dinosaur.isSleeping() && dinosaur.shouldSleep();
    }

    @Override
    public void resetTask()
    {
        dinosaur.setSleeping(true);
    }
}
