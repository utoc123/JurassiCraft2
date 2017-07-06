package org.jurassicraft.server.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;

import org.jurassicraft.server.entity.DinosaurEntity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RaptorGlideAI extends EntityAIBase
{
    private final DinosaurEntity theCreature;

    public RaptorGlideAI(DinosaurEntity theCreatureIn, double movementSpeedIn)
    {
        this.theCreature = theCreatureIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
    	//System.out.println(this.theCreature.getPosition().getY()+"   "+this.theCreature.getClimbHeight());
    	if(this.theCreature.isClimbing() && this.theCreature.getPosition().getY()>= this.theCreature.getClimbHeight()){
    		return true;
    	}
    	return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        System.out.println("gliding");
    	return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        //TODO put some logic here to glide
    }

}