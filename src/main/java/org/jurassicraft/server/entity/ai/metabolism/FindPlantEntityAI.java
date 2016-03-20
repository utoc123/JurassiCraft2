package org.jurassicraft.server.entity.ai.metabolism;

import net.ilexiconn.llibrary.common.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.util.OnionTraverser;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.MetabolismContainer;

import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.entity.base.EnumDiet;

/**
 * This piece of AI use used to find a plant and eat it. Should be titled "graze".
 */
public class FindPlantEntityAI extends EntityAIBase
{
    // We always want to eat if below this.
    public static final double MUST_EAT_THRESHOLD = 0.25;

    // If we are awake, then we want to eat below this threshold
    public static final double SHOULD_EAT_THRESHOLD = 0.82;

    // How far to eat the thing
    public static final int EAT_RADIUS = 25;

    // This is how fast we are to break grass
    public static final double EAT_BREAK_SPEED = 1.0;

    // The minimum time to eat.
    public static final double MIN_BREAK_TIME_SEC = 3.0;

    // How many block away the critter will look for plants.
    // TODO: Add eyesight/smell attribute for finding plants.
    public static final int LOOK_RADIUS = 16;

    // The animal we are tracking for.
    protected DinosaurEntity dinosaur;

    // The target block to feed on, other null if currently not targeting anything
    protected BlockPos target = null;

    public FindPlantEntityAI(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        //We don't want to eat if we are dead or not supposed to
        if (dinosaur.isDead ||
                dinosaur.isCarcass() ||
                !dinosaur.worldObj.getGameRules().getBoolean("dinoMetabolism"))
        {
            return false;
        }

        // Now, let's see if we are hungry
        MetabolismContainer metabolism = dinosaur.getMetabolism();
            double food = metabolism.getFood();
            int maxFood = metabolism.getMaxFood();

        return ((food < (maxFood * MUST_EAT_THRESHOLD)) ||
                ((food < (maxFood * SHOULD_EAT_THRESHOLD)) &&
                        dinosaur.getDinosaur().getSleepingSchedule()
                                .isWithinEatingTime(dinosaur.getDinosaurTime(), dinosaur.getRNG())));
    }

    @Override
    public void startExecuting()
    {
        // This gets called once to initiate.  Here's where we find the plant and start movement
        BlockPos head = getHeadPos();

        //world the animal currently inhabits
        World world = dinosaur.worldObj;

        MetabolismContainer metabolism = dinosaur.getMetabolism();
            double food = metabolism.getFood();
            int maxFood = metabolism.getMaxFood();

        // Look in increasing layers (e.g. boxes) around the head. Traversers... are like ogres?
        OnionTraverser traverser = new OnionTraverser(head, LOOK_RADIUS);
        target = null;

        //scans all blocks around the LOOK_RADIUS
        for (BlockPos pos : traverser)
        {
            Block block = world.getBlockState(pos).getBlock();

            if (block instanceof BlockBush || block instanceof BlockLeaves)
//          if (FoodHelper.canDietEat(EnumDiet.HERBIVORE, block))
            {
                target = pos;
                break;
            }
        }

        if (target != null && food <= (maxFood * MUST_EAT_THRESHOLD))
        {
//          LOGGER.info("Running towards found plant food pos = " + target);
            dinosaur.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), 1.4);
        }

        else if (target != null && food <= (maxFood * SHOULD_EAT_THRESHOLD))
        {
//          LOGGER.info("Walking towards found plant food pos = " + target);
            dinosaur.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), 0.7);
        }
    }

    @Override 
    public boolean continueExecuting()
    {
        return target != null;
    }

    @Override
    public void updateTask()
    {
        if (dinosaur.getNavigator().noPath())
        {
//            TODO: Head is above ground, so we need to compute this differently.  Ideally it can bend down
//            if (getHeadPos().distanceSq(target) < EAT_RADIUS)
//            {               
                if(target != null){
                    // Start the animation
                    Animation.sendAnimationPacket(dinosaur, Animations.EATING.get());

                    dinosaur.getLookHelper().setLookPosition(target.getX(), target.getY(), target.getZ(),0, dinosaur.getVerticalFaceSpeed());

                    if (dinosaur.worldObj.getGameRules().getBoolean("mobGriefing"))
                    {
                        dinosaur.worldObj.destroyBlock(target, false);
                    }

                    // TODO:  Add food value & food heal value to food helper
                    dinosaur.getMetabolism().increaseFood(2000);
                    dinosaur.heal(4.0F);

                    //Now that we have finished stop the animation
                    TerminateTask();
                }
//            }
//            else
//            {
//              // TODO If animal cannot reach location, try again or end task  
//            }
        }
    }

    private void TerminateTask(){
        dinosaur.getNavigator().clearPathEntity();
        target = null;
        Animation.sendAnimationPacket(dinosaur, Animations.IDLE.get());
    }

    //=========================================================================

    private BlockPos getHeadPos()
    {
        // TODO:  Use getHeadPos() from DinosaurEntity once working correctly
        return dinosaur.getPosition().
                offset(dinosaur.getHorizontalFacing(), (int) dinosaur.width).
                offset(EnumFacing.UP, (int) dinosaur.getEyeHeight());
    }
  private static final Logger LOGGER = LogManager.getLogger();
}
