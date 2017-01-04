package org.jurassicraft.server.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.achievements.AchievementHandler;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.FossilizedTrackwayBlock;
import org.jurassicraft.server.block.plant.DoublePlantBlock;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.util.GameRuleHandler;
import org.jurassicraft.server.world.WorldGenCoal;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ServerEventHandler {
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        GameRuleHandler.register(event.getWorld());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.pickedUp.getEntityItem().getItem() == ItemHandler.AMBER) {
            event.player.addStat(AchievementHandler.AMBER, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onCraft(PlayerEvent.ItemCraftedEvent event) {
        Item item = event.crafting.getItem();

        if (item == ItemHandler.PLASTER_AND_BANDAGE) {
            event.player.addStat(AchievementHandler.PALEONTOLOGY, 1);
        } else if (item == Item.getItemFromBlock(BlockHandler.CLEANING_STATION)) {
            event.player.addStat(AchievementHandler.CLEANING_STATION, 1);
        } else if (item == Item.getItemFromBlock(BlockHandler.FOSSIL_GRINDER)) {
            event.player.addStat(AchievementHandler.FOSSIL_GRINDER, 1);
        } else if (item == Item.getItemFromBlock(BlockHandler.REINFORCED_STONE)) {
            event.player.addStat(AchievementHandler.REINFORCED_STONE, 1);
        } else if (item == Item.getItemFromBlock(BlockHandler.REINFORCED_BRICKS)) {
            event.player.addStat(AchievementHandler.REINFORCED_STONE, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void decorate(DecorateBiomeEvent.Pre event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Random rand = event.getRand();

        Biome biome = world.getBiome(pos);

        BiomeDecorator decorator = biome.theBiomeDecorator;

        if (JurassiCraft.CONFIG.plantFossilGeneration) {
            if (decorator != null && decorator.chunkProviderSettings != null && !(decorator.coalGen instanceof WorldGenCoal)) {
                decorator.coalGen = new WorldGenCoal(Blocks.COAL_ORE.getDefaultState(), decorator.chunkProviderSettings.coalSize);
            }
        }

        if (JurassiCraft.CONFIG.mossGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);

                    if (world.getBlockState(topBlock.down()).isOpaqueCube() && !world.getBlockState(topBlock).getMaterial().isLiquid()) {
                        world.setBlockState(topBlock, BlockHandler.MOSS.getDefaultState(), 2);
                    }
                }
            }
        }

        if (JurassiCraft.CONFIG.flowerGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                    if (world.getBlockState(topBlock.down()).isOpaqueCube() && !world.getBlockState(topBlock).getMaterial().isLiquid()) {
                        world.setBlockState(topBlock.up(), BlockHandler.WEST_INDIAN_LILAC.getDefaultState(), 2);
                        world.setBlockState(topBlock, BlockHandler.WEST_INDIAN_LILAC.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.LOWER), 2);
                    }
                }
            }
        }

        if (JurassiCraft.CONFIG.gracilariaGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);

                    if (topBlock.getY() < 62) {
                        IBlockState state = world.getBlockState(topBlock.down());

                        if (state.isOpaqueCube()) {
                            world.setBlockState(topBlock, BlockHandler.GRACILARIA.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        if (JurassiCraft.CONFIG.peatGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                if (rand.nextInt(2) == 0) {
                    new WorldGenMinable(BlockHandler.PEAT.getDefaultState(), 5, input -> input == Blocks.DIRT.getDefaultState() || input == Blocks.GRASS.getDefaultState()).generate(world, rand, world.getTopSolidOrLiquidBlock(pos));
                }
            }
        }

        if (JurassiCraft.CONFIG.trackwayGeneration) {
            int footprintChance = 20;

            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {
                footprintChance = 10;
            }

            if (rand.nextInt(footprintChance) == 0) {
                int y = rand.nextInt(20) + 30;

                FossilizedTrackwayBlock.TrackwayType type = FossilizedTrackwayBlock.TrackwayType.values()[rand.nextInt(FossilizedTrackwayBlock.TrackwayType.values().length)];

                for (int i = 0; i < rand.nextInt(2) + 1; i++) {
                    BlockPos basePos = new BlockPos(pos.getX() + rand.nextInt(10) - 5, y, pos.getZ() + rand.nextInt(10) - 5);

                    float angle = (float) (rand.nextDouble() * 360.0F);

                    IBlockState trackway = BlockHandler.FOSSILIZED_TRACKWAY.getDefaultState().withProperty(FossilizedTrackwayBlock.FACING, EnumFacing.fromAngle(angle)).withProperty(FossilizedTrackwayBlock.VARIANT, type);

                    float xOffset = -MathHelper.sin((float) Math.toRadians(angle));
                    float zOffset = MathHelper.cos((float) Math.toRadians(angle));

                    for (int l = 0; l < rand.nextInt(2) + 3; l++) {
                        BlockPos trackwayPos = basePos.add(xOffset * l, 0, zOffset * l);

                        if (world.getBlockState(trackwayPos).getBlock() == Blocks.STONE) {
                            world.setBlockState(trackwayPos, trackway);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();

        LootTable table = event.getTable();

        if (name == LootTableList.GAMEPLAY_FISHING) {
            LootEntry[] entries = new LootEntry[] { new LootEntryItem(ItemHandler.GRACILARIA, 25, 0, new LootFunction[0], new LootCondition[0], "gracilaria") };
            LootPool pool = new LootPool(entries, new LootCondition[0], new RandomValueRange(1, 1), new RandomValueRange(0, 0), "jurassicraft");
            table.addPool(pool);
        } else if (name == LootTableList.CHESTS_VILLAGE_BLACKSMITH || name == LootTableList.CHESTS_NETHER_BRIDGE || name == LootTableList.CHESTS_SIMPLE_DUNGEON || name == LootTableList.CHESTS_STRONGHOLD_CORRIDOR || name == LootTableList.CHESTS_DESERT_PYRAMID || name == LootTableList.CHESTS_ABANDONED_MINESHAFT) {
            List<Dinosaur> dinosaurs = EntityHandler.getRegisteredDinosaurs();

            LootEntry[] actionFigureEntries = new LootEntry[dinosaurs.size()];

            int i = 0;

            for (Dinosaur dinosaur : dinosaurs) {
                int meta = EntityHandler.getDinosaurId(dinosaur);
                actionFigureEntries[i++] = new LootEntryItem(ItemHandler.ACTION_FIGURE, 25, 0, new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(meta, meta)) }, new LootCondition[0], dinosaur.getName().toLowerCase(Locale.ENGLISH));
            }

            table.addPool(new LootPool(actionFigureEntries, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(0, 0), "action_figures"));

            LootEntry[] fossilEntries = new LootEntry[dinosaurs.size() + 3];

            fossilEntries[0] = new LootEntryItem(ItemHandler.PLANT_FOSSIL, 20, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(1, 3)) }, new LootCondition[0], "plant_fossil");
            fossilEntries[1] = new LootEntryItem(ItemHandler.TWIG_FOSSIL, 20, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(1, 3)) }, new LootCondition[0], "twig_fossil");
            fossilEntries[2] = new LootEntryItem(ItemHandler.AMBER, 15, 0, new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(0, 1)) }, new LootCondition[0], "amber");

            i = 3;

            for (Dinosaur dinosaur : dinosaurs) {
                int meta = EntityHandler.getDinosaurId(dinosaur);
                fossilEntries[i++] = new LootEntryItem(ItemHandler.FOSSILS.get("skull"), 10, 0, new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(meta, meta)) }, new LootCondition[0], dinosaur.getName().toLowerCase(Locale.ENGLISH));
            }

            table.addPool(new LootPool(fossilEntries, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(0, 0), "fossils"));

            LootEntry[] recordEntries = new LootEntry[] { new LootEntryItem(ItemHandler.JURASSICRAFT_THEME_DISC, 25, 0, new LootFunction[0], new LootCondition[0], "jurassicraft_theme"), new LootEntryItem(ItemHandler.DONT_MOVE_A_MUSCLE_DISC, 25, 0, new LootFunction[0], new LootCondition[0], "dont_move_a_muscle"), new LootEntryItem(ItemHandler.TROODONS_AND_RAPTORS_DISC, 25, 0, new LootFunction[0], new LootCondition[0], "troodons_and_raptors") };

            table.addPool(new LootPool(recordEntries, new LootCondition[0], new RandomValueRange(0, 2), new RandomValueRange(0, 0), "records"));
        }
    }
}
