package org.jurassicraft.server.world.jurdstrees.trees;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.world.jurdstrees.algorythms.TreeGenerator;

import java.util.Random;

/**
 * Created by Jordi on 12/08/2015.
 */
public class WorldGenJCTree extends WorldGenAbstractTree
{
    private TreeType type;

    public WorldGenJCTree(TreeType type)
    {
        super(true);
        this.type = type;
    }

    @Override
    public boolean generate(World worldIn, Random random, BlockPos pos)
    {
        TreeGenerator generator = new TreeGenerator(type, worldIn, pos);
        generator.placeTree();

        return true;
    }
}
