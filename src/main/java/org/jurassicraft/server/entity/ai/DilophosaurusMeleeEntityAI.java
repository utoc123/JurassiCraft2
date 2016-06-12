package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.potion.Potion;
import org.jurassicraft.server.entity.dinosaur.DilophosaurusEntity;

public class DilophosaurusMeleeEntityAI extends EntityAIAttackMelee
{
    public DilophosaurusMeleeEntityAI(DilophosaurusEntity entity, double speed)
    {
        super(entity, speed, false);
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase target = attacker.getAttackTarget();
        return super.shouldExecute() && target.getHealth() < target.getMaxHealth() * 0.9F && target.isPotionActive(Potion.getPotionFromResourceLocation("blindness"));
    }
}
