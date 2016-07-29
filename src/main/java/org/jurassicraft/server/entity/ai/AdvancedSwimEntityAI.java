package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.util.AIUtils;

public class AdvancedSwimEntityAI extends EntityAIBase {
    private final DinosaurEntity entity;
    private BlockPos shore;

    public AdvancedSwimEntityAI(DinosaurEntity entity) {
        this.entity = entity;
        ((PathNavigateGround) entity.getNavigator()).setCanSwim(true);
    }

    @Override
    public boolean shouldExecute() {
        return (this.entity.isInLava() || this.entity.isInWater()) && this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        BlockPos surface = AIUtils.findSurface(this.entity);

        if (surface != null) {
            this.shore = AIUtils.findShore(this.entity.getEntityWorld(), surface);

            if (this.shore != null) {
                if (!this.entity.getNavigator().tryMoveToXYZ(this.shore.getX(), this.shore.getY(), this.shore.getZ(), 1.5)) {
                    this.shore = null;
                }
            }
        }
    }

    @Override
    public boolean continueExecuting() {
        return this.shore != null && (this.entity.isInWater() || this.entity.isInLava());
    }

    @Override
    public void updateTask() {
        if (this.shore != null && this.entity.getNavigator().noPath()) {
            if (!this.entity.getNavigator().tryMoveToXYZ(this.shore.getX(), this.shore.getY(), this.shore.getZ(), 1.5)) {
                this.shore = null;
            }
        }
    }
}

/*
    - Wade if water isn't too deep
    - Swim toward land
 */