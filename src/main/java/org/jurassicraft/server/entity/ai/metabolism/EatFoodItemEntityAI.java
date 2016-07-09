package org.jurassicraft.server.entity.ai.metabolism;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.food.FoodHelper;

import java.util.List;

public class EatFoodItemEntityAI extends EntityAIBase
{
    protected DinosaurEntity dinosaur;

    protected EntityItem item;
    protected boolean eaten;

    public EatFoodItemEntityAI(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute()
    {
        if (!dinosaur.isDead && !dinosaur.isCarcass() && dinosaur.worldObj.getGameRules().getBoolean("dinoMetabolism"))
        {
            if (dinosaur.getMetabolism().isHungry())
            {
                double posX = dinosaur.posX;
                double posY = dinosaur.posY;
                double posZ = dinosaur.posZ;

                double closestDist = Integer.MAX_VALUE;
                EntityItem closest = null;

                boolean found = false;

                World world = dinosaur.worldObj;

                List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 16, posY - 16, posZ - 16, posX + 16, posY + 16, posZ + 16));

                for (EntityItem entity : items)
                {
                    ItemStack stack = entity.getEntityItem();

                    Item item = stack.getItem();

                    if (FoodHelper.isEdible(dinosaur.getDinosaur().getDiet(), item))
                    {
                        double deltaX = Math.abs(posX - entity.posX);
                        double deltaY = Math.abs(posY - entity.posY);
                        double deltaZ = Math.abs(posZ - entity.posZ);

                        double distance = (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);

                        if (distance < closestDist)
                        {
                            closestDist = distance;
                            closest = entity;

                            found = true;
                        }
                    }
                }

                if (found)
                {
                    dinosaur.getNavigator().tryMoveToEntityLiving(closest, 1.0);
                    this.item = closest;

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void updateTask()
    {
        if (dinosaur.getEntityBoundingBox().intersectsWith(item.getEntityBoundingBox().expand(1.0, 1.0, 1.0)))
        {
            dinosaur.setAnimation(DinosaurAnimation.EATING.get());

            if (item.getEntityItem().stackSize > 1)
            {
                item.getEntityItem().stackSize--;
            }
            else
            {
                item.setDead();
            }

            Item item = this.item.getEntityItem().getItem();
            dinosaur.getMetabolism().eat(FoodHelper.getHealAmount(item));
            FoodHelper.applyEatEffects(dinosaur, item);
            dinosaur.heal(10.0F);

            eaten = true;
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return dinosaur != null && !this.dinosaur.getNavigator().noPath() && item != null && !item.isDead && !eaten;
    }
}
