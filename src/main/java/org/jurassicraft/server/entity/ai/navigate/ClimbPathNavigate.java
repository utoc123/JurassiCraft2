package org.jurassicraft.server.entity.ai.navigate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class ClimbPathNavigate extends PathNavigateGround {
    public ClimbPathNavigate(EntityLiving entity, World world) {
        super(entity, world);
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new ClimbNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }
}
