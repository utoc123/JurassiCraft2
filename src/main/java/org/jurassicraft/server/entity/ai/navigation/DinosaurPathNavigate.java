package org.jurassicraft.server.entity.ai.navigation;

import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.DinosaurEntity;

public class DinosaurPathNavigate extends PathNavigateGround {
    private DinosaurEntity dinosaur;

    public DinosaurPathNavigate(DinosaurEntity entity, World world) {
        super(entity, world);
        this.dinosaur = entity;
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new DinosaurWalkNodeProcessor(((DinosaurEntity) this.theEntity).getDinosaur());
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    @Override
    protected void pathFollow() {
        Vec3d position = this.getEntityPosition();
        int length = this.currentPath.getCurrentPathLength();

        for (int i = this.currentPath.getCurrentPathIndex(); i < this.currentPath.getCurrentPathLength(); ++i) {
            if (this.currentPath.getPathPointFromIndex(i).yCoord != Math.floor(position.yCoord)) {
                length = i;
                break;
            }
        }

        Vec3d target = this.currentPath.getCurrentPos();

        float deltaX = MathHelper.abs((float) (this.theEntity.posX - (target.xCoord + 0.5)));
        float deltaZ = MathHelper.abs((float) (this.theEntity.posZ - (target.zCoord + 0.5)));
        float deltaY = MathHelper.abs((float) (this.theEntity.posY - target.yCoord));

        int width = MathHelper.ceil(this.theEntity.width);
        int height = MathHelper.ceil(this.theEntity.height);

        float maxDistance = this.theEntity.width > 0.75F ? width : 0.75F - this.theEntity.width / 2.0F;

        if (deltaX < maxDistance && deltaZ < maxDistance && deltaY < 1.0) {
            this.currentPath.incrementPathIndex();
        }

        for (int i = length - 1; i >= this.currentPath.getCurrentPathIndex(); --i) {
            if (this.isDirectPathBetweenPoints(position, this.currentPath.getVectorFromIndex(this.theEntity, i), width, height, width)) {
                this.currentPath.setCurrentPathIndex(i);
                break;
            }
        }

        this.checkForStuck(position);
    }

    @Override
    protected boolean canNavigate() {
        return !this.dinosaur.isMovementBlocked() && super.canNavigate();
    }
}
