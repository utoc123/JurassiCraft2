package org.jurassicraft.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.common.entity.ai.util.HerdManager;
import org.jurassicraft.common.entity.base.EntityDinosaur;

public class EntityAIHerd extends EntityAIBase
{
    // How fast we move toward the herd center
    //private static final double SPEED = 1.0D;
    private static final double SPEED = 0.6D;

    protected final EntityDinosaur _dinosaur;

    // The herd center
    private BlockPos _target;

    public EntityAIHerd(EntityDinosaur entity)
    {
        _dinosaur = entity;
        if (!entity.getEntityWorld().isRemote)
        {
            HerdManager.getInstance().add(entity);
        }
    }

    @Override
    public boolean shouldExecute()
    {
        if (_dinosaur.getEntityWorld().isRemote)
        {
            LOGGER.info(" !!!!! SHOULD EXECUTE IS ON REMOTE");
            return false;
        }

        // Only do this every once in a while
        if ((_dinosaur.ticksExisted & 0x3F) != 0)
            return false;

        _target = HerdManager.getInstance().getWanderLocation(_dinosaur);
        if (_target != null)
            LOGGER.info("Found target=" + _target + ", pos=" + _dinosaur.getPosition());

        return _target != null;
    }

    @Override
    public void startExecuting()
    {
        LOGGER.info("MOVING Found target=" + _target + ", pos=" + _dinosaur.getPosition());
        if (!_dinosaur.getNavigator().tryMoveToXYZ(_target.getX(), _target.getY(), _target.getZ(), SPEED))
            LOGGER.info("!!!!! Navigator says no.");
    }

    @Override
    public boolean continueExecuting()
    {
        if (_target == null)
            return false;

        if (!_dinosaur.getNavigator().noPath())
        {
            LOGGER.info("Done executing.");
            _target = null;
        }

        return (_target != null);
    }

    @Override
    public void resetTask()
    {
        LOGGER.info("resetTask");
        if (_target != null)
            _dinosaur.getNavigator().clearPathEntity();
    }

    private static final Logger LOGGER = LogManager.getLogger();
}


/**
 * FUTURE MODEL:
 *
 * graze - wander around inside herd radius
 * alerted - radius is closer
 *
 * The basic issue is that we don't have a real herd model.
 */