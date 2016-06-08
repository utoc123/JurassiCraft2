package org.jurassicraft.server.block.tree;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.world.jurdstrees.trees.WorldGenJCTree;

public enum TreeType
{
    GINKGO(PlantHandler.INSTANCE.GINKGO), CALAMITES(PlantHandler.INSTANCE.CALAMITES);

    private Plant plant;

    TreeType(Plant plant)
    {
        this.plant = plant;
    }

    public WorldGenAbstractTree getTreeGenerator()
    {
        return new WorldGenJCTree(this);
    }

    public Plant getPlant()
    {
        return plant;
    }
}
