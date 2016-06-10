package org.jurassicraft.server.plant;

import java.util.ArrayList;
import java.util.List;

public enum PlantHandler
{
    INSTANCE;

    private List<Plant> plants = new ArrayList<>();

    public final Plant AJUGINUCULA_SMITHII = new AjuginuculaSmithiiPlant();
    public final Plant SMALL_ROYAL_FERN = new SmallRoyalFernPlant();
    public final Plant CALAMITES = new CalamitesPlant();
    public final Plant SMALL_CHAIN_FERN = new SmallChainFernPlant();
    public final Plant SMALL_CYCAD = new SmallCycadPlant();
    public final Plant GINKGO = new GinkgoPlant();
    public final Plant CYCADEOIDEA = new BennettitaleanCycadeoideaPlant();
    public final Plant CRY_PANSY = new CryPansyPlant();
    public final Plant SCALY_TREE_FERN = new ScalyTreeFernPlant();
    public final Plant ZAMITES = new ZamitesPlant();
    public final Plant DICKSONIA = new DicksoniaPlant();
    public final Plant WILD_ONION = new WildOnionPlant();
    public final Plant DICROIDIUM_ZUBERI_PLANT = new DicroidiumZuberiPlant();

    public void init()
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
