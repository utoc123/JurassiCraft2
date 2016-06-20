package org.jurassicraft.server.world.tree;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.AncientLogBlock;
import org.jurassicraft.server.block.tree.TreeType;

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
        IBlockState log = BlockHandler.ANCIENT_LOGS.get(TreeType.GINKGO).getDefaultState();
        IBlockState leaves = BlockHandler.ANCIENT_LEAVES.get(TreeType.GINKGO).getDefaultState();

        int height = rand.nextInt(16) + 4;

        for (int y = 0; y < height; y++)
        {
            BlockPos logPos = position.up(y);
            world.setBlockState(logPos, log);

            int branchLength = Math.max(1, (height - y) / 3);

            if (y >= 2)
            {
                for (int x = -1; x <= 1; x++)
                {
                    for (int z = -1; z <= 1; z++)
                    {
                        if (x != 0 || z != 0)
                        {
                            world.setBlockState(logPos.add(x, 0, z), leaves);
                        }
                    }
                }

                int bushSize = (int) (branchLength * 0.8);

                for (int x = -bushSize; x <= bushSize; x++)
                {
                    for (int z = -bushSize; z <= bushSize; z++)
                    {
                        if ((x != 0 || z != 0) && Math.sqrt(x * x + z * z) < bushSize)
                        {
                            world.setBlockState(logPos.add(x, 0, z), leaves);
                        }
                    }
                }
            }

            if (y % 3 == 2)
            {
                for (int face = 0; face < 4; face++)
                {
                    EnumFacing facing = EnumFacing.getHorizontal(face);
                    BlockPos branchPos = logPos.offset(facing);
                    IBlockState facingLog = log.withProperty(AncientLogBlock.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));

                    world.setBlockState(branchPos, facingLog);

                    world.setBlockState(branchPos.up(2), leaves);
                    world.setBlockState(branchPos.down(), leaves);
                    world.setBlockState(branchPos.offset(facing.rotateY(), 2), leaves);
                    world.setBlockState(branchPos.offset(facing.rotateYCCW(), 2), leaves);

                    for (int i = 0; i < branchLength; i++)
                    {
                        BlockPos pos = branchPos.offset(facing, i + 1).up(i / 2 + 1);

                        world.setBlockState(pos, facingLog);
                        world.setBlockState(pos.up(), leaves);
                        world.setBlockState(pos.down(), leaves);
                        world.setBlockState(pos.offset(facing.rotateY()), leaves);
                        world.setBlockState(pos.offset(facing.rotateYCCW()), leaves);

                        if (i >= branchLength - 1)
                        {
                            world.setBlockState(pos.offset(facing), leaves);
                        }
                    }
                }
            }
        }

        world.setBlockState(position.up(height), leaves);
        world.setBlockState(position.up(height).north(), leaves);
        world.setBlockState(position.up(height).south(), leaves);
        world.setBlockState(position.up(height).west(), leaves);
        world.setBlockState(position.up(height).east(), leaves);
        world.setBlockState(position.up(height + 1), leaves);

        return true;
    }
}