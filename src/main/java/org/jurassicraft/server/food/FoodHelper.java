package org.jurassicraft.server.food;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FoodHelper
{
    private static final Map<FoodType, List<Item>> FOOD_TYPES = new HashMap<>();
    private static final List<Item> FOODS = new LinkedList<>();
    private static final Map<Item, Integer> HEAL_AMOUNTS = new HashMap<>();
    private static final Map<Item, FoodEffect[]> FOOD_EFFECTS = new HashMap<>();

    public static void init()
    {
        registerFood(Blocks.LEAVES, FoodType.PLANT, 2000);
        registerFood(Blocks.LEAVES2, FoodType.PLANT, 2000);
        registerFood(Blocks.TALLGRASS, FoodType.PLANT, 1000);
        registerFood(Blocks.WHEAT, FoodType.PLANT, 2000);
        registerFood(Blocks.MELON_BLOCK, FoodType.PLANT, 3000);
        registerFood(Blocks.REEDS, FoodType.PLANT, 1000);
        registerFood(Blocks.SAPLING, FoodType.PLANT, 1000);
        registerFood(Blocks.PUMPKIN, FoodType.PLANT, 3000);
        registerFood(Blocks.CARROTS, FoodType.PLANT, 2000);
        registerFood(Blocks.POTATOES, FoodType.PLANT, 2000);
        registerFood(Blocks.HAY_BLOCK, FoodType.PLANT, 5000);
        registerFood(Blocks.WATERLILY, FoodType.PLANT, 500);
        registerFood(Blocks.YELLOW_FLOWER, FoodType.PLANT, 500);
        registerFood(Blocks.RED_FLOWER, FoodType.PLANT, 500);
        registerFood(Blocks.DOUBLE_PLANT, FoodType.PLANT, 2000);
        registerFood(Blocks.BROWN_MUSHROOM, FoodType.PLANT, 250);
        registerFood(Blocks.RED_MUSHROOM, FoodType.PLANT, 250);

        registerFood(BlockHandler.PALEO_BALE_CYCADEOIDEA, FoodType.PLANT, 5000);
        registerFood(BlockHandler.PALEO_BALE_CYCAD, FoodType.PLANT, 5000);
        registerFood(BlockHandler.PALEO_BALE_FERN, FoodType.PLANT, 5000);
        registerFood(BlockHandler.PALEO_BALE_LEAVES, FoodType.PLANT, 5000);
        registerFood(BlockHandler.PALEO_BALE_OTHER, FoodType.PLANT, 5000);

        for (Plant plant : PlantHandler.getPlants())
        {
            registerFood(plant.getBlock(), FoodType.PLANT, plant.getHealAmount(), plant.getEffects());
        }

        for (TreeType type : TreeType.values())
        {
            registerFood(BlockHandler.ANCIENT_LEAVES.get(type), FoodType.PLANT, 2000);
            registerFood(BlockHandler.ANCIENT_SAPLINGS.get(type), FoodType.PLANT, 1000);
        }

        registerFood(Items.WHEAT, FoodType.PLANT, 1000);
        registerFood(Items.WHEAT_SEEDS, FoodType.PLANT, 100);
        registerFood(Items.MELON_SEEDS, FoodType.PLANT, 100);
        registerFood(Items.PUMPKIN_SEEDS, FoodType.PLANT, 100);

        registerFoodAuto((ItemFood) Items.FISH, FoodType.FISH);
        registerFoodAuto((ItemFood) Items.COOKED_FISH, FoodType.FISH);

        registerFoodAuto(ItemHandler.DINOSAUR_MEAT, FoodType.MEAT);
        registerFoodAuto(ItemHandler.DINOSAUR_STEAK, FoodType.MEAT);

        for (Item item : Item.REGISTRY)
        {
            if (item instanceof ItemFood)
            {
                ItemFood food = (ItemFood) item;

                registerFoodAuto(food, food.isWolfsFavoriteMeat() ? FoodType.MEAT : FoodType.PLANT);
            }
        }
    }

    public static void registerFoodAuto(ItemFood food, FoodType foodType, FoodEffect... effects)
    {
        registerFood(food, foodType, food.getHealAmount(new ItemStack(food)) * 650, effects);
    }

    public static void registerFood(Item food, FoodType foodType, int healAmount, FoodEffect... effects)
    {
        if (!FOODS.contains(food))
        {
            List<Item> foodsForType = FOOD_TYPES.get(foodType);

            if (foodsForType == null)
            {
                foodsForType = new ArrayList<>();
            }

            foodsForType.add(food);

            FOODS.add(food);
            FOOD_TYPES.put(foodType, foodsForType);
            HEAL_AMOUNTS.put(food, healAmount);
            FOOD_EFFECTS.put(food, effects);
        }
    }

    public static void registerFood(Block food, FoodType foodType, int foodAmount, FoodEffect... effects)
    {
        registerFood(Item.getItemFromBlock(food), foodType, foodAmount, effects);
    }

    public static List<Item> getFoodType(FoodType type)
    {
        return FOOD_TYPES.get(type);
    }

    public static FoodType getFoodType(Item item)
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

    public static FoodType getFoodType(Block block)
    {
        return getFoodType(Item.getItemFromBlock(block));
    }

    public static boolean isEdible(Diet diet, Item item)
    {
        return item != null && getEdibleFoods(diet).contains(item);
    }

    public static boolean isEdible(Diet diet, Block block)
    {
        return isEdible(diet, Item.getItemFromBlock(block));
    }

    public static List<Item> getEdibleFoods(Diet diet)
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

    public static int getHealAmount(Item item)
    {
        return HEAL_AMOUNTS.get(item);
    }

    public static void applyEatEffects(DinosaurEntity entity, Item item)
    {
        FoodEffect[] effects = FOOD_EFFECTS.get(item);

        if (effects != null)
        {
            for (FoodEffect effect : effects)
            {
                if (entity.getRNG().nextInt(100) <= effect.chance)
                {
                    entity.addPotionEffect(effect.effect);
                }
            }
        }
    }

    public static class FoodEffect
    {
        public PotionEffect effect;
        public int chance;

        public FoodEffect(PotionEffect effect, int chance)
        {
            this.effect = effect;
            this.chance = chance;
        }
    }
}
