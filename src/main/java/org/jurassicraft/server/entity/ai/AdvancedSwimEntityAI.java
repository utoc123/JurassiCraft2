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
        this.setMutexBits(4);
        ((PathNavigateGround) entity.getNavigator()).setCanSwim(true);
    }

    @Override
    public boolean shouldExecute()
    {
        return entity.isInLava() || entity.isInWater();
    }

    @Override
    public void startExecuting()
    {
        // TODO: What speed do we use in water?
        BlockPos surface = AIUtils.findSurface(entity);
        shore = AIUtils.findShore(entity.getEntityWorld(), surface);
        if (shore != null)
        {
            //LOGGER.info("Swimming, found shore. Surface=" + surface + " Moving to shore=" + shore);
            entity.getNavigator().tryMoveToXYZ(shore.getX(), shore.getY(), shore.getZ(), 1.0);
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return this.entity.isInWater() || this.entity.isInLava();
//        return shore != null || AIUtils.getWaterDepth(entity) >= entity.getEyeHeight();
    }

    @Override
    public void updateTask()
    {
        // We should be moving toward shore. At the same time, bounce
        int depth = AIUtils.getWaterDepth(entity);
        if (depth >= entity.getEyeHeight() && entity.getRNG().nextFloat() < 0.8F)
        {
            entity.getJumpHelper().setJumping();
        }

        if (entity.getNavigator().noPath())
        {
            shore = null;
        }
    }
}

/*
    - Wade if water isn't too deep
    - Swim toward land
 */