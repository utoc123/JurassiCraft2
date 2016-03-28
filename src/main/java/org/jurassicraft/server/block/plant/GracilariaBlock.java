package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

/**
 * Copyright 2016 Andrew O. Mellinger
 */
public class GracilariaBlock extends BlockBush
{
    /**
     * DESIGN:
     * <p>
     * This stuff spreads like mushrooms.  It grows on sand or clay and periodically
     * will spread to some more sand or clay.  It will not spread if it reaches a
     * certain density within a 9x9 area.
     * <p>
     * It will spread quickly if within 5-11 range, slowly otherwise.
     */

    private static final int DENSITY_PER_4X4 = 8;
    private static final int GOOD_LIGHT_SPREAD_CHANCE = 25;
    private static final int BAD_LIGHT_SPREAD_CHANCE = 2;
    private static final int SPREAD_RADIUS = 4;

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.8F, 0.4F, 0.8F);

    public GracilariaBlock()
    {
        super(Material.coral);
        this.setCreativeTab(null);
        this.setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    //  ____  _            _
    // | __ )| | ___   ___| | __
    // |  _ \| |/ _ \ / __| |/ /
    // | |_) | | (_) | (__|   <
    // |____/|_|\___/ \___|_|\_\

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ItemHandler.INSTANCE.gracilaria;
    }

    //  ____  _            _    ____            _
    // | __ )| | ___   ___| | _| __ ) _   _ ___| |__
    // |  _ \| |/ _ \ / __| |/ /  _ \| | | / __| '_ \
    // | |_) | | (_) | (__|   <| |_) | |_| \__ \ | | |
    // |____/|_|\___/ \___|_|\_\____/ \__,_|___/_| |_|

    private boolean canPlaceBlockOn(Block down)
    {
        return down == Blocks.sand || down == Blocks.clay;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        // Place on sand/clay, water here, water up
        Block down = worldIn.getBlockState(pos.down()).getBlock();
        Block here = worldIn.getBlockState(pos).getBlock();
        Block up = worldIn.getBlockState(pos.up()).getBlock();

        return canPlaceBlockOn(down) && here == Blocks.water && up == Blocks.water;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        // Stay on sand/clay with water up
        Block down = worldIn.getBlockState(pos.down()).getBlock();
        Block up = worldIn.getBlockState(pos.up()).getBlock();

        return canPlaceBlockOn(down) && up == Blocks.water;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        // Make sure we have enough light.
        int spreadChance = BAD_LIGHT_SPREAD_CHANCE;
        int light = worldIn.getLight(pos);
        if (light >= 5 && light <= 11)
        {
            spreadChance = GOOD_LIGHT_SPREAD_CHANCE;
        }

        if (rand.nextInt(100) <= spreadChance)
        {
            // Density check
            int i = DENSITY_PER_4X4;

            // We only allow so many around us before we move one.
            for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-SPREAD_RADIUS, -3, -SPREAD_RADIUS), pos.add(SPREAD_RADIUS, 3, SPREAD_RADIUS)))
            {
                // Count how many
                if (worldIn.getBlockState(blockpos).getBlock() == this)
                {
                    --i;
                    if (i <= 0)
                    {
                        return;
                    }
                }
            }

            // Choose a location then find the surface.
            BlockPos nextPos = null;
            int placementAttempts = 3;

            while (nextPos == null && placementAttempts > 0)
            {
                // Chose a random location
                int doubleRadius = SPREAD_RADIUS * 2;
                BlockPos tmp = pos.add(rand.nextInt(doubleRadius) - SPREAD_RADIUS, -SPREAD_RADIUS,
                        rand.nextInt(doubleRadius) - SPREAD_RADIUS);
                nextPos = findGround(worldIn, tmp);
                --placementAttempts;
            }

            if (nextPos != null)
            {
                worldIn.setBlockState(nextPos, this.getDefaultState());
            }
        }
    }

    //             _            _
    //  _ __  _ __(_)_   ____ _| |_ ___
    // | '_ \| '__| \ \ / / _` | __/ _ \
    // | |_) | |  | |\ V / (_| | ||  __/
    // | .__/|_|  |_| \_/ \__,_|\__\___|
    // |_|

    private BlockPos findGround(World worldIn, BlockPos start)
    {
        BlockPos pos = start;

        // Search a column
        Block down = worldIn.getBlockState(pos.down()).getBlock();
        Block here = worldIn.getBlockState(pos).getBlock();
        Block up = worldIn.getBlockState(pos.up()).getBlock();

        for (int i = 0; i < 8; ++i)
        {
            if (canPlaceBlockOn(down) && here == Blocks.water && up == Blocks.water)
            {
                return pos;
            }

            down = here;
            here = up;
            pos = pos.up();
            up = worldIn.getBlockState(pos.up()).getBlock();
        }

        return null;
    }

}
