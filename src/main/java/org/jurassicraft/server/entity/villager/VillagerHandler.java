package org.jurassicraft.server.entity.villager;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.jurassicraft.server.world.structure.GeneticistVillagerHouse;

public class VillagerHandler {
    public static final GeneticistProfession GENETICIST = new GeneticistProfession();

    public static void onPreInit() {
        VillagerRegistry.instance().register(GENETICIST);
        VillagerRegistry.instance().registerVillageCreationHandler(new GeneticistVillagerHouse.CreationHandler());
        MapGenStructureIO.registerStructureComponent(GeneticistVillagerHouse.class, "GeneticistHouse");
    }
}
