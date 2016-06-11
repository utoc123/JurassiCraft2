package org.jurassicraft.server.item;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.item.bones.FossilItem;
import org.jurassicraft.server.item.vehicles.HelicopterItem;
import org.jurassicraft.server.item.vehicles.HelicopterModuleItem;
import org.jurassicraft.server.tab.TabHandler;

import java.util.HashMap;
import java.util.Map;

public enum ItemHandler
{
    INSTANCE;

    public PlasterAndBandageItem PLASTER_AND_BANDAGE;
    public DinosaurSpawnEggItem SPAWN_EGG;

    public DNAItem DNA;
    public DinosaurEggItem EGG;
    public HatchedEggItem HATCHED_EGG;
    public SoftTissueItem SOFT_TISSUE;
    public PlantSoftTissueItem PLANT_SOFT_TISSUE;

    public DinsaurMeatItem DINOSAUR_MEAT;
    public DinosaurSteakItem DINOSAUR_STEAK;

    public BluePrintItem BLUEPRINT;
    public PaddockSignItem PADDOCK_SIGN;
    public AttractionSignItem ATTRACTION_SIGN;

    public AmberItem AMBER;
    public BasicItem PETRI_DISH;
    public BasicItem PETRI_DISH_AGAR;
    public BasicItem EMPTY_TEST_TUBE;

    public SyringeItem SYRINGE;
    public EmptySyringeItem EMPTY_SYRINGE;

    public StorageDiscItem STORAGE_DISC;
    public BasicItem DNA_NUCLEOTIDES;

    public PlantDNAItem PLANT_DNA;

    public BasicItem SEA_LAMPREY;

    public BasicItem IRON_BLADES;
    public BasicItem IRON_ROD;
    public BasicItem HARD_DRIVE;
    public BasicItem LASER;

    public GrowthSerumItem GROWTH_SERUM;

    public BasicItem PLANT_CELLS;
    public PlantCallusItem PLANT_CALLUS;
    public BasicItem PLANT_CELLS_PETRI_DISH;
    public HelicopterItem HELICOPTER;
    public HelicopterModuleItem MINIGUN_MODULE;

    public BasicItem TRACKER;

    public JCMusicDiscItem JURASSICRAFT_THEME_DISC;
    public JCMusicDiscItem TROODONS_AND_RAPTORS_DISC;
    public JCMusicDiscItem DONT_MOVE_A_MUSCLE_DISC;

    public ActionFigureItem ACTION_FIGURE;

    public BasicItem AMBER_KEYCHAIN;
    public BasicItem AMBER_CANE;
    public BasicItem MR_DNA_KEYCHAIN;

    public BasicItem BASIC_CIRCUIT;
    public BasicItem ADVANCED_CIRCUIT;

    public BasicItem IRON_NUGGET;

    public Item AJUGINUCULA_SMITHII_SEEDS;
    public Item AJUGINUCULA_SMITHII_LEAVES;
    public Item AJUGINUCULA_SMITHII_OIL;

    public Item WILD_ONION;

    public Item GRACILARIA;
    public Item LIQUID_AGAR;

    public Item DINO_SCANNER;

    public Item PLANT_FOSSIL;
    public Item TWIG_FOSSIL;

    public Map<String, FossilItem> FOSSILS = new HashMap<>();
    public Map<String, FossilItem> FRESH_FOSSILS = new HashMap<>();

    public BasicItem GYPSUM_POWDER;

    public BasicItem COMPUTER_SCREEN;
    public BasicItem KEYBOARD;

    public BasicItem DNA_ANALYZER;

    public BasicFoodItem CHILEAN_SEA_BASS;

    public void init()
    {
        PLASTER_AND_BANDAGE = new PlasterAndBandageItem();
        SPAWN_EGG = new DinosaurSpawnEggItem();
        DNA = new DNAItem();
        EGG = new DinosaurEggItem();
        HATCHED_EGG = new HatchedEggItem();
        DINOSAUR_MEAT = new DinsaurMeatItem();
        DINOSAUR_STEAK = new DinosaurSteakItem();
        BLUEPRINT = new BluePrintItem();
        PADDOCK_SIGN = new PaddockSignItem();
        ATTRACTION_SIGN = new AttractionSignItem();
        SOFT_TISSUE = new SoftTissueItem();
        PLANT_SOFT_TISSUE = new PlantSoftTissueItem();
        AMBER = new AmberItem();
        PETRI_DISH = new BasicItem(TabHandler.INSTANCE.ITEMS);
        PETRI_DISH_AGAR = new BasicItem(TabHandler.INSTANCE.ITEMS);
        EMPTY_TEST_TUBE = new BasicItem(TabHandler.INSTANCE.ITEMS);
        SYRINGE = new SyringeItem();
        EMPTY_SYRINGE = new EmptySyringeItem();
        STORAGE_DISC = new StorageDiscItem();
        HARD_DRIVE = new BasicItem(TabHandler.INSTANCE.ITEMS);
        LASER = new BasicItem(TabHandler.INSTANCE.ITEMS);
        DNA_NUCLEOTIDES = new BasicItem(TabHandler.INSTANCE.ITEMS);
        PLANT_DNA = new PlantDNAItem();
        SEA_LAMPREY = new BasicItem(TabHandler.INSTANCE.ITEMS);
        IRON_BLADES = new BasicItem(TabHandler.INSTANCE.ITEMS);
        IRON_ROD = new BasicItem(TabHandler.INSTANCE.ITEMS);
        GROWTH_SERUM = new GrowthSerumItem();
        PLANT_CELLS = new BasicItem(TabHandler.INSTANCE.ITEMS);
        PLANT_CALLUS = new PlantCallusItem();
        PLANT_CELLS_PETRI_DISH = new BasicItem(TabHandler.INSTANCE.ITEMS);
        TRACKER = new BasicItem(TabHandler.INSTANCE.ITEMS);
        ACTION_FIGURE = new ActionFigureItem();
        DINO_SCANNER = new DinoScannerItem();
        PLANT_FOSSIL = new PlantFossilItem();
        TWIG_FOSSIL = new TwigFossilItem();

        AMBER_CANE = new BasicItem(TabHandler.INSTANCE.MERCHANDISE);
        AMBER_CANE.setFull3D();
        AMBER_CANE.setMaxStackSize(1);
        AMBER_KEYCHAIN = new BasicItem(TabHandler.INSTANCE.MERCHANDISE);
        MR_DNA_KEYCHAIN = new BasicItem(TabHandler.INSTANCE.MERCHANDISE);

        HELICOPTER = new HelicopterItem();
        MINIGUN_MODULE = new HelicopterModuleItem("minigun");

        JURASSICRAFT_THEME_DISC = new JCMusicDiscItem("jurassicraft_theme");
        TROODONS_AND_RAPTORS_DISC = new JCMusicDiscItem("troodons_and_raptors");
        DONT_MOVE_A_MUSCLE_DISC = new JCMusicDiscItem("dont_move_a_muscle");

        BASIC_CIRCUIT = new BasicItem(TabHandler.INSTANCE.ITEMS);
        ADVANCED_CIRCUIT = new BasicItem(TabHandler.INSTANCE.ITEMS);

        COMPUTER_SCREEN = new BasicItem(TabHandler.INSTANCE.ITEMS);
        KEYBOARD = new BasicItem(TabHandler.INSTANCE.ITEMS);
        DNA_ANALYZER = new BasicItem(TabHandler.INSTANCE.ITEMS);

        IRON_NUGGET = new BasicItem(TabHandler.INSTANCE.ITEMS);

        GYPSUM_POWDER = new BasicItem(TabHandler.INSTANCE.ITEMS);

        AJUGINUCULA_SMITHII_SEEDS = new ItemSeeds(BlockHandler.INSTANCE.AJUGINUCULA_SMITHII, Blocks.FARMLAND).setUnlocalizedName("ajuginucula_smithii_seeds").setCreativeTab(TabHandler.INSTANCE.PLANTS);
        AJUGINUCULA_SMITHII_LEAVES = new ItemFood(1, 0.5F, false).setUnlocalizedName("ajuginucula_smithii_leaves").setCreativeTab(TabHandler.INSTANCE.PLANTS);
        AJUGINUCULA_SMITHII_OIL = new BasicItem(TabHandler.INSTANCE.PLANTS);

        WILD_ONION = new ItemSeeds(BlockHandler.INSTANCE.WILD_ONION, Blocks.FARMLAND).setUnlocalizedName("wild_onion").setCreativeTab(TabHandler.INSTANCE.PLANTS);
        GRACILARIA = new GracilariaItem(BlockHandler.INSTANCE.GRACILARIA).setCreativeTab(TabHandler.INSTANCE.PLANTS);
        LIQUID_AGAR = new BasicItem(TabHandler.INSTANCE.ITEMS);

        CHILEAN_SEA_BASS = new BasicFoodItem(10, 1.0F, false, TabHandler.INSTANCE.FOODS);

        for (Dinosaur dinosaur : EntityHandler.INSTANCE.getDinosaurs())
        {
            String[] boneTypes = dinosaur.getBones();

            for (String boneType : boneTypes)
            {
                if (!(dinosaur instanceof Hybrid))
                {
                    if (!FOSSILS.containsKey(boneType))
                    {
                        FossilItem fossil = new FossilItem(boneType, false);
                        FOSSILS.put(boneType, fossil);
                        registerItem(fossil, boneType);
                    }
                }

                if (!FRESH_FOSSILS.containsKey(boneType))
                {
                    FossilItem fossil = new FossilItem(boneType, true);
                    FRESH_FOSSILS.put(boneType, fossil);
                    registerItem(fossil, boneType + " Fresh");
                }
            }
        }

        registerItem(AMBER, "Amber");
        registerItem(SEA_LAMPREY, "Sea Lamprey");
        registerItem(PLASTER_AND_BANDAGE, "Plaster And Bandage");
        registerItem(EMPTY_TEST_TUBE, "Empty Test Tube");
        registerItem(EMPTY_SYRINGE, "Empty Syringe");
        registerItem(GROWTH_SERUM, "Growth Serum");
        registerItem(STORAGE_DISC, "Storage Disc");
        registerItem(HARD_DRIVE, "Disc Reader");
        registerItem(LASER, "Laser");
        registerItem(DNA_NUCLEOTIDES, "DNA Base Material");
        registerItem(PETRI_DISH, "Petri Dish");
        registerItem(PETRI_DISH_AGAR, "Petri Dish Agar");
        registerItem(PLANT_CELLS_PETRI_DISH, "Plant Cells Petri Dish");
        registerItem(BLUEPRINT, "Blue Print");
        registerItem(PADDOCK_SIGN, "Paddock Sign");
        registerItem(ATTRACTION_SIGN, "Attraction Sign");
        registerItem(SPAWN_EGG, "Dino Spawn Egg");
        registerItem(DNA, "DNA");
        registerItem(EGG, "Dino Egg");
        registerItem(HATCHED_EGG, "Hatched Egg");
        registerItem(SOFT_TISSUE, "Soft Tissue");
        registerItem(PLANT_SOFT_TISSUE, "Plant Soft Tissue");
        registerItem(SYRINGE, "Syringe");
        registerItem(PLANT_DNA, "Plant DNA");
        registerItem(IRON_BLADES, "Iron Blades");
        registerItem(IRON_ROD, "Iron Rod");
        registerItem(PLANT_CELLS, "Plant Cells");
        registerItem(PLANT_CALLUS, "Plant Callus");
//        registerItem(TRACKER, "Tracker");
        registerItem(BASIC_CIRCUIT, "Basic Circuit");
        registerItem(ADVANCED_CIRCUIT, "Advanced Circuit");
        registerItemOreDict(IRON_NUGGET, "Iron Nugget", "nuggetIron");
        registerItem(COMPUTER_SCREEN, "Computer Screen");
        registerItem(KEYBOARD, "Keyboard");
        registerItem(DNA_ANALYZER, "DNA Analyzer");

        registerItem(DINOSAUR_MEAT, "Dinosaur Meat");
        registerItem(DINOSAUR_STEAK, "Dinosaur Steak");

        registerItem(PLANT_FOSSIL, "Plant Fossil");
        registerItem(TWIG_FOSSIL, "Twig Fossil");

//        registerItem(HELICOPTER, "Helicopter Spawner");
//        registerItem(MINIGUN_MODULE, "Helicopter Minigun");

        registerItem(JURASSICRAFT_THEME_DISC, "Disc JurassiCraft Theme");
        registerItem(TROODONS_AND_RAPTORS_DISC, "Disc Troodons And Raptors");
        registerItem(DONT_MOVE_A_MUSCLE_DISC, "Disc Don't Move A Muscle");

        registerItem(AMBER_CANE, "Amber Cane");
        registerItem(AMBER_KEYCHAIN, "Amber Keychain");
        registerItem(MR_DNA_KEYCHAIN, "Mr DNA Keychain");

        registerItem(ACTION_FIGURE, "Action Figure");

        registerItem(DINO_SCANNER, "Dino Scanner");

        registerItem(GYPSUM_POWDER, "Gypsum Powder");

        registerItem(AJUGINUCULA_SMITHII_SEEDS, "Ajuginucula Smithii Seeds");
        registerItem(AJUGINUCULA_SMITHII_LEAVES, "Ajuginucula Smithii Leaves");
        registerItem(AJUGINUCULA_SMITHII_OIL, "Ajuginucula Smithii Oil");

        registerItem(WILD_ONION, "Wild Onion");

        registerItem(GRACILARIA, "Gracilaria");
        registerItem(LIQUID_AGAR, "Liquid Agar");

        registerItem(CHILEAN_SEA_BASS, "Chilean Sea Bass");
    }

    public void registerItemOreDict(Item item, String name, String oreDict)
    {
        registerItem(item, name);
        OreDictionary.registerOre(oreDict, item);
    }

    public void registerItem(Item item, String name)
    {
        String formattedName = name.toLowerCase().replaceAll(" ", "_").replaceAll("'", "");
        item.setUnlocalizedName(formattedName);

        GameRegistry.register(item, new ResourceLocation(JurassiCraft.MODID, formattedName));
    }
}
