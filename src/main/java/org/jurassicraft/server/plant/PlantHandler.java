package org.jurassicraft.server.plant;

import java.util.LinkedList;
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
    public static final Plant DICROIDIUM_ZUBERI = new DicroidiumZuberiPlant();
    public static final Plant DICTYOPHYLLUM = new DictyophyllumPlant();
    public static final Plant WEST_INDIAN_LILAC = new WestIndianLilacPlant();
    public static final Plant SERENNA_VERIFORMANS = new SerennaVeriformansPlant();
    public static final Plant LADINIA_SIMPLEX = new LadiniaSimplexPlant();
    public static final Plant ORONTIUM_MACKII = new OrontiumMackiiPlant();

    private static final List<Plant> PLANTS = new LinkedList<>();

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
        registerPlant(DICROIDIUM_ZUBERI);
        registerPlant(DICTYOPHYLLUM);
        registerPlant(WEST_INDIAN_LILAC);
        registerPlant(SERENNA_VERIFORMANS);
        registerPlant(LADINIA_SIMPLEX);
        registerPlant(ORONTIUM_MACKII);
    }

    public static Plant getPlantById(int id)
    {
        if (id >= PLANTS.size() || id < 0)
        {
            return null;
        }

        return PLANTS.get(id);
    }

    public static int getPlantId(Plant plant)
    {
        return PLANTS.indexOf(plant);
    }

    public static List<Plant> getPlants()
    {
        return PLANTS;
    }

    public static void registerPlant(Plant plant)
    {
        if (!PLANTS.contains(plant))
        {
            PLANTS.add(plant);
        }
    }

    public static List<Plant> getPrehistoricPlants()
    {
        List<Plant> prehistoricPlants = new LinkedList<>();

        for (Plant plant : PLANTS)
        {
            if (plant.shouldRegister() && plant.isPrehistoric())
            {
                prehistoricPlants.add(plant);
            }
        }

        return prehistoricPlants;
    }
}
