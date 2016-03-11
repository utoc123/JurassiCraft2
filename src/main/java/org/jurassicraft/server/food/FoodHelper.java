package org.jurassicraft.server.food;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.entity.base.EnumDiet;
import org.jurassicraft.server.item.JCItemRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodHelper
{
    private static final Map<FoodType, List<Item>> itemFoods = new HashMap<FoodType, List<Item>>();
    private static final List<Item> allItemFoods = new ArrayList<Item>();

    private static final Map<FoodType, List<Block>> blockFoods = new HashMap<FoodType, List<Block>>();
    private static final List<Block> allBlockFoods = new ArrayList<Block>();

    // TODO:  Add food values for each.

    public static void init()
    {
        //blocks
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

        registerFood(JCBlockRegistry.bennettitalean_cycadeoidea, FoodType.PLANT);
        registerFood(JCBlockRegistry.cry_pansy, FoodType.PLANT);
        registerFood(JCBlockRegistry.cycad_zamites, FoodType.PLANT);
        registerFood(JCBlockRegistry.dicksonia, FoodType.PLANT);
        registerFood(JCBlockRegistry.scaly_tree_fern, FoodType.PLANT);
        registerFood(JCBlockRegistry.small_chain_fern, FoodType.PLANT);
        registerFood(JCBlockRegistry.small_cycad, FoodType.PLANT);
        registerFood(JCBlockRegistry.small_royal_fern, FoodType.PLANT);
        for(int i = 0;i < JCBlockRegistry.saplings.length;i++)
        {
            registerFood(JCBlockRegistry.leaves[i], FoodType.PLANT);
            registerFood(JCBlockRegistry.saplings[i], FoodType.PLANT);
        }

        //items
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
                        registerFood(food, FoodType.PLANT);                  
                    else
                        registerFood(food, FoodType.MEAT);
                }
            }
        }
    }

    public static void registerFood(Item food, FoodType foodType)
    {
        if (!allItemFoods.contains(food))
        {
            List<Item> foodsForType = itemFoods.get(foodType);

            if (foodsForType == null)
            {
                foodsForType = new ArrayList<Item>();
            }

            foodsForType.add(food);

            allItemFoods.add(food);

            itemFoods.put(foodType, foodsForType);
        }
    }

    public static void registerFood(Block food, FoodType foodType)
    {
        if(!allBlockFoods.contains(food))
        {
            List<Block> foodsForType = blockFoods.get(foodType);

            if(foodsForType == null)
            {
                foodsForType = new ArrayList<Block>();
            }

            foodsForType.add(food);

            allBlockFoods.add(food);

            blockFoods.put(foodType, foodsForType);
        }
    }//end of overloaded registerFood(block)

    public static List<Item> getItemFoodType(FoodType type)
    {
        return itemFoods.get(type);
    }
    public static List<Block> getBlockFoodType(FoodType type)
    {
        return blockFoods.get(type);
    }

    public static FoodType getFoodType(Item item)
    {
        for (FoodType foodType : FoodType.values())
        {
            if (getItemFoodType(foodType).contains(item))
            {
                return foodType;
            }
        }

        return null;
    }
    public static FoodType getFoodType(Block block)
    {
        for(FoodType foodType : FoodType.values())
        {
            if(getBlockFoodType(foodType).contains(block))
                return foodType;
        }

        return null;
    }//end of overloaded getFoodType(block)


    public static boolean canDietEat(EnumDiet diet, Item item)
    {
        return getItemFoodsForDiet(diet).contains(item);
    }
    public static boolean canDietEat(EnumDiet diet, Block block)
    {
        return getBlockFoodsForDiet(diet).contains(block);
    }//end of overloaded canDietEat(block)

    private static List<Item> getItemFoodsForDiet(EnumDiet diet)
    {
        List<Item> possibleItems = new ArrayList<Item>();

        if (diet.doesEatPlants())
        {
            possibleItems.addAll(getItemFoodType(FoodType.PLANT));
        }

        if (diet.doesEatFish())
        {
            possibleItems.addAll(getItemFoodType(FoodType.FISH));
        }

        if (diet.doesEatMeat())
        {
            possibleItems.addAll(getItemFoodType(FoodType.MEAT));
        }

        return possibleItems;
    }
    private static List<Block> getBlockFoodsForDiet(EnumDiet diet)
    {
        List<Block> possibleBlocks = new ArrayList<Block>();

        if (diet.doesEatPlants())
        {
            possibleBlocks.addAll(getBlockFoodType(FoodType.PLANT));
        }

        //guess I'll keep these here in case fish/meat blocks are implemented...
        //if (diet.doesEatFish())
        //{
        //    possibleBlocks.addAll(getBlockFoodType(FoodType.FISH));
        //}

        //if (diet.doesEatMeat())
        //{
        //    possibleBlocks.addAll(getBlockFoodType(FoodType.MEAT));
        //}

        return possibleBlocks;
    }
}
