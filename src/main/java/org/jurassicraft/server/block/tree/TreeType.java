package org.jurassicraft.server.block.tree;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import org.jurassicraft.server.world.jurdstrees.trees.WorldGenJCTree;

public enum TreeType
{
    GINKGO, CALAMITES;

    public WorldGenAbstractTree getTreeGenerator()
    {
        return new WorldGenJCTree(this);
    }
}
