package org.jurassicraft.server.entity.ai.util;

import net.minecraft.util.BlockPos;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RingXZTraverserTest
{
    @Test
    public void testIterator() throws Exception
    {
        BlockPos center = new BlockPos( 100, 0, 200 );
        RingXZTraverser traverser = new RingXZTraverser(center, 4);

        List<BlockPos> values = new ArrayList<BlockPos>();

        values.add( new BlockPos(96, 0, 196));
        values.add( new BlockPos(97, 0, 196));
        values.add( new BlockPos(98, 0, 196));
        values.add( new BlockPos(99, 0, 196));
        values.add( new BlockPos(100, 0, 196));
        values.add( new BlockPos(101, 0, 196));
        values.add( new BlockPos(102, 0, 196));
        values.add( new BlockPos(103, 0, 196));
        values.add( new BlockPos(104, 0, 196));
        values.add( new BlockPos(104, 0, 197));
        values.add( new BlockPos(104, 0, 198));
        values.add( new BlockPos(104, 0, 199));
        values.add( new BlockPos(104, 0, 200));
        values.add( new BlockPos(104, 0, 201));
        values.add( new BlockPos(104, 0, 202));
        values.add( new BlockPos(104, 0, 203));
        values.add( new BlockPos(104, 0, 204));
        values.add( new BlockPos(103, 0, 204));
        values.add( new BlockPos(102, 0, 204));
        values.add( new BlockPos(101, 0, 204));
        values.add( new BlockPos(100, 0, 204));
        values.add( new BlockPos(99, 0, 204));
        values.add( new BlockPos(98, 0, 204));
        values.add( new BlockPos(97, 0, 204));
        values.add( new BlockPos(96, 0, 204));
        values.add( new BlockPos(96, 0, 203));
        values.add( new BlockPos(96, 0, 202));
        values.add( new BlockPos(96, 0, 201));
        values.add( new BlockPos(96, 0, 200));
        values.add( new BlockPos(96, 0, 199));
        values.add( new BlockPos(96, 0, 198));
        values.add( new BlockPos(96, 0, 197));

        int idx = 0;
        for (BlockPos pos : traverser)
        {
            assertEquals(pos, values.get(idx));
            // System.out.println("values.add( new BlockPos(" + pos.getX() + ", " +
            //        pos.getY() + ", " + pos.getZ() + "));");
            idx++;
        }
    }

}