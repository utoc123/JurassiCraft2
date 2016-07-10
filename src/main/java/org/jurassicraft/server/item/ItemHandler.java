package org.jurassicraft.server.item;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.sound.SoundHandler;
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

public class ItemHandler
{
    public static final PlasterAndBandageItem PLASTER_AND_BANDAGE = new PlasterAndBandageItem();
    public static final DinosaurSpawnEggItem SPAWN_EGG = new DinosaurSpawnEggItem();

    public static final DNAItem DNA = new DNAItem();
    public static final DinosaurEggItem EGG = new DinosaurEggItem();
    public static final HatchedEggItem HATCHED_EGG = new HatchedEggItem();
    public static final SoftTissueItem SOFT_TISSUE = new SoftTissueItem();
    public static final PlantSoftTissueItem PLANT_SOFT_TISSUE = new PlantSoftTissueItem();

    public static final DinosaurMeatItem DINOSAUR_MEAT = new DinosaurMeatItem();
    public static final DinosaurSteakItem DINOSAUR_STEAK = new DinosaurSteakItem();

    public static final BluePrintItem BLUEPRINT = new BluePrintItem();
    public static final PaddockSignItem PADDOCK_SIGN = new PaddockSignItem();
    public static final AttractionSignItem ATTRACTION_SIGN = new AttractionSignItem();

    public static final AmberItem AMBER = new AmberItem();
    public static final BasicItem PETRI_DISH = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem PETRI_DISH_AGAR = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem EMPTY_TEST_TUBE = new BasicItem(TabHandler.ITEMS);

    public static final SyringeItem SYRINGE = new SyringeItem();
    public static final EmptySyringeItem EMPTY_SYRINGE = new EmptySyringeItem();

    public static final StorageDiscItem STORAGE_DISC = new StorageDiscItem();
    public static final BasicItem DNA_NUCLEOTIDES = new BasicItem(TabHandler.ITEMS);

    public static final PlantDNAItem PLANT_DNA = new PlantDNAItem();

    public static final BasicItem SEA_LAMPREY = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem IRON_BLADES = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem IRON_ROD = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem HARD_DRIVE = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem LASER = new BasicItem(TabHandler.ITEMS);

    public static final GrowthSerumItem GROWTH_SERUM = new GrowthSerumItem();

    public static final BasicItem PLANT_CELLS = new BasicItem(TabHandler.ITEMS);
    public static final PlantCallusItem PLANT_CALLUS = new PlantCallusItem();
    public static final BasicItem PLANT_CELLS_PETRI_DISH = new BasicItem(TabHandler.ITEMS);
    public static final HelicopterItem HELICOPTER = new HelicopterItem();
    public static final HelicopterModuleItem MINIGUN_MODULE = new HelicopterModuleItem("minigun");

    public static final BasicItem TRACKER = new BasicItem(TabHandler.ITEMS);

    public static final AncientRecordItem JURASSICRAFT_THEME_DISC = new AncientRecordItem("jurassicraft_theme", SoundHandler.JURASSICRAFT_THEME);
    public static final AncientRecordItem TROODONS_AND_RAPTORS_DISC = new AncientRecordItem("troodons_and_raptors", SoundHandler.TROODONS_AND_RAPTORS);
    public static final AncientRecordItem DONT_MOVE_A_MUSCLE_DISC = new AncientRecordItem("dont_move_a_muscle", SoundHandler.DONT_MOVE_A_MUSCLE);

    public static final ActionFigureItem ACTION_FIGURE = new ActionFigureItem();

    public static final BasicItem AMBER_KEYCHAIN = new BasicItem(TabHandler.DECORATIONS);
    public static final BasicItem AMBER_CANE = new BasicItem(TabHandler.DECORATIONS);
    public static final BasicItem MR_DNA_KEYCHAIN = new BasicItem(TabHandler.DECORATIONS);

    public static final BasicItem BASIC_CIRCUIT = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem ADVANCED_CIRCUIT = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem IRON_NUGGET = new BasicItem(TabHandler.ITEMS);

    public static final Item AJUGINUCULA_SMITHII_SEEDS = new ItemSeeds(BlockHandler.AJUGINUCULA_SMITHII, Blocks.FARMLAND).setUnlocalizedName("ajuginucula_smithii_seeds").setCreativeTab(TabHandler.PLANTS);
    public static final Item AJUGINUCULA_SMITHII_LEAVES = new ItemFood(1, 0.5F, false).setUnlocalizedName("ajuginucula_smithii_leaves").setCreativeTab(TabHandler.PLANTS);
    public static final BasicItem AJUGINUCULA_SMITHII_OIL = new BasicItem(TabHandler.PLANTS);

    public static final Item WILD_ONION = new ItemSeeds(BlockHandler.WILD_ONION, Blocks.FARMLAND).setUnlocalizedName("wild_onion").setCreativeTab(TabHandler.PLANTS);

    public static final GracilariaItem GRACILARIA = (GracilariaItem) new GracilariaItem(BlockHandler.GRACILARIA).setCreativeTab(TabHandler.PLANTS);
    public static final BasicItem LIQUID_AGAR = new BasicItem(TabHandler.PLANTS);

    public static final DinoScannerItem DINO_SCANNER = new DinoScannerItem();

    public static final PlantFossilItem PLANT_FOSSIL = new PlantFossilItem();
    public static final TwigFossilItem TWIG_FOSSIL = new TwigFossilItem();

    public static final Map<String, FossilItem> FOSSILS = new HashMap<>();
    public static final Map<String, FossilItem> FRESH_FOSSILS = new HashMap<>();

    public static final BasicItem GYPSUM_POWDER = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem COMPUTER_SCREEN = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem KEYBOARD = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem DNA_ANALYZER = new BasicItem(TabHandler.ITEMS);

    public static final BasicFoodItem CHILEAN_SEA_BASS = new BasicFoodItem(10, 1.0F, false, TabHandler.FOODS);

    public static final FieldGuideItem FIELD_GUIDE = new FieldGuideItem();

    public static final BasicItem CAR_CHASSIS = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem CAR_ENGINE_SYSTEM = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem CAR_SEATS = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem CAR_TIRE = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem CAR_WINDSCREEN = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem UNFINISHED_CAR = new BasicItem(TabHandler.ITEMS);

    public static final JeepWranglerItem JEEP_WRANGLER = new JeepWranglerItem();

    public static final MuralItem MURAL = new MuralItem();

    public static void init()
    {
        for (Dinosaur dinosaur : EntityHandler.getDinosaurs())
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

        registerItem(SPAWN_EGG, "Dino Spawn Egg");
        registerItem(FIELD_GUIDE, "Field Guide");
        registerItem(AMBER, "Amber");
//        registerItem(SEA_LAMPREY, "Sea Lamprey");
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
        registerItem(MURAL, "Mural");
        registerItem(DNA, "DNA");
        registerItem(SOFT_TISSUE, "Soft Tissue");
        registerItem(SYRINGE, "Syringe");
        registerItem(EGG, "Dino Egg");
        registerItem(HATCHED_EGG, "Hatched Egg");
        registerItem(PLANT_SOFT_TISSUE, "Plant Soft Tissue");
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

        registerItem(CAR_CHASSIS, "Car Chassis");
        registerItem(CAR_ENGINE_SYSTEM, "Car Engine System");
        registerItem(CAR_SEATS, "Car Seats");
        registerItem(CAR_TIRE, "Car Tire");
        registerItem(CAR_WINDSCREEN, "Car Windscreen");
        registerItem(UNFINISHED_CAR, "Unfinished Car");
        registerItem(JEEP_WRANGLER, "Jeep Wrangler");
    }

    public static void registerItemOreDict(Item item, String name, String oreDict)
    {
        registerItem(item, name);
        OreDictionary.registerOre(oreDict, item);
    }

    public static void registerItem(Item item, String name)
    {
        String formattedName = name.toLowerCase().replaceAll(" ", "_").replaceAll("'", "");
        item.setUnlocalizedName(formattedName);

        GameRegistry.register(item, new ResourceLocation(JurassiCraft.MODID, formattedName));
    }
}
