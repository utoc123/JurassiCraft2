package org.jurassicraft.server.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;

import org.jurassicraft.server.entity.DinosaurEntity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.AxisAlignedBB;
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
    private boolean gliding;
    private boolean active;
    private int lastActive = -900;

    public RaptorHighGroundAI(DinosaurEntity theCreatureIn, double movementSpeedIn)
    {
        this.theCreature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.world;
        this.setMutexBits(7);
        
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
    	if(this.active == true || (this.theCreature.ticksExisted-this.lastActive)/20<45){
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
    	//System.out.println();
    	if(this.theCreature.onGround && this.active && this.theCreature.posY>=this.shelterY-1){
    		this.theCreature.addVelocity(0.2,0.8,0.2);
    		this.gliding = true;
    		
    	}
    	if(this.theCreature.isCollidedVertically && this.theCreature.motionX==0 && this.theCreature.motionX==0 && this.theCreature.motionX==0 && this.gliding==false){
    		this.theCreature.setPosition(this.theCreature.posX, this.theCreature.posY+0.14, this.theCreature.posZ);
    		
    	}
    	if(this.gliding&&this.theCreature.onGround){
    		this.lastActive = this.theCreature.ticksExisted;
    		this.active = false;
    		this.gliding = false;
    		return false;
    	}
    	if(this.theCreature.getNavigator().getPath() == null){
    		
    		this.active = false;
    	}
    	
    	return this.theCreature.getNavigator().getPath() != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
        this.active = true;
        this.gliding = false;
    }

    @Nullable
    private Vec3d findPossibleShelter()
    {
        BlockPos blockpos = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);
        Random rng = this.theCreature.getRNG();
        for (int i = 0; i < 20; ++i)
        {
        	BlockPos blockpos1 = blockpos.add(rng.nextInt(20) - 10, 0, rng.nextInt(20) - 10);


            if (!this.world.isAirBlock(blockpos1))
            {	
            	int recur = 0;
            	while(!this.world.isAirBlock(blockpos1) && recur<25){
            		blockpos1 = blockpos1.add(0,1,0);
            		recur+=1;
            	}
            	if(recur>5) return new Vec3d(((double)blockpos1.getX())+0.5f, (double)blockpos1.getY(), ((double)blockpos1.getZ())+0.5f);
                
            }
        }

        return null;
    }
}