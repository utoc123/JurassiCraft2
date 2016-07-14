package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class RespondToAttackEntityAI extends EntityAIBase
{
    private DinosaurEntity dinosaur;
    private EntityLivingBase attacker;

    public RespondToAttackEntityAI(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        this.attacker = dinosaur.getAITarget();

        if (attacker != null && !attacker.isDead && !(attacker instanceof DinosaurEntity && ((DinosaurEntity) attacker).isCarcass()) && !(attacker instanceof EntityPlayer && ((EntityPlayer) attacker).capabilities.isCreativeMode))
        {
            return true;
        }

        return false;
    }

    @Override
    public void startExecuting()
    {
        dinosaur.respondToAttack(attacker);
    }

    @Override
    public boolean continueExecuting()
    {
        return false;
    }
}
