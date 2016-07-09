package org.jurassicraft.server.entity.base;

import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.List;

public enum DinosaurStatus
{
    TAMED
            {
                @Override
                public boolean apply(DinosaurEntity entity)
                {
                    return entity.getOwner() != null;
                }
            },
    LOW_HEALTH
            {
                @Override
                public boolean apply(DinosaurEntity entity)
                {
                    return entity.getHealth() < entity.getMaxHealth() / 4;
                }
            },
    HUNGRY
            {
                @Override
                public boolean apply(DinosaurEntity entity)
                {
                    return entity.getMetabolism().isHungry();
                }
            },
    THIRSTY
            {
                @Override
                public boolean apply(DinosaurEntity entity)
                {
                    return entity.getMetabolism().isThirsty();
                }
            },
    POISONED
            {
                @Override
                public boolean apply(DinosaurEntity entity)
                {
                    return entity.isPotionActive(Potion.getPotionFromResourceLocation("poison"));
                }
            },
    DROWNING
            {
                @Override
                public boolean apply(DinosaurEntity entity)
                {
                    return !entity.getDinosaur().isMarineAnimal() && entity.getAir() < 200;
                }
            },
    SLEEPY
            {
                @Override
                public boolean apply(DinosaurEntity entity)
                {
                    return entity.shouldSleep();
                }
            };

    public static List<DinosaurStatus> getActiveStatuses(DinosaurEntity entity)
    {
        List<DinosaurStatus> statuses = new ArrayList<>();

        for (DinosaurStatus status : values())
        {
            if (status.apply(entity))
            {
                statuses.add(status);
            }
        }

        return statuses;
    }

    public abstract boolean apply(DinosaurEntity entity);
}
