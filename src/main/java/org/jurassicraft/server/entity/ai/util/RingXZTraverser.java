package org.jurassicraft.server.entity.ai.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Iterator;

/**
 * Copyright 2016 Andrew O. Mellinger
 */
public class RingXZTraverser implements Iterable<BlockPos>
{
    public RingXZTraverser(BlockPos center, int radius)
    {
        _center = center;
        _radius = radius;
    }

    @Override
    public Iterator<BlockPos> iterator()
    {
        return new RingIter(_radius);
    }

    private class RingIter implements Iterator<BlockPos>
    {
        public RingIter(int radius)
        {
            _x = _minX = _center.getX() - radius;
            _z = _minZ = _center.getZ() - radius;
            _y = _center.getY();
            _maxX = _center.getX() + radius;
            _maxZ = _center.getZ() + radius;
        }

        @Override
        public boolean hasNext()
        {
            return !_done;
        }

        @Override
        public BlockPos next()
        {
            BlockPos pos = new BlockPos(_x, _y, _z);
            walkPerimeter();
            return pos;
        }

        @Override
        public void remove()
        {
            // Nothing
        }

        // Walk around the outside perimeter
        private void walkPerimeter()
        {
            switch (_facing)
            {
                case EAST:
                    if (_x == _maxX)
                    {
                        ++_z;
                        _facing = EnumFacing.SOUTH;
                    }
                    else
                    {
                        ++_x;
                    }
                    break;

                case SOUTH:
                    if (_z == _maxZ)
                    {
                        --_x;
                        _facing = EnumFacing.WEST;
                    }
                    else
                    {
                        ++_z;
                    }
                    break;

                case WEST:
                    if (_x == _minX)
                    {
                        --_z;
                        _facing = EnumFacing.NORTH;
                    }
                    else
                    {
                        --_x;
                    }
                    break;

                case NORTH:
                    --_z;
                    if (_z == _minZ)
                    {
                        _done = true;
                    }
                    break;
            }
        }

        private EnumFacing _facing = EnumFacing.EAST;
        private int _x, _minX, _maxX;
        private int _y;
        private int _z, _minZ, _maxZ;
        private boolean _done = false;
    }

    private BlockPos _center;
    private final int _radius;
}
