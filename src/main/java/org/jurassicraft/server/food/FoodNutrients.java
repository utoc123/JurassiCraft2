package org.jurassicraft.server.food;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public enum FoodNutrients {
    APPLE(Items.APPLE, 0.060D, 0.065D, 0.100D, 0.010D),
    POTATO(Items.POTATO, 0.100D, 0.200D, 0.160D, 0.020D),
    BREAD(Items.BREAD, 0.300D, 0.400D, 0.430D, 0.180D),
    CHICKEN(Items.CHICKEN, 0.390D, 0.350D, 0.280D, 0.450D),
    COOKED_CHICKEN(Items.COOKED_CHICKEN, 0.490D, 0.425D, 0.335D, 0.555D),
    PORKCHOP(Items.PORKCHOP, 0.460D, 0.310D, 0.390D, 0.380D),
    COOKED_PORKCHOP(Items.COOKED_PORKCHOP, 0.580D, 0.390D, 0.490D, 0.470D),
    BEEF(Items.BEEF, 0.460D, 0.310D, 0.390D, 0.380D),
    COOKED_BEEF(Items.COOKED_BEEF, 0.520D, 0.330D, 0.410D, 0.400D),
    FISH(Items.FISH, 0.480D, 0.430D, 0.140D, 0.240D),
    COOKED_FISH(Items.COOKED_FISH, 0.500D, 0.450D, 0.200D, 0.280D),
    MILK(Items.MILK_BUCKET, 0.180D, 0.260D, 0.220D, 0.600D),
    EGG(Items.EGG, 0.050D, 0.030D, 0.050D, 0.250D),
    CARROT(Items.CARROT, 0.070D, 0.170D, 0.350D, 0.010D),
    SUGAR(Items.SUGAR, 0.200D, 0.010D, 0.010D, 0.010D),
    MELON(Items.MELON, 0.060D, 0.060D, 0.060D, 0.010D),
    WHEAT(Items.WHEAT, 0.100D, 0.220D, 0.100D, 0.030D),
    MUTTON(Items.MUTTON, 0.460D, 0.310D, 0.390D, 0.380D),
    COOKED_MUTTON(Items.COOKED_MUTTON, 0.580D, 0.390D, 0.490D, 0.470D);

    public static final Map<Item, Integer> FOOD_LIST = new HashMap<>();

    static {
        for (int i = 0; i < values().length; i++) {
            FOOD_LIST.put(FoodNutrients.values()[i].getFoodItem(), i);
        }
    }

    private final double proximate;
    private final double minerals;
    private final double vitamins;
    private final double lipids;
    private final Item food;

    FoodNutrients(Item food, double foodProximates, double foodMinerals, double foodVitamins, double foodLipids) {
        this.food = food;
        this.proximate = foodProximates;
        this.minerals = foodMinerals;
        this.vitamins = foodVitamins;
        this.lipids = foodLipids;
    }

    public Item getFoodItem() {
        return this.food;
    }

    public double getProximate() {
        return this.proximate;
    }

    public double getMinerals() {
        return this.minerals;
    }

    public double getVitamins() {
        return this.vitamins;
    }

    public double getLipids() {
        return this.lipids;
    }
}
