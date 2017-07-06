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

public class RaptorHighGroundAI extends EntityAIBase
{
    private final DinosaurEntity theCreature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private final double movementSpeed;
    private final World world;

    public RaptorHighGroundAI(DinosaurEntity theCreatureIn, double movementSpeedIn)
    {
        this.theCreature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.world;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
    	if(this.theCreature.isClimbing()){
    		return false;
    	}
        Vec3d vec3d = this.findPossibleShelter();

        if (vec3d == null)
        {
        	//System.out.println("not executing");
            return false;
        }
        else
        {
        	
            this.shelterX = vec3d.xCoord;
            this.shelterY = vec3d.yCoord;
            this.shelterZ = vec3d.zCoord;
            //System.out.println("going to "+shelterX+","+shelterY+","+shelterZ+","+this.theCreature.isOnLadder());
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
    	System.out.println(this.theCreature.getNavigator().noPath());
        return !this.theCreature.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
        this.theCreature.setClimbing(true);
        this.theCreature.setClimbHeight((int) this.shelterY);
    }

    @Nullable
    private Vec3d findPossibleShelter()
    {
        BlockPos blockpos = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);

        for (int i = 0; i < 400; ++i)
        {
            BlockPos blockpos1 = blockpos.add(i%20-10, 0, i/20-10);

            if (!this.world.isAirBlock(blockpos1))
            {	
            	int recur = 0;
            	while(!this.world.isAirBlock(blockpos1) && recur<25){
            		blockpos1 = blockpos1.add(0,1,0);
            		recur+=1;
            	}
            	if(recur>8) return new Vec3d(((double)blockpos1.getX())+0.5f, (double)blockpos1.getY(), ((double)blockpos1.getZ())+0.5f);
                
            }
        }

        return null;
    }
}