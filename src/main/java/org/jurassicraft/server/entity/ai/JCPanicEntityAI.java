package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class JCPanicEntityAI extends EntityAIBase
{
    private final DinosaurEntity dinosaur;
    protected double speed;
    private double randPosX;
    private double randPosY;
    private double randPosZ;

    public JCPanicEntityAI(DinosaurEntity dinosaur, double speed)
    {
        this.dinosaur = dinosaur;
        this.speed = speed;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (dinosaur.getAITarget() == null && !dinosaur.isBurning())
        {
            return false;
        }
        else
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(dinosaur, 5, 4);

            if (vec3 == null)
            {
                return false;
            }
            else
            {
                randPosX = vec3.xCoord;
                randPosY = vec3.yCoord;
                randPosZ = vec3.zCoord;
                return true;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        dinosaur.getNavigator().tryMoveToXYZ(randPosX, randPosY, randPosZ, speed);

        dinosaur.setAnimation(Animations.HISSING.get());
        // DEBUG
        System.out.println("Starting panic AI for entity " + dinosaur.getEntityId());

    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !dinosaur.getNavigator().noPath();
    }
}