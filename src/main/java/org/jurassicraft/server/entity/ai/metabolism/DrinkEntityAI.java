package org.jurassicraft.server.entity.ai.metabolism;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.MetabolismContainer;
import org.jurassicraft.server.entity.ai.Mutex;
import org.jurassicraft.server.entity.ai.util.AIUtils;
import org.jurassicraft.server.entity.ai.util.OnionTraverser;
import org.jurassicraft.server.util.GameRuleHandler;

public class DrinkEntityAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    protected Path path;
    protected BlockPos pos;

    protected int giveUpTime;

    public DrinkEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.METABOLISM);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dinosaur.isDead && !this.dinosaur.isCarcass() && GameRuleHandler.DINO_METABOLISM.getBoolean(this.dinosaur.worldObj)) {
            if (this.dinosaur.getMetabolism().isThirsty()) {
                World world = this.dinosaur.worldObj;
                BlockPos water = null;
                OnionTraverser traverser = new OnionTraverser(this.dinosaur.getPosition(), 32);
                for (BlockPos pos : traverser) {
                    if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                        BlockPos surface = AIUtils.findSurface(world, pos);
                        BlockPos shore = AIUtils.findShore(world, surface);
                        if (shore != null) {
                            IBlockState state = world.getBlockState(shore);
                            if (state.isFullBlock()) {
                                Path path = this.dinosaur.getNavigator().getPathToPos(shore);
                                if (path != null && path.getCurrentPathLength() != 0) {
                                    this.path = path;
                                    water = shore;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (water != null) {
                    this.pos = water;
                    this.giveUpTime = this.path.getCurrentPathLength() * 15;
                    return this.dinosaur.getNavigator().setPath(this.path, 1.0);
                }
            }
        }
        return false;
    }

    @Override
    public void updateTask() {
        if (this.giveUpTime > 0) {
            this.giveUpTime--;
        }
        if (this.path != null) {
            this.dinosaur.getNavigator().setPath(this.path, 1.0);
            if (this.path.isFinished() || this.dinosaur.getEntityBoundingBox().expandXyz(1).isVecInside(new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5))) {
                this.dinosaur.setAnimation(DinosaurAnimation.DRINKING.get());
                MetabolismContainer metabolism = this.dinosaur.getMetabolism();
                metabolism.setWater(metabolism.getWater() + (metabolism.getMaxWater() / 8));
            }
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
        return this.giveUpTime > 0 && this.dinosaur != null && !(this.dinosaur.isCarcass() || this.dinosaur.isDead) && this.path != null && this.dinosaur.getMetabolism().getWater() < this.dinosaur.getMetabolism().getMaxWater() * 0.9;
    }
}
