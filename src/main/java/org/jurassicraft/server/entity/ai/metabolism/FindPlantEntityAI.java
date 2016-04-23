package org.jurassicraft.server.entity.ai.metabolism;

import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.ai.util.BlockBreaker;
import org.jurassicraft.server.entity.ai.util.OnionTraverser;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.MetabolismContainer;

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
    public static final int EAT_RADIUS = 6;// was 25

    // This is how fast we are to break grass
    public static final double EAT_BREAK_SPEED = 1.0;
    // The minimum time to eat.
    public static final double MIN_BREAK_TIME_SEC = 3.0;

    // Used to animate block breaking
    protected BlockBreaker breaker = null;

    private int counter;
    //Time at which animal will cease attempting to eat a block
    private static final int GIVE_UP_TIME = 140;// 7*20 counter = 7 ticks (ish?

    // How many block away the critter will look for plants.
    // TODO: Add eyesight/smell attribute for finding plants.
    public static final int LOOK_RADIUS = 16;

    // The animal we are tracking for.
    protected DinosaurEntity dinosaur;

    private World world;

    // The target block to feed on, other null if currently not targeting anything
    protected BlockPos target;
    private BlockPos previousTarget;

    private Vec3d targetVec;

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
        BlockPos head = new BlockPos(dinosaur.getHeadPos().xCoord, dinosaur.getHeadPos().yCoord, dinosaur.getHeadPos().zCoord);

        //world the animal currently inhabits
        world = dinosaur.worldObj;

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

            if (block instanceof BlockBush || block instanceof BlockLeaves && pos != previousTarget)
//          if (FoodHandler.canDietEat(EnumDiet.HERBIVORE, block)) // TODO returns true for air blocks
            {
                target = pos;
                targetVec = new Vec3d(target.getX(), target.getY(), target.getZ());
                break;
            }
        }

        if (target != null && food <= (maxFood * MUST_EAT_THRESHOLD))
        {
//          LOGGER.info("Running towards found plant food pos = " + target);
            dinosaur.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), 1.2);
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
        if (target != null && world.isAirBlock(target))
        {
            terminateTask();
            return false;
        }
        return target != null;
    }

    @Override
    public void updateTask()
    {
        if (target != null)
        {
            Vec3d headVec = new Vec3d(dinosaur.getHeadPos().xCoord, target.getY(), dinosaur.getHeadPos().zCoord);
            if (headVec.squareDistanceTo(targetVec) < EAT_RADIUS)
            {
                dinosaur.getNavigator().clearPathEntity();

                // TODO inadequate method for looking at block
                dinosaur.getLookHelper().setLookPosition(target.getX(), target.getY(), target.getZ(), 0, dinosaur.getVerticalFaceSpeed());

                AnimationHandler.INSTANCE.sendAnimationMessage(dinosaur, Animations.EATING.get());

                // TODO reimplement BlockBreaker
                breaker = new BlockBreaker(dinosaur, EAT_BREAK_SPEED, target, MIN_BREAK_TIME_SEC);

//                if (breaker.tickUpdate()){
                if (world.getGameRules().getBoolean("mobGriefing"))
                {
                    world.destroyBlock(target, false);
                }

                // TODO:  Add food value & food heal value to food helper
                dinosaur.getMetabolism().increaseFood(2000);
                dinosaur.heal(4.0F);

                previousTarget = null;
                terminateTask();
//                }
            }
            else
            {
                counter++;
                if (counter >= GIVE_UP_TIME)
                {
                    // TODO perhaps some sort of visual/audiatory display to showcase animal cannot reach food?
                    LOGGER.info("Targeted food block was too far, seeking another target...");
                    counter = 0;
                    previousTarget = target;
                    terminateTask();
                }
            }
        }
    }

    private void terminateTask()
    {
        dinosaur.getNavigator().clearPathEntity();
        target = null;
        AnimationHandler.INSTANCE.sendAnimationMessage(dinosaur, Animations.IDLE.get());
    }

    private static final Logger LOGGER = LogManager.getLogger();
}
