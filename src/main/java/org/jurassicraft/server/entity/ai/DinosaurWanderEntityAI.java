package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIWander;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class DinosaurWanderEntityAI extends EntityAIWander
{
    private DinosaurEntity dinosaur;

    public DinosaurWanderEntityAI(DinosaurEntity dinosaur, double speed, int chance)
    {
        super(dinosaur, speed, chance);
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        return (dinosaur.herd == null || dinosaur.herd.state == Herd.State.MOVING) && super.shouldExecute();
    }
}
