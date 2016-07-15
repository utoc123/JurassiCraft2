package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.List;

public class ProtectInfantEntityAI<T extends DinosaurEntity> extends EntityAIBase
{
    private T dinosaur;
    private Class<T> dinosaurClazz;
    private T infant;

    public ProtectInfantEntityAI(T dinosaur)
    {
        this.dinosaur = dinosaur;
        this.dinosaurClazz = (Class<T>) dinosaur.getClass();
    }

    @Override
    public boolean shouldExecute()
    {
        if (dinosaur.getAgePercentage() > 75)
        {
            infant = null;

            List<T> dinosaurs = dinosaur.worldObj.getEntitiesWithinAABB(dinosaurClazz, dinosaur.getEntityBoundingBox().expand(8, 3, 8));

            for (T entity : dinosaurs)
            {
                if (entity.getAITarget() != null && entity.getAgePercentage() <= 50)
                {
                    infant = entity;
                    break;
                }
            }

            if (infant != null)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void startExecuting()
    {
        EntityLivingBase attacker = infant.getAITarget();

        if (attacker != null && !(attacker instanceof EntityPlayer && ((EntityPlayer) attacker).capabilities.isCreativeMode))
        {
            dinosaur.setAttackTarget(attacker);
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
