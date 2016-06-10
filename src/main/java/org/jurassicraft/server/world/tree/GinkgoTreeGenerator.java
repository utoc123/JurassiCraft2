package org.jurassicraft.server.world.tree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class GinkgoTreeGenerator extends WorldGenAbstractTree
{
    public GinkgoTreeGenerator()
    {
        super(true);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position)
    {
        return false;
    }
}