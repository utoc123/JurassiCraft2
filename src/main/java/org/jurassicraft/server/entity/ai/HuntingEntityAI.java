package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.server.entity.ai.util.HuntingUtils;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class HuntingEntityAI extends EntityAIBase
{

    protected final DinosaurEntity dinosaur;
    private BlockPos pos;
    private Entity target;
    private HuntingUtils utils;

    public HuntingEntityAI(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;
        utils = new HuntingUtils(dinosaur);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!dinosaur.worldObj.getGameRules().getBoolean("dinoMetabolism"))
        {
            return false;
        }
        if (dinosaur.getMetabolism().getFood() > (dinosaur.getMetabolism().getMaxFood() / 3 * 2))
        {
            return false;
        }
        if (utils.getEntitiesWithinDistance(dinosaur, 30, 10).isEmpty())
        {
            return false;
        }
        return true;
    }

    @Override
    public void startExecuting()
    {
        utils.addEntity(utils.getEntitiesWithinDistance(dinosaur, 30, 10));
        target = utils.chooseEntity();
        if (!dinosaur.attackEntityAsMob(target))
        {
            target = null;
        }
    }

    @Override
    public boolean continueExecuting()
    {
        if (target == null)
        {
            return false;
        }

        if (!dinosaur.getNavigator().noPath())
        {
            target = null;
        }

        return (target != null);
    }

    @Override
    public void resetTask()
    {
        if (target != null)
        {
            dinosaur.getNavigator().clearPathEntity();
        }
    }
}
