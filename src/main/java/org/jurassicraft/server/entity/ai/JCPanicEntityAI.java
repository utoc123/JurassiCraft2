package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class JCPanicEntityAI extends EntityAIBase
{
    private final DinosaurEntity theEntityCreature;
    protected double speed;
    private double randPosX;
    private double randPosY;
    private double randPosZ;

    public JCPanicEntityAI(DinosaurEntity creature, double speedIn)
    {
        theEntityCreature = creature;
        speed = speedIn;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if (theEntityCreature.getAITarget() == null && !theEntityCreature.isBurning())
        {
            return false;
        }
        else
        {
            Vec3d vec3 = RandomPositionGenerator.findRandomTarget(theEntityCreature, 5, 4);

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
        theEntityCreature.getNavigator().tryMoveToXYZ(randPosX, randPosY, randPosZ, speed);

        theEntityCreature.setAnimation(DinosaurAnimation.HISSING.get());

        // DEBUG
        System.out.println("Starting panic AI for entity " + theEntityCreature.getEntityId());

    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !theEntityCreature.getNavigator().noPath();
    }
}