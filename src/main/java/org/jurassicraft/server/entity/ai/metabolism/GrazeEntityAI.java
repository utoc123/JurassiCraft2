package org.jurassicraft.server.entity.ai.metabolism;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.ai.util.OnionTraverser;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.MetabolismContainer;
import org.jurassicraft.server.food.FoodHelper;

public class GrazeEntityAI extends EntityAIBase
{
    public static final int EAT_RADIUS = 6;// was 25
    public static final int LOOK_RADIUS = 16;
    private static final int GIVE_UP_TIME = 200;// 7*20 counter = 7 seconds (ish?
    private static final Logger LOGGER = LogManager.getLogger();

    protected DinosaurEntity dinosaur;
    protected BlockPos target;
    private int counter;
    private World world;
    private BlockPos previousTarget;
    private Vec3d targetVec;

    public GrazeEntityAI(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        return !(dinosaur.isDead || dinosaur.isCarcass() || !dinosaur.worldObj.getGameRules().getBoolean("dinoMetabolism")) && dinosaur.getMetabolism().isHungry();
    }

    @Override
    public void startExecuting()
    {
        // This gets called once to initiate.  Here's where we find the plant and start movement
        Vec3d headPos = dinosaur.getHeadPos();
        BlockPos head = new BlockPos(headPos.xCoord, headPos.yCoord, headPos.zCoord);

        //world the animal currently inhabits
        world = dinosaur.worldObj;

        MetabolismContainer metabolism = dinosaur.getMetabolism();

        // Look in increasing layers (e.g. boxes) around the head. Traversers... are like ogres?
        OnionTraverser traverser = new OnionTraverser(head, LOOK_RADIUS);
        target = null;

        //scans all blocks around the LOOK_RADIUS
        for (BlockPos pos : traverser)
        {
            Block block = world.getBlockState(pos).getBlock();

            if (FoodHelper.isEdible(dinosaur.getDinosaur().getDiet(), block) && pos != previousTarget)
            {
                target = pos;
                targetVec = new Vec3d(target.getX(), target.getY(), target.getZ());
                break;
            }
        }

        if (target != null && metabolism.isStarving())
        {
            dinosaur.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), 1.2);
        }
        else if (target != null)
        {
            dinosaur.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), 0.7);
        }
    }

    @Override
    public boolean continueExecuting()
    {
        if (target != null && world.isAirBlock(target) && !dinosaur.getNavigator().noPath())
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
            Vec3d headPos = dinosaur.getHeadPos();
            Vec3d headVec = new Vec3d(headPos.xCoord, target.getY(), headPos.zCoord);

            if (headVec.squareDistanceTo(targetVec) < EAT_RADIUS)
            {
                dinosaur.getNavigator().clearPathEntity();

                // TODO inadequate method for looking at block
                dinosaur.getLookHelper().setLookPosition(target.getX(), target.getY(), target.getZ(), 30.0F, dinosaur.getVerticalFaceSpeed());

                dinosaur.setAnimation(DinosaurAnimation.EATING.get());

                Item item = Item.getItemFromBlock(world.getBlockState(target).getBlock());

                world.destroyBlock(target, false);

                dinosaur.getMetabolism().eat(FoodHelper.getHealAmount(item));
                FoodHelper.applyEatEffects(dinosaur, item);
                dinosaur.heal(10.0F);

                previousTarget = null;
                terminateTask();
            }
            else
            {
                counter++;

                if (counter >= GIVE_UP_TIME)
                {
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
        dinosaur.setAnimation(DinosaurAnimation.IDLE.get());
    }
}
