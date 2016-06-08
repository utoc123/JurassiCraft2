package org.jurassicraft.server.plant;

import java.util.ArrayList;
import java.util.List;

public enum PlantHandler
{
    INSTANCE;

    private List<Plant> plants = new ArrayList<>();

    public Plant AJUGINUCULA_SMITHII;
    public Plant SMALL_ROYAL_FERN;
    public Plant CALAMITES;
    public Plant SMALL_CHAIN_FERN;
    public Plant SMALL_CYCAD;
    public Plant GINKGO;
    public Plant CYCADEOIDEA;
    public Plant CRY_PANSY;
    public Plant SCALY_TREE_FERN;
    public Plant ZAMITES;
    public Plant DICKSONIA;
    public Plant WILD_ONION;

    public void init()
    {
        AJUGINUCULA_SMITHII = new AjuginuculaSmithiiPlant();
        SMALL_ROYAL_FERN = new SmallRoyalFernPlant();
        CALAMITES = new CalamitesPlant();
        SMALL_CHAIN_FERN = new SmallChainFernPlant();
        SMALL_CYCAD = new SmallCycadPlant();
        GINKGO = new GinkgoPlant();
        CYCADEOIDEA = new BennettitaleanCycadeoideaPlant();
        CRY_PANSY = new CryPansyPlant();
        SCALY_TREE_FERN = new ScalyTreeFernPlant();
        ZAMITES = new ZamitesPlant();
        DICKSONIA = new DicksoniaPlant();
        WILD_ONION = new WildOnionPlant();

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
    }

    public Plant getPlantById(int id)
    {
        if (id >= plants.size() || id < 0)
        {
            return null;
        }

        return plants.get(id);
    }

    public int getPlantId(Plant plant)
    {
        return plants.indexOf(plant);
    }

    public List<Plant> getPlants()
    {
        return plants;
    }

    public void registerPlant(Plant plant)
    {
        if (!plants.contains(plant))
        {
            plants.add(plant);
        }
    }
}
