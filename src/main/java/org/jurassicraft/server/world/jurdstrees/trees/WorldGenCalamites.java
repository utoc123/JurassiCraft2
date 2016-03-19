package org.jurassicraft.server.world.jurdstrees.trees;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import org.jurassicraft.server.world.jurdstrees.algorythms.TreeGenerator;

import java.util.Random;

/**
 * Created by Jordi on 12/08/2015.
 */
public class WorldGenCalamites extends WorldGenAbstractTree
{
    private int code;

    public WorldGenCalamites(int code)
    {

        super(true);
        this.code = code;

    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos)
    {
        TreeGenerator generator = new TreeGenerator(code, world, pos);
        generator.placeTree();

        return true;

    }
}
