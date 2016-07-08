package org.jurassicraft.server.food;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.entity.base.Diet;
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

    private final Map<FoodType, List<Item>> foodTypes = new HashMap<>();
    private final List<Item> food = new ArrayList<>();

    // TODO:  Add food values for each.

    public void init()
    {
        registerFood(Blocks.LEAVES, FoodType.PLANT);
        registerFood(Blocks.LEAVES2, FoodType.PLANT);
        registerFood(Blocks.TALLGRASS, FoodType.PLANT);
        registerFood(Blocks.WHEAT, FoodType.PLANT);
        registerFood(Blocks.MELON_BLOCK, FoodType.PLANT);
        registerFood(Blocks.REEDS, FoodType.PLANT);
        registerFood(Blocks.SAPLING, FoodType.PLANT);
        registerFood(Blocks.PUMPKIN, FoodType.PLANT);
        registerFood(Blocks.CARROTS, FoodType.PLANT);
        registerFood(Blocks.POTATOES, FoodType.PLANT);
        registerFood(Blocks.HAY_BLOCK, FoodType.PLANT);
        registerFood(Blocks.WATERLILY, FoodType.PLANT);
        registerFood(Blocks.YELLOW_FLOWER, FoodType.PLANT);
        registerFood(Blocks.RED_FLOWER, FoodType.PLANT);
        registerFood(Blocks.DOUBLE_PLANT, FoodType.PLANT);
        registerFood(Blocks.BROWN_MUSHROOM, FoodType.PLANT);
        registerFood(Blocks.RED_MUSHROOM, FoodType.PLANT);

        for (Plant plant : PlantHandler.getPlants())
        {
            registerFood(plant.getBlock(), FoodType.PLANT);
        }

        for (TreeType type : TreeType.values())
        {
            registerFood(BlockHandler.ANCIENT_LEAVES.get(type), FoodType.PLANT);
            registerFood(BlockHandler.ANCIENT_SAPLINGS.get(type), FoodType.PLANT);
        }

        registerFood(Items.APPLE, FoodType.PLANT);
        registerFood(Items.POTATO, FoodType.PLANT);
        registerFood(Items.CARROT, FoodType.PLANT);
        registerFood(Items.WHEAT, FoodType.PLANT);
        registerFood(Items.WHEAT_SEEDS, FoodType.PLANT);
        registerFood(Items.MELON_SEEDS, FoodType.PLANT);
        registerFood(Items.PUMPKIN_SEEDS, FoodType.PLANT);
        registerFood(Items.MELON, FoodType.PLANT);
        registerFood(Items.BREAD, FoodType.PLANT);
        registerFood(Items.SUGAR, FoodType.PLANT);
        registerFood(ItemHandler.WILD_ONION, FoodType.PLANT);
        registerFood(Items.BEETROOT, FoodType.PLANT);

        registerFood(Items.BEEF, FoodType.MEAT);
        registerFood(Items.COOKED_BEEF, FoodType.MEAT);
        registerFood(Items.PORKCHOP, FoodType.MEAT);
        registerFood(Items.COOKED_PORKCHOP, FoodType.MEAT);
        registerFood(Items.CHICKEN, FoodType.MEAT);
        registerFood(Items.COOKED_CHICKEN, FoodType.MEAT);
        registerFood(Items.FISH, FoodType.FISH);
        registerFood(Items.COOKED_FISH, FoodType.MEAT);
        registerFood(Items.MUTTON, FoodType.MEAT);
        registerFood(Items.COOKED_MUTTON, FoodType.MEAT);
        registerFood(Items.RABBIT, FoodType.MEAT);
        registerFood(Items.COOKED_RABBIT, FoodType.MEAT);
        registerFood(Items.ROTTEN_FLESH, FoodType.MEAT);

        registerFood(ItemHandler.DINOSAUR_MEAT, FoodType.MEAT);
        registerFood(ItemHandler.DINOSAUR_STEAK, FoodType.MEAT);

        for (Item item : Item.REGISTRY)
        {
            String resourceDomain = Item.REGISTRY.getNameForObject(item).getResourceDomain();

            if (!resourceDomain.equals("minecraft"))
            {
                if (item instanceof ItemFood)
                {
                    ItemFood food = (ItemFood) item;

                    if (food.getHealAmount(new ItemStack(food)) <= 3)
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
                foodsForType = new ArrayList<>();
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

    public boolean canDietEat(Diet diet, Item item)
    {
        return getEdibleFoods(diet).contains(item);
    }

    public boolean canDietEat(Diet diet, Block block)
    {
        return canDietEat(diet, Item.getItemFromBlock(block));
    }

    public List<Item> getEdibleFoods(Diet diet)
    {
        List<Item> possibleItems = new ArrayList<>();

        if (diet.isHerbivorous())
        {
            possibleItems.addAll(getFoodType(FoodType.PLANT));
        }

        if (diet.isPiscivorous())
        {
            possibleItems.addAll(getFoodType(FoodType.FISH));
        }

        if (diet.isCarnivorous())
        {
            possibleItems.addAll(getFoodType(FoodType.MEAT));
        }

        return possibleItems;
    }
}
