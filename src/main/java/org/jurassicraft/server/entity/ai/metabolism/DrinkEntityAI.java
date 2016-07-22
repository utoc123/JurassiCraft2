package org.jurassicraft.server.entity.ai.metabolism;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.MetabolismContainer;

public class DrinkEntityAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    protected Path path;
    protected BlockPos pos;

    protected int giveUpTime;

    public DrinkEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dinosaur.isDead && !this.dinosaur.isCarcass() && this.dinosaur.ticksExisted % 4 == 0 && this.dinosaur.worldObj.getGameRules().getBoolean("dinoMetabolism")) {
            if (this.dinosaur.getMetabolism().isThirsty()) {
                int posX = (int) this.dinosaur.posX;
                int posY = (int) this.dinosaur.posY;
                int posZ = (int) this.dinosaur.posZ;

                int closestDistance = Integer.MAX_VALUE;
                BlockPos closestPos = null;

                World world = this.dinosaur.worldObj;

                int range = 32;

                for (int x = posX - range; x < posX + range; x++) {
                    for (int y = posY - range; y < posY + range; y++) {
                        for (int z = posZ - range; z < posZ + range; z++) {
                            Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();

                            if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
                                for (int landX = x - 1; landX < x + 1; landX++) {
                                    for (int landZ = z - 1; landZ < z + 1; landZ++) {
                                        IBlockState state = world.getBlockState(new BlockPos(landX, y, landZ));

                                        if (state.isOpaqueCube()) {
                                            int diffX = Math.abs(posX - landX);
                                            int diffY = Math.abs(posY - y);
                                            int diffZ = Math.abs(posZ - landZ);

                                            int distance = (diffX * diffX) + (diffY * diffY) + (diffZ * diffZ);

                                            if (distance < closestDistance) {
                                                closestDistance = distance;
                                                closestPos = new BlockPos(landX, y, landZ);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (closestPos != null) {
                    this.pos = closestPos;
                    this.path = this.dinosaur.getNavigator().getPathToXYZ(closestPos.getX(), closestPos.getY(), closestPos.getZ());
                    this.giveUpTime = 500;
                    return this.dinosaur.getNavigator().setPath(this.path, 1.0);
                }
            }
        }

        return false;
    }

    @Override
    public void updateTask() {
        this.giveUpTime--;

        if (this.giveUpTime <= 0) {
            this.resetTask();
            return;
        }

        this.dinosaur.getNavigator().setPath(this.path, 1.0);

        if (this.path.isFinished()) {
            if (this.dinosaur.getAnimation() != DinosaurAnimation.DRINKING.get()) {
                this.dinosaur.setAnimation(DinosaurAnimation.DRINKING.get());
            }

            MetabolismContainer metabolism = this.dinosaur.getMetabolism();
            metabolism.setWater(metabolism.getMaxWater());
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();

        this.path = null;
        this.dinosaur.getNavigator().clearPathEntity();
    }

    @Override
    public boolean continueExecuting() {
        Block block = this.dinosaur.worldObj.getBlockState(this.pos).getBlock();

        return this.dinosaur != null && this.path != null && !this.dinosaur.getNavigator().noPath() && (block == Blocks.WATER || block == Blocks.FLOWING_WATER);
    }
}
