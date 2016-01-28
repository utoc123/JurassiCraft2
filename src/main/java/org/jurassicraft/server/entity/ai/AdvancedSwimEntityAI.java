package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.server.entity.ai.util.AIUtils;

/**
 * Copyright Timeless Mod Team
 * <p/>
 * Make things swim/wade to land.
 */
public class AdvancedSwimEntityAI extends EntityAIBase
{
    public AdvancedSwimEntityAI(EntityLiving entitylivingIn)
    {
        _entity = entitylivingIn;
        setMutexBits(4);
        ((PathNavigateGround) entitylivingIn.getNavigator()).setCanSwim(true);
    }

    @Override
    public boolean shouldExecute()
    {
        // If in lava, move to shore
        if (_entity.isInLava())
        {
            return true;
        }

        // We need to be not going any where, in water and
        return (_entity.getNavigator().noPath() && _entity.isInWater());
    }

    @Override
    public void startExecuting()
    {
        // TODO: What speed do we use in water?
        BlockPos surface = AIUtils.findSurface(_entity);
        _shore = AIUtils.findShore(_entity.getEntityWorld(), surface);
        if (_shore != null)
        {
            //LOGGER.info("Swimming, found shore. Surface=" + surface + " Moving to shore=" + _shore);
            _entity.getNavigator().tryMoveToXYZ(_shore.getX(), _shore.getY(), _shore.getZ(), 1.0);
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return _shore != null;
    }

    @Override
    public void updateTask()
    {
        if (_entity.getNavigator().noPath())
        {
            _shore = null;
            return;
        }

        // We should be moving toward shore. At the same time, bounce
//        int depth = AIUtils.getWaterDepth(_entity);
//        if (depth >= _entity.getEyeHeight() &&
//                     _entity.getRNG().nextFloat() < 0.8F)
//        {
//            _entity.getJumpHelper().setJumping();
//        }
    }

    private final EntityLiving _entity;
    private BlockPos _shore;

    private static final Logger LOGGER = LogManager.getLogger();
}


/*
    - Wade if water isn't too deep
    - Swim toward land
 */