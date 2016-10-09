package org.jurassicraft.server.world.structure;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StructureGenerationHandler implements IWorldGenerator {
    private static final Map<Biome, List<GeneratorEntry>> GENERATORS = new HashMap<>();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) {
            if (random.nextInt(100) == 0) {
                int blockX = (chunkX << 4) + random.nextInt(16);
                int blockZ = (chunkZ << 4) + random.nextInt(16);
                BlockPos pos = new BlockPos(blockX, 0, blockZ);
                Biome biome = world.getBiomeForCoordsBody(pos);
                List<GeneratorEntry> entries = GENERATORS.get(biome);
                if (entries != null) {
                    int totalWeight = 0;
                    for (GeneratorEntry entry : entries) {
                        totalWeight += entry.weight;
                    }
                    int selection = random.nextInt(totalWeight);
                    GeneratorEntry generatorEntry = null;
                    for (GeneratorEntry entry : entries) {
                        selection -= entry.weight;
                        if (selection <= 0) {
                            generatorEntry = entry;
                            break;
                        }
                    }
                    if (generatorEntry != null) {
                        try {
                            StructureGenerator generator = generatorEntry.generator.getConstructor(Random.class).newInstance(random);
                            generator.generate(world, random, pos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void register() {
        GameRegistry.registerWorldGenerator(new StructureGenerationHandler(), 0);
        StructureGenerationHandler.registerGenerator(RaptorPaddockGenerator.class, 5, Biomes.JUNGLE, Biomes.MUTATED_JUNGLE, Biomes.JUNGLE_EDGE, Biomes.MUTATED_JUNGLE_EDGE);
    }

    public static void registerGenerator(Class<? extends StructureGenerator> generator, int weight, Biome... validBiomes) {
        GeneratorEntry entry = new GeneratorEntry(generator, validBiomes, weight);
        for (Biome biome : validBiomes) {
            StructureGenerationHandler.addEntry(biome, entry);
        }
    }

    private static void addEntry(Biome biome, GeneratorEntry generator) {
        List<GeneratorEntry> entries = GENERATORS.get(biome);
        if (entries == null) {
            entries = new ArrayList<>();
            GENERATORS.put(biome, entries);
        }
        entries.add(generator);
    }

    private static class GeneratorEntry {
        private Class<? extends StructureGenerator> generator;
        private int weight;
        private Biome[] validBiomes;

        public GeneratorEntry(Class<? extends StructureGenerator> generator, Biome[] biomes, int weight) {
            this.generator = generator;
            this.validBiomes = biomes;
            this.weight = weight;
        }
    }
}
