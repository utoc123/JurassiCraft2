package org.jurassicraft.common.entity.ai.util;

import net.minecraft.util.BlockPos;
import org.junit.Test;

import static org.junit.Assert.*;

public class AIUtilsTest
{
    @Test
    public void testFindIntersect() throws Exception
    {
        BlockPos center = new BlockPos(100, 0, 100);
        BlockPos start  = new BlockPos(75, 0, 100);
        BlockPos pos = AIUtils.findIntersect(center, 4, start);

        assertEquals("X", 96, pos.getX());
        assertEquals("Y", 0, pos.getY());
        assertEquals("Z", 100, pos.getZ());

        center = new BlockPos(100, 0, 100);
        start  = new BlockPos(75, 0, 75);
        pos = AIUtils.findIntersect(center, 4, start);

        assertEquals("X", 97, pos.getX());
        assertEquals("Y", 0, pos.getY());
        assertEquals("Z", 97, pos.getZ());

        center = new BlockPos(100, 0, 100);
        start  = new BlockPos(200, 0, 200);
        pos = AIUtils.findIntersect(center, 4, start);

        assertEquals("X", 103, pos.getX());
        assertEquals("Y", 0, pos.getY());
        assertEquals("Z", 103, pos.getZ());
    }

    @Test
    public void testPlotCircle() throws Exception
    {
        AIUtils.plotCircle(100, 100, 10);

    }
}