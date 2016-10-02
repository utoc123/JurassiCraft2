package org.jurassicraft.server.entity.villager;

import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class VillagerHandler {
    public static final GeneticistProfession GENETICIST = new GeneticistProfession();

    public static void onPreInit() {
        VillagerRegistry.instance().register(GENETICIST);
    }
}
