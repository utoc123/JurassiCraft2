package org.jurassicraft.server.entity.ai;

import com.google.common.collect.Sets;
import net.minecraft.entity.ai.EntityAITempt;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.food.FoodHelper;

public class TemptNonAdultEntityAI extends EntityAITempt
{
    private DinosaurEntity dinosaur;

    public TemptNonAdultEntityAI(DinosaurEntity dinosaur, double speed)
    {
        super(dinosaur, speed, !dinosaur.getDinosaur().getDiet().doesEatMeat(), Sets.newHashSet(FoodHelper.INSTANCE.getEdibleFoods(dinosaur.getDinosaur().getDiet())));
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        return super.shouldExecute() && dinosaur.getAgePercentage() < 50;
    }
}
