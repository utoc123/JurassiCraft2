package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.server.entity.ai.util.AIUtils;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class AdvancedSwimEntityAI extends EntityAIBase
{
    private final DinosaurEntity entity;
    private BlockPos shore;

    public AdvancedSwimEntityAI(DinosaurEntity entity)
    {
        this.entity = entity;
        ((PathNavigateGround) entity.getNavigator()).setCanSwim(true);
    }

    @Override
    public boolean shouldExecute()
    {
        return (entity.isInLava() || entity.isInWater()) && entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting()
    {
        BlockPos surface = AIUtils.findSurface(entity);

        if (surface != null)
        {
            shore = AIUtils.findShore(entity.getEntityWorld(), surface);

            if (shore != null)
            {
                if (!entity.getNavigator().tryMoveToXYZ(shore.getX(), shore.getY(), shore.getZ(), 1.5))
                {
                    shore = null;
                }
            }
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return shore != null && (this.entity.isInWater() || this.entity.isInLava());
    }

    @Override
    public void updateTask()
    {
        if (shore != null && entity.getNavigator().noPath())
        {
            if (!entity.getNavigator().tryMoveToXYZ(shore.getX(), shore.getY(), shore.getZ(), 1.5))
            {
                shore = null;
            }
        }
    }
}

/*
    - Wade if water isn't too deep
    - Swim toward land
 */