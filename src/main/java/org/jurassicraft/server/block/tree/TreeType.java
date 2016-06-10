package org.jurassicraft.server.block.tree;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.world.tree.CalamitesTreeGenerator;
import org.jurassicraft.server.world.tree.GinkgoTreeGenerator;

public enum TreeType
{
    GINKGO(PlantHandler.INSTANCE.GINKGO, new GinkgoTreeGenerator()), CALAMITES(PlantHandler.INSTANCE.CALAMITES, new CalamitesTreeGenerator());

    private WorldGenAbstractTree generator;
    private Plant plant;

    TreeType(Plant plant, WorldGenAbstractTree generator)
    {
        this.plant = plant;
        this.generator = generator;
    }

    public WorldGenAbstractTree getTreeGenerator()
    {
        return generator;
    }

    public Plant getPlant()
    {
        return plant;
    }
}
