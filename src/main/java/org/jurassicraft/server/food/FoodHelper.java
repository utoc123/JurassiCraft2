package org.jurassicraft.server.food;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.item.JCItemRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodHelper
{
    private static final Map<FoodType, List<Item>> foods = new HashMap<FoodType, List<Item>>();
    private static final List<Item> allFoods = new ArrayList<Item>();

    // TODO:  Add food values for each.
    // TODO:  Add blocks (like leaves/foliage) for herbivores.

    public static void init()
    {
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

        registerFood(JCItemRegistry.dino_meat, FoodType.MEAT);
        registerFood(JCItemRegistry.dino_steak, FoodType.MEAT);

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

    public static void registerFood(Item food, FoodType foodType)
    {
        if (!allFoods.contains(food))
        {
            List<Item> foodsForType = foods.get(foodType);

            if (foodsForType == null)
            {
                foodsForType = new ArrayList<Item>();
            }

            foodsForType.add(food);

            allFoods.add(food);

            foods.put(foodType, foodsForType);
        }
    }

    public static List<Item> getFoodsForFoodType(FoodType type)
    {
        return foods.get(type);
    }

    public static FoodType getFoodType(Item item)
    {
        for (FoodType foodType : FoodType.values())
        {
            if (getFoodsForFoodType(foodType).contains(item))
            {
                return foodType;
            }
        }

        return null;
    }

    public static boolean canDietEat(EnumDiet diet, Item item)
    {
        return getFoodsForDiet(diet).contains(item);
    }

    private static List<Item> getFoodsForDiet(EnumDiet diet)
    {
        List<Item> possibleItems = new ArrayList<Item>();

        if (diet.doesEatPlants())
        {
            possibleItems.addAll(getFoodsForFoodType(FoodType.PLANT));
        }

        if (diet.doesEatFish())
        {
            possibleItems.addAll(getFoodsForFoodType(FoodType.FISH));
        }

        if (diet.doesEatMeat())
        {
            possibleItems.addAll(getFoodsForFoodType(FoodType.MEAT));
        }

        return possibleItems;
    }
}
