package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import net.minecraft.init.PotionTypes;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.food.FoodHelper;

public class PsaroniusPlant extends Plant {
    @Override
    public String getName() {
        return "Psaronius";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.ANCIENT_SAPLINGS.get(TreeType.PSARONIUS);
    }

    @Override
    public int getHealAmount() {
        return 1000;
    }
}
