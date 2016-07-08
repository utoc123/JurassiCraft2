package org.jurassicraft.server.entity.ai.metabolism;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.tile.FeederTile;

public class FeederEntityAI extends EntityAIBase
{
    protected DinosaurEntity dinosaur;

    protected Path path;
    protected BlockPos pos;

    public FeederEntityAI(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        if (!dinosaur.isDead && !dinosaur.isCarcass() && !dinosaur.isMovementBlocked() && dinosaur.ticksExisted % 16 == 0 && dinosaur.worldObj.getGameRules().getBoolean("dinoMetabolism"))
        {
            if (dinosaur.getMetabolism().isHungry())
            {
                int posX = (int) dinosaur.posX;
                int posY = (int) dinosaur.posY;
                int posZ = (int) dinosaur.posZ;

                int closestDist = Integer.MAX_VALUE;
                BlockPos closestPos = null;

                World world = dinosaur.worldObj;

                int range = 32;

                for (int x = posX - range; x < posX + range; x++)
                {
                    for (int y = posY - range; y < posY + range; y++)
                    {
                        for (int z = posZ - range; z < posZ + range; z++)
                        {
                            BlockPos pos = new BlockPos(x, y, z);
                            Block block = world.getBlockState(pos).getBlock();

                            if (block == BlockHandler.FEEDER)
                            {
                                FeederTile tile = (FeederTile) world.getTileEntity(pos);

                                if (tile != null && tile.canFeedDinosaur(dinosaur.getDinosaur()) && tile.getFeeding() == null && tile.openAnimation == 0)
                                {
                                    int deltaX = posX - x;
                                    int deltaY = posY - y;
                                    int deltaZ = posZ - z;

                                    int dist = (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);

                                    if (dist < closestDist)
                                    {
                                        closestDist = dist;
                                        closestPos = new BlockPos(x, y, z);
                                    }
                                }
                            }
                        }
                    }
                }

                if (closestPos != null)
                {
                    this.pos = closestPos;
                    this.path = dinosaur.getNavigator().getPathToPos(pos);
                    return this.dinosaur.getNavigator().setPath(path, 1.0);
                }
            }
        }

        return false;
    }

    @Override
    public void updateTask()
    {
        if (dinosaur.getDistance(pos.getX(), pos.getY(), pos.getZ()) < dinosaur.width + 2.0)
        {
            TileEntity tile = dinosaur.worldObj.getTileEntity(pos);

            if (tile instanceof FeederTile)
            {
                FeederTile feeder = (FeederTile) tile;
                feeder.setOpen(true);
                feeder.setFeeding(dinosaur);
            }

            resetTask();
        }
    }

    @Override
    public void resetTask()
    {
        dinosaur.getNavigator().clearPathEntity();
    }

    @Override
    public boolean continueExecuting()
    {
        Block block = dinosaur.worldObj.getBlockState(pos).getBlock();

        return dinosaur != null && path != null && path.equals(this.dinosaur.getNavigator().getPath()) && block == BlockHandler.FEEDER;
    }
}
