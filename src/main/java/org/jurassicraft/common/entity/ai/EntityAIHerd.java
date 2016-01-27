package org.jurassicraft.common.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.common.entity.ai.util.HerdManager;
import org.jurassicraft.common.entity.base.EntityDinosaur;

public class EntityAIHerd extends EntityAIBase
{
    // How fast we move toward the herd center
    // TODO: Speed should vary based on the herd "Alert level"
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
        if (!_dinosaur.worldObj.getGameRules().getBoolean("dinoHerding"))
            return false;

        if (_dinosaur.getEntityWorld().isRemote)
        {
            return false;
        }

        // Only do this every once in a while.
        if ((_dinosaur.ticksExisted & 0x3F) != 0)
            return false;

        _target = HerdManager.getInstance().getWanderLocation(_dinosaur);
        //if (_target != null)
        //    LOGGER.info("Found target=" + _target + ", pos=" + _dinosaur.getPosition());

        return _target != null;
    }

    @Override
    public void startExecuting()
    {
        //LOGGER.info("MOVING Found target=" + _target + ", pos=" + _dinosaur.getPosition());

        _dinosaur.getLookHelper().setLookPosition(_target.getX(), _target.getY(), _target.getZ(), 1.0F, 1.0F);
        if (!_dinosaur.getNavigator().tryMoveToXYZ(_target.getX(), _target.getY(), _target.getZ(), SPEED))
        {
            // If we failed to try to move to (e.g. pathfinder can't find it)
            // then null out the target so we don't "continue executing."
            _target = null;
        }
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