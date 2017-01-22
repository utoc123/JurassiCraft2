package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import net.minecraft.init.PotionTypes;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.food.FoodHelper;

/**
 * Created by Codyr on 22/01/2017.
 */
public class HeliconiaPlant extends Plant {
    @Override
    public String getName() {
        return "Heliconia";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.HELICONIA;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }

    @Override
    public boolean isPrehistoric() {
        return false;
    }
}
