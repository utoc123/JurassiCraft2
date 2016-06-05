package org.jurassicraft.server.entity.ai.metabolism;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.MetabolismContainer;

public class DrinkEntityAI extends EntityAIBase
{
    protected DinosaurEntity dinosaur;

    protected Path path;
    protected BlockPos pos;

    public DrinkEntityAI(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        if (!dinosaur.isDead && !dinosaur.isCarcass() && dinosaur.ticksExisted % 4 == 0 && dinosaur.worldObj.getGameRules().getBoolean("dinoMetabolism"))
        {
            if (dinosaur.getMetabolism().isThirsty())
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
                            Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();

                            if (block == Blocks.WATER || block == Blocks.FLOWING_WATER)
                            {
                                for (int landX = x - 1; landX < x + 1; landX++)
                                {
                                    for (int landZ = z - 1; landZ < z + 1; landZ++)
                                    {
                                        IBlockState state = world.getBlockState(new BlockPos(landX, y, landZ));

                                        if (state.isOpaqueCube())
                                        {
                                            int diffX = posX - landX;
                                            int diffY = posY - y;
                                            int diffZ = posZ - landZ;

                                            int dist = (diffX * diffX) + (diffY * diffY) + (diffZ * diffZ);

                                            if (dist < closestDist)
                                            {
                                                closestDist = dist;
                                                closestPos = new BlockPos(landX, y, landZ);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (closestPos != null)
                {
                    this.pos = closestPos;
                    this.path = dinosaur.getNavigator().getPathToXYZ(closestPos.getX(), closestPos.getY(), closestPos.getZ());
                    return this.dinosaur.getNavigator().setPath(path, 1.0);
                }
            }
        }

        return false;
    }

    @Override
    public void updateTask()
    {
        if (path.isFinished())
        {
            if (dinosaur.getAnimation() != Animations.DRINKING.get())
            {
                dinosaur.setAnimation(Animations.DRINKING.get());
            }

            MetabolismContainer metabolism = dinosaur.getMetabolism();
            metabolism.setWater(metabolism.getMaxWater());
        }
    }

    @Override
    public boolean continueExecuting()
    {
        Block block = dinosaur.worldObj.getBlockState(pos).getBlock();

        return dinosaur != null && path != null && !this.dinosaur.getNavigator().noPath() && (block == Blocks.WATER || block == Blocks.FLOWING_WATER);
    }
}
