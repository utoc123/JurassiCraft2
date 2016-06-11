package org.jurassicraft.server.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jurassicraft.server.achievements.AchievementHandler;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.FossilizedTrackwayBlock;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.world.WorldGenCoal;

import java.util.Random;

public class ServerEventHandler
{
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        GameRules gameRules = event.getWorld().getGameRules();

        registerGameRule(gameRules, "dinoMetabolism", true);
        registerGameRule(gameRules, "dinoGrowth", true);
        registerGameRule(gameRules, "dinoHerding", false);
    }

    private void registerGameRule(GameRules gameRules, String name, boolean value)
    {
        if (!gameRules.hasRule(name))
        {
            gameRules.addGameRule(name, value + "", GameRules.ValueType.BOOLEAN_VALUE);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            player.addStat(AchievementHandler.INSTANCE.jurassicraft, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onItemPickup(PlayerEvent.ItemPickupEvent event)
    {
        if (event.pickedUp.getEntityItem().getItem() == ItemHandler.INSTANCE.AMBER)
        {
            event.player.addStat(AchievementHandler.INSTANCE.amber, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onCraft(PlayerEvent.ItemCraftedEvent event)
    {
        Item item = event.crafting.getItem();

        if (item == ItemHandler.INSTANCE.PLASTER_AND_BANDAGE)
        {
            event.player.addStat(AchievementHandler.INSTANCE.paleontology, 1);
        }
        else if (item == Item.getItemFromBlock(BlockHandler.INSTANCE.CLEANING_STATION))
        {
            event.player.addStat(AchievementHandler.INSTANCE.cleaningStation, 1);
        }
        else if (item == Item.getItemFromBlock(BlockHandler.INSTANCE.FOSSIL_GRINDER))
        {
            event.player.addStat(AchievementHandler.INSTANCE.fossilGrinder, 1);
        }
        else if (item == Item.getItemFromBlock(BlockHandler.INSTANCE.REINFORCED_STONE))
        {
            event.player.addStat(AchievementHandler.INSTANCE.reinforcedStone, 1);
        }
        else if (item == Item.getItemFromBlock(BlockHandler.INSTANCE.REINFORCED_BRICKS))
        {
            event.player.addStat(AchievementHandler.INSTANCE.reinforcedStone, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void decorate(DecorateBiomeEvent.Pre event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Random rand = event.getRand();

        Biome biome = world.getBiomeGenForCoords(pos);

        BiomeDecorator decorator = biome.theBiomeDecorator;

        if (decorator != null && decorator.chunkProviderSettings != null && !(decorator.coalGen instanceof WorldGenCoal))
        {
            decorator.coalGen = new WorldGenCoal(Blocks.COAL_ORE.getDefaultState(), decorator.chunkProviderSettings.coalSize);
        }

        if (biome == Biomes.FOREST || biome == Biomes.BIRCH_FOREST || biome == Biomes.TAIGA || biome == Biomes.REDWOOD_TAIGA || biome == Biomes.SWAMPLAND || biome == Biomes.JUNGLE)
        {
            if (rand.nextInt(8) == 0)
            {
                BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                IBlockState state = world.getBlockState(topBlock.down());

                if (state.isOpaqueCube())
                {
                    world.setBlockState(topBlock, BlockHandler.INSTANCE.MOSS.getDefaultState(), 2);
                }
            }
        }

        if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN)
        {
            if (rand.nextInt(8) == 0)
            {
                BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);

                if (topBlock.getY() < 62)
                {
                    IBlockState state = world.getBlockState(topBlock.down());

                    if (state.isOpaqueCube())
                    {
                        world.setBlockState(topBlock, BlockHandler.INSTANCE.GRACILARIA.getDefaultState(), 2);
                    }
                }
            }
        }

        if (biome == Biomes.SWAMPLAND)
        {
            if (rand.nextInt(2) == 0)
            {
                new WorldGenMinable(BlockHandler.INSTANCE.PEAT.getDefaultState(), 5, input -> input == Blocks.DIRT.getDefaultState() || input == Blocks.GRASS.getDefaultState()).generate(world, rand, world.getTopSolidOrLiquidBlock(pos));
            }
        }

        int footprintChance = 20;

        if (biome == Biomes.RIVER)
        {
            footprintChance = 10;
        }

        if (rand.nextInt(footprintChance) == 0)
        {
            int y = rand.nextInt(20) + 30;

            FossilizedTrackwayBlock.TrackwayType type = FossilizedTrackwayBlock.TrackwayType.values()[rand.nextInt(FossilizedTrackwayBlock.TrackwayType.values().length)];

            for (int i = 0; i < rand.nextInt(2) + 1; i++)
            {
                BlockPos basePos = new BlockPos(pos.getX() + rand.nextInt(10) - 5, y, pos.getZ() + rand.nextInt(10) - 5);

                float angle = (float) (rand.nextDouble() * 360.0F);

                IBlockState trackway = BlockHandler.INSTANCE.FOSSILIZED_TRACKWAY.getDefaultState().withProperty(FossilizedTrackwayBlock.FACING, EnumFacing.fromAngle(angle)).withProperty(FossilizedTrackwayBlock.VARIANT, type);

                float xOffset = -MathHelper.sin((float) Math.toRadians(angle));
                float zOffset = MathHelper.cos((float) Math.toRadians(angle));

                for (int l = 0; l < rand.nextInt(2) + 3; l++)
                {
                    BlockPos trackwayPos = basePos.add(xOffset * l, 0, zOffset * l);

                    if (world.getBlockState(trackwayPos).getBlock() == Blocks.STONE)
                    {
                        world.setBlockState(trackwayPos, trackway);
                    }
                }
            }
        }
    }
}
