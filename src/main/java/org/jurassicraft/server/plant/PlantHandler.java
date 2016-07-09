package org.jurassicraft.server.plant;

import java.util.ArrayList;
import java.util.List;

public class PlantHandler
{
    public static final Plant AJUGINUCULA_SMITHII = new AjuginuculaSmithiiPlant();
    public static final Plant SMALL_ROYAL_FERN = new SmallRoyalFernPlant();
    public static final Plant CALAMITES = new CalamitesPlant();
    public static final Plant SMALL_CHAIN_FERN = new SmallChainFernPlant();
    public static final Plant SMALL_CYCAD = new SmallCycadPlant();
    public static final Plant GINKGO = new GinkgoPlant();
    public static final Plant CYCADEOIDEA = new BennettitaleanCycadeoideaPlant();
    public static final Plant CRY_PANSY = new CryPansyPlant();
    public static final Plant SCALY_TREE_FERN = new ScalyTreeFernPlant();
    public static final Plant ZAMITES = new ZamitesPlant();
    public static final Plant DICKSONIA = new DicksoniaPlant();
    public static final Plant WILD_ONION = new WildOnionPlant();
    public static final Plant DICROIDIUM_ZUBERI_PLANT = new DicroidiumZuberiPlant();
    private static List<Plant> plants = new ArrayList<>();

    public static void init()
    {
        registerPlant(AJUGINUCULA_SMITHII);
        registerPlant(SMALL_ROYAL_FERN);
        registerPlant(CALAMITES);
        registerPlant(SMALL_CHAIN_FERN);
        registerPlant(SMALL_CYCAD);
        registerPlant(GINKGO);
        registerPlant(CYCADEOIDEA);
        registerPlant(CRY_PANSY);
        registerPlant(SCALY_TREE_FERN);
        registerPlant(ZAMITES);
        registerPlant(DICKSONIA);
        registerPlant(WILD_ONION);
        registerPlant(DICROIDIUM_ZUBERI_PLANT);
    }

    public static Plant getPlantById(int id)
    {
        if (id >= plants.size() || id < 0)
        {
            return null;
        }

        return plants.get(id);
    }

    public static int getPlantId(Plant plant)
    {
        return plants.indexOf(plant);
    }

    public static List<Plant> getPlants()
    {
        return plants;
    }

    public static void registerPlant(Plant plant)
    {
        if (!plants.contains(plant))
        {
            plants.add(plant);
        }
    }
}
