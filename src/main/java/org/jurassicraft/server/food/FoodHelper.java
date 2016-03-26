package org.jurassicraft.server.food;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum FoodHelper
{
    INSTANCE;

    private final Map<FoodType, List<Item>> foodTypes = new HashMap<FoodType, List<Item>>();
    private final List<Item> food = new ArrayList<Item>();

    // TODO:  Add food values for each.

    public void init()
    {
        registerFood(Blocks.leaves, FoodType.PLANT);
        registerFood(Blocks.leaves2, FoodType.PLANT);
        registerFood(Blocks.tallgrass, FoodType.PLANT);
        registerFood(Blocks.wheat, FoodType.PLANT);
        registerFood(Blocks.melon_block, FoodType.PLANT);
        registerFood(Blocks.reeds, FoodType.PLANT);
        registerFood(Blocks.sapling, FoodType.PLANT);
        registerFood(Blocks.pumpkin, FoodType.PLANT);
        registerFood(Blocks.carrots, FoodType.PLANT);
        registerFood(Blocks.potatoes, FoodType.PLANT);
        registerFood(Blocks.hay_block, FoodType.PLANT);
        registerFood(Blocks.waterlily, FoodType.PLANT);
        registerFood(Blocks.yellow_flower, FoodType.PLANT);
        registerFood(Blocks.red_flower, FoodType.PLANT);
        registerFood(Blocks.double_plant, FoodType.PLANT);

        for (Plant plant : PlantHandler.INSTANCE.getPlants())
        {
            registerFood(plant.getBlock(), FoodType.PLANT);
        }

        for (TreeType type : TreeType.values())
        {
            registerFood(BlockHandler.INSTANCE.leaves.get(type), FoodType.PLANT);
            registerFood(BlockHandler.INSTANCE.saplings.get(type), FoodType.PLANT);
        }

        registerFood(Items.apple, FoodType.PLANT);
        registerFood(Items.potato, FoodType.PLANT);
        registerFood(Items.carrot, FoodType.PLANT);
        registerFood(Items.wheat, FoodType.PLANT);
        registerFood(Items.wheat_seeds, FoodType.PLANT);
        registerFood(Items.melon_seeds, FoodType.PLANT);
        registerFood(Items.pumpkin_seeds, FoodType.PLANT);
        registerFood(Items.melon, FoodType.PLANT);

        registerFood(Items.beef, FoodType.MEAT);
        registerFood(Items.cooked_beef, FoodType.MEAT);
        registerFood(Items.porkchop, FoodType.MEAT);
        registerFood(Items.cooked_porkchop, FoodType.MEAT);
        registerFood(Items.chicken, FoodType.MEAT);
        registerFood(Items.cooked_chicken, FoodType.MEAT);
        registerFood(Items.fish, FoodType.FISH);
        registerFood(Items.cooked_fish, FoodType.MEAT);
        registerFood(Items.mutton, FoodType.MEAT);
        registerFood(Items.cooked_mutton, FoodType.MEAT);
        registerFood(Items.rabbit, FoodType.MEAT);
        registerFood(Items.cooked_rabbit, FoodType.MEAT);

        registerFood(ItemHandler.INSTANCE.dino_meat, FoodType.MEAT);
        registerFood(ItemHandler.INSTANCE.dino_steak, FoodType.MEAT);

        for (Item item : Item.itemRegistry)
        {
            String resourceDomain = Item.itemRegistry.getNameForObject(item).getResourceDomain();

            if (!resourceDomain.equals("minecraft"))
            {
                if (item instanceof ItemFood)
                {
                    ItemFood food = (ItemFood) item;

                    if (food.getHealAmount(new ItemStack(food)) > 3)
                    {
                        registerFood(food, FoodType.PLANT);
                    }
                    else
                    {
                        registerFood(food, FoodType.MEAT);
                    }
                }
            }
        }
    }

    public void registerFood(Item food, FoodType foodType)
    {
        if (!this.food.contains(food))
        {
            List<Item> foodsForType = foodTypes.get(foodType);

            if (foodsForType == null)
            {
                foodsForType = new ArrayList<Item>();
            }

            foodsForType.add(food);

            this.food.add(food);

            foodTypes.put(foodType, foodsForType);
        }
    }

    public void registerFood(Block food, FoodType foodType)
    {
        registerFood(Item.getItemFromBlock(food), foodType);
    }

    public List<Item> getFoodType(FoodType type)
    {
        return foodTypes.get(type);
    }

    public FoodType getFoodType(Item item)
    {
        for (FoodType foodType : FoodType.values())
        {
            if (getFoodType(foodType).contains(item))
            {
                return foodType;
            }
        }

        return null;
    }

    public FoodType getFoodType(Block block)
    {
        return getFoodType(Item.getItemFromBlock(block));
    }

    public boolean canDietEat(EnumDiet diet, Item item)
    {
        return getFoodsForDiet(diet).contains(item);
    }

    public boolean canDietEat(EnumDiet diet, Block block)
    {
        return canDietEat(diet, Item.getItemFromBlock(block));
    }

    private List<Item> getFoodsForDiet(EnumDiet diet)
    {
        List<Item> possibleItems = new ArrayList<Item>();

        if (diet.doesEatPlants())
        {
            possibleItems.addAll(getFoodType(FoodType.PLANT));
        }

        if (diet.doesEatFish())
        {
            possibleItems.addAll(getFoodType(FoodType.FISH));
        }

        if (diet.doesEatMeat())
        {
            possibleItems.addAll(getFoodType(FoodType.MEAT));
        }

        return possibleItems;
    }
}
