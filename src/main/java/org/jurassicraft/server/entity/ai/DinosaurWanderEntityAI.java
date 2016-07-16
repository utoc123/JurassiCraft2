package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class DinosaurWanderEntityAI extends EntityAIBase
{
    private DinosaurEntity entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int executionChance;
    private boolean mustUpdate;

    public DinosaurWanderEntityAI(DinosaurEntity creatureIn, double speedIn)
    {
        this(creatureIn, speedIn, 120);
    }

    public DinosaurWanderEntityAI(DinosaurEntity creatureIn, double speedIn, int chance)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        //TODO: edit once you are pleased with the wander.
        this.executionChance = 0;//chance;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.mustUpdate)
        {
            if (this.entity.getAge() >= 100)
            {
                return false;
            }
    //TODO: edit once you are pleased with the wander.
            //if (this.entity.getRNG().nextInt(this.executionChance) != 0)
            //{
            //    return false;
            //}
        }

        Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            this.xPosition = vec3d.xCoord;
            this.yPosition = vec3d.yCoord;
            this.zPosition = vec3d.zCoord;
            this.mustUpdate = false;
            return true;
        }
    }

    public boolean continueExecuting()
    {
        return !this.entity.getNavigator().noPath();
    }

    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate()
    {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance)
    {
        this.executionChance = newchance;
    }
}