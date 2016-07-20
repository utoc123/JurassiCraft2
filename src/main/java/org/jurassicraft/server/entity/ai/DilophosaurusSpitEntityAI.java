package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.dinosaur.DilophosaurusEntity;

public class DilophosaurusSpitEntityAI extends EntityAIBase
{
    private final DilophosaurusEntity dilophosaurus;
    private EntityLivingBase target;
    private int rangedAttackTime;
    private double speed;
    private int seeTime;
    private int attackInterval;
    private int maxRangedAttackTime;
    private float attackRadius;
    private float maxAttackDistance;

    public DilophosaurusSpitEntityAI(DilophosaurusEntity dilophosaurus, double speed, int maxAttackTime, float maxAttackDistance)
    {
        this(dilophosaurus, speed, maxAttackTime, maxAttackTime, maxAttackDistance);
    }

    public DilophosaurusSpitEntityAI(DilophosaurusEntity dilophosaurus, double speed, int attackInterval, int maxAttackTime, float maxAttackDistance)
    {
        this.rangedAttackTime = -1;

        this.dilophosaurus = dilophosaurus;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.maxRangedAttackTime = maxAttackTime;
        this.attackRadius = maxAttackDistance;
        this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase target = this.dilophosaurus.getAttackTarget();

        if (target != null && !(target.isDead || (target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass())) && !(target.getHealth() < target.getMaxHealth() * 0.9F && target.isPotionActive(Potion.getPotionFromResourceLocation("blindness"))))
        {
            this.target = target;

            return true;
        }

        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        return this.shouldExecute() || !this.dilophosaurus.getNavigator().noPath();
    }

    @Override
    public void resetTask()
    {
        this.target = null;
        this.seeTime = 0;
        this.rangedAttackTime = -1;
    }

    @Override
    public void updateTask()
    {
        double distance = this.dilophosaurus.getDistanceSq(this.target.posX, this.target.getEntityBoundingBox().minY, this.target.posZ);
        boolean canSee = this.dilophosaurus.getEntitySenses().canSee(this.target);

        if (canSee)
        {
            this.seeTime++;
        }
        else
        {
            this.seeTime = 0;
        }

        if (distance <= (double) this.maxAttackDistance && this.seeTime >= 20)
        {
            this.dilophosaurus.getNavigator().clearPathEntity();
        }
        else
        {
            this.dilophosaurus.getNavigator().tryMoveToEntityLiving(this.target, this.speed);
        }

        this.dilophosaurus.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);

        if (--this.rangedAttackTime == 0)
        {
            if (distance > (double) this.maxAttackDistance || !canSee)
            {
                return;
            }

            float scaledDistance = MathHelper.sqrt_double(distance) / this.attackRadius;
            this.dilophosaurus.attackEntityWithRangedAttack(this.target, MathHelper.clamp_float(scaledDistance, 0.1F, 1.0F));
            this.rangedAttackTime = MathHelper.floor_float(scaledDistance * (float) (this.maxRangedAttackTime - this.attackInterval) + (float) this.attackInterval);
        }
        else if (this.rangedAttackTime < 0)
        {
            float scaledDistance = MathHelper.sqrt_double(distance) / this.attackRadius;
            this.rangedAttackTime = MathHelper.floor_float(scaledDistance * (float) (this.maxRangedAttackTime - this.attackInterval) + (float) this.attackInterval);
        }
    }
}