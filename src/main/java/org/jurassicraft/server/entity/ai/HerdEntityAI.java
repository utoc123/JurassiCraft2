package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.server.entity.ai.util.HerdManager;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class HerdEntityAI extends EntityAIBase
{
    // How fast we move toward the herd center
    // TODO: Speed should vary based on the herd "Alert level"
    //private static final double SPEED = 1.0D;
    private static final double SPEED = 0.6D;

    protected final DinosaurEntity dinosaur;

    // The herd center
    private BlockPos target;

    public HerdEntityAI(DinosaurEntity entity)
    {
        dinosaur = entity;
        if (!entity.getEntityWorld().isRemote)
        {
            HerdManager.getInstance().add(entity);
        }
    }

    @Override
    public boolean shouldExecute()
    {
        if (!dinosaur.worldObj.getGameRules().getBoolean("dinoHerding"))
            return false;

        if (dinosaur.getEntityWorld().isRemote)
        {
            return false;
        }

        // Only do this every once in a while.
        if ((dinosaur.ticksExisted & 0x3F) != (hashCode() & 0x3F))
            return false;

        // Ask the herd manager where we should move to
        target = HerdManager.getInstance().getWanderLocation(dinosaur);
        //if (_target != null)
        //    LOGGER.info("Found target=" + _target + ", pos=" + dinosaur.getPosition());

        return target != null;
    }

    @Override
    public void startExecuting()
    {
        //LOGGER.info("MOVING Found target=" + _target + ", pos=" + dinosaur.getPosition());

        dinosaur.getLookHelper().setLookPosition(target.getX(), target.getY(), target.getZ(), 1.0F, 1.0F);
        if (!dinosaur.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), SPEED))
        {
            // If we failed to try to move to (e.g. pathfinder can't find it)
            // then null out the target so we don't "continue executing."
            target = null;
        }
    }

    @Override
    public boolean continueExecuting()
    {
        if (target == null)
            return false;

        if (!dinosaur.getNavigator().noPath())
        {
            LOGGER.info("Done executing.");
            target = null;
        }

        return (target != null);
    }

    @Override
    public void resetTask()
    {
        LOGGER.info("resetTask");
        if (target != null)
            dinosaur.getNavigator().clearPathEntity();
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