package org.jurassicraft.server.item;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.server.api.IHybrid;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.item.bones.FossilItem;
import org.jurassicraft.server.item.vehicles.HeliModuleItem;
import org.jurassicraft.server.item.vehicles.HelicopterItem;

import java.util.HashMap;
import java.util.Map;

public enum ItemHandler
{
    INSTANCE;

    public PlasterAndBandageItem plaster_and_bandage;
    public DinosaurSpawnEggItem spawn_egg;

    public DNAItem dna;
    public DinosaurEggItem egg;
    public PaleoPadItem paleo_pad;
    public SoftTissueItem soft_tissue;

    public DinsaurMeatItem dino_meat;
    public DinosaurSteakItem dino_steak;

    public BluePrintItem blue_print;
    public PaddockSignItem paddock_sign;
    public JurassiCraftSignItem jc_sign;

    public AmberItem amber;
    public BasicItem petri_dish;
    public BasicItem empty_test_tube;

    public SyringeItem syringe;
    public EmptySyringeItem empty_syringe;

    public StorageDiscItem storage_disc;
    public BasicItem dna_base;

    public CageItem cage_small;

    public PlantDNAItem plant_dna;

    public BasicItem sea_lamprey;

    public BasicItem iron_blades;
    public BasicItem iron_rod;
    public BasicItem disc_reader;
    public BasicItem laser;

    public BasicItem needle;

    public GrowthSerumItem growth_serum;

    public BasicItem plant_cells;
    public PlantCallusItem plant_callus;
    public BasicItem plant_cells_petri_dish;
    public HelicopterItem helicopter_spawner;
    public HeliModuleItem minigun_module_adder;

    public BasicItem tracker;

    public JCMusicDiscItem disc_jurassicraft_theme;
    public JCMusicDiscItem disc_troodons_and_raptors;
    public JCMusicDiscItem disc_dont_move_a_muscle;

    public ActionFigureItem action_figure;

    public BasicItem amber_keychain;
    public BasicItem amber_cane;
    public BasicItem mr_dna_keychain;

    public BasicItem basic_circuit;
    public BasicItem advanced_circuit;

    public BasicItem iron_nugget;

    public Item ajuginucula_smithii_seeds;
    public Item ajuginucula_smithii_leaves;
    public Item ajuginucula_smithii_oil;

    public Item wild_onion;

    public Item dino_scanner;

    public Map<String, FossilItem> fossils = new HashMap<String, FossilItem>();
    public Map<String, FossilItem> fresh_fossils = new HashMap<String, FossilItem>();

    public BasicItem gypsum_powder;

    // TODO more complex crafting components, eg circuit boards

    public void init()
    {
        plaster_and_bandage = new PlasterAndBandageItem();
        spawn_egg = new DinosaurSpawnEggItem();
        dna = new DNAItem();
        egg = new DinosaurEggItem();
        paleo_pad = new PaleoPadItem();
        dino_meat = new DinsaurMeatItem();
        dino_steak = new DinosaurSteakItem();
        blue_print = new BluePrintItem();
        paddock_sign = new PaddockSignItem();
        jc_sign = new JurassiCraftSignItem();
        soft_tissue = new SoftTissueItem();
        amber = new AmberItem();
        petri_dish = new BasicItem(TabHandler.INSTANCE.items);
        empty_test_tube = new BasicItem(TabHandler.INSTANCE.items);
        syringe = new SyringeItem();
        empty_syringe = new EmptySyringeItem();
        storage_disc = new StorageDiscItem();
        disc_reader = new BasicItem(TabHandler.INSTANCE.items);
        laser = new BasicItem(TabHandler.INSTANCE.items);
        dna_base = new BasicItem(TabHandler.INSTANCE.items);
        cage_small = new CageItem();
        plant_dna = new PlantDNAItem();
        sea_lamprey = new BasicItem(TabHandler.INSTANCE.items);
        iron_blades = new BasicItem(TabHandler.INSTANCE.items);
        iron_rod = new BasicItem(TabHandler.INSTANCE.items);
        growth_serum = new GrowthSerumItem();
        needle = new BasicItem(TabHandler.INSTANCE.items);
        plant_cells = new BasicItem(TabHandler.INSTANCE.items);
        plant_callus = new PlantCallusItem();
        plant_cells_petri_dish = new BasicItem(TabHandler.INSTANCE.items);
        tracker = new BasicItem(TabHandler.INSTANCE.items);
        action_figure = new ActionFigureItem();
        dino_scanner = new DinoScannerItem();

        amber_cane = new BasicItem(TabHandler.INSTANCE.merchandise);
        amber_cane.setFull3D();
        amber_cane.setMaxStackSize(1);
        amber_keychain = new BasicItem(TabHandler.INSTANCE.merchandise);
        mr_dna_keychain = new BasicItem(TabHandler.INSTANCE.merchandise);

        helicopter_spawner = new HelicopterItem();
        minigun_module_adder = new HeliModuleItem("minigun");

        disc_jurassicraft_theme = new JCMusicDiscItem("jurassicraft_theme");
        disc_troodons_and_raptors = new JCMusicDiscItem("troodons_and_raptors");
        disc_dont_move_a_muscle = new JCMusicDiscItem("dont_move_a_muscle");

        basic_circuit = new BasicItem(TabHandler.INSTANCE.items);
        advanced_circuit = new BasicItem(TabHandler.INSTANCE.items);

        iron_nugget = new BasicItem(TabHandler.INSTANCE.items);

        gypsum_powder = new BasicItem(TabHandler.INSTANCE.items);

        ajuginucula_smithii_seeds = new ItemSeeds(BlockHandler.INSTANCE.ajuginucula_smithii, Blocks.farmland).setUnlocalizedName("ajuginucula_smithii_seeds").setCreativeTab(TabHandler.INSTANCE.plants);
        ajuginucula_smithii_leaves = new ItemFood(1, 0.5F, false).setUnlocalizedName("ajuginucula_smithii_leaves").setCreativeTab(TabHandler.INSTANCE.plants);
        ajuginucula_smithii_oil = new BasicItem(TabHandler.INSTANCE.plants);

        wild_onion = new ItemSeeds(BlockHandler.INSTANCE.wild_onion, Blocks.farmland).setUnlocalizedName("wild_onion").setCreativeTab(TabHandler.INSTANCE.plants);

        for (Dinosaur dinosaur : JCEntityRegistry.getRegisteredDinosaurs())
        {

            String[] boneTypes = dinosaur.getBones();

            for (String boneType : boneTypes)
            {
                if (!(dinosaur instanceof IHybrid))
                {
                    if (!fossils.containsKey(boneType))
                    {
                        FossilItem fossil = new FossilItem(boneType, false);
                        fossils.put(boneType, fossil);
                        registerItem(fossil, boneType);
                    }
                }

                if (!fresh_fossils.containsKey(boneType))
                {
                    FossilItem fossil = new FossilItem(boneType, true);
                    fresh_fossils.put(boneType, fossil);
                    registerItem(fossil, boneType + " Fresh");
                }
            }
        }

        registerItem(amber, "Amber");
        registerItem(sea_lamprey, "Sea Lamprey");
        registerItem(plaster_and_bandage, "Plaster And Bandage");
        registerItem(empty_test_tube, "Empty Test Tube");
        registerItem(empty_syringe, "Empty Syringe");
        registerItem(growth_serum, "Growth Serum");
        registerItem(storage_disc, "Storage Disc");
        registerItem(disc_reader, "Disc Reader");
        registerItem(laser, "Laser");
        registerItem(dna_base, "DNA Base Material");
        registerItem(petri_dish, "Petri Dish");
        registerItem(plant_cells_petri_dish, "Plant Cells Petri Dish");
        registerItem(blue_print, "Blue Print");
        registerItem(paddock_sign, "Paddock Sign");
        registerItem(cage_small, "Cage Small");
        // registerItem(jc_sign, "JurassiCraft Sign");
        registerItem(spawn_egg, "Dino Spawn Egg");
        registerItem(dna, "DNA");
        registerItem(egg, "Dino Egg");
        registerItem(paleo_pad, "Paleo Pad");
        registerItem(soft_tissue, "Soft Tissue");
        registerItem(syringe, "Syringe");
        registerItem(plant_dna, "Plant DNA");
        registerItem(iron_blades, "Iron Blades");
        registerItem(iron_rod, "Iron Rod");
        registerItem(needle, "Needle");
        registerItem(plant_cells, "Plant Cells");
        registerItem(plant_callus, "Plant Callus");
        registerItem(tracker, "Tracker");
        registerItem(basic_circuit, "Basic Circuit");
        registerItem(advanced_circuit, "Advanced Circuit");
        registerItemOreDict(iron_nugget, "Iron Nugget", "nuggetIron");

        registerItem(dino_meat, "Dinosaur Meat");
        registerItem(dino_steak, "Dinosaur Steak");

        registerItem(helicopter_spawner, "Helicopter Spawner");
        registerItem(minigun_module_adder, "Helicopter Minigun");

        registerItem(disc_jurassicraft_theme, "Disc JurassiCraft Theme");
        registerItem(disc_troodons_and_raptors, "Disc Troodons And Raptors");
        registerItem(disc_dont_move_a_muscle, "Disc Don't Move A Muscle");

        registerItem(amber_cane, "Amber Cane");
        registerItem(amber_keychain, "Amber Keychain");
        registerItem(mr_dna_keychain, "Mr DNA Keychain");

        registerItem(action_figure, "Action Figure");

        registerItem(dino_scanner, "Dino Scanner");

        registerItem(gypsum_powder, "Gypsum Powder");

        registerItem(ajuginucula_smithii_seeds, "Ajuginucula Smithii Seeds");
        registerItem(ajuginucula_smithii_leaves, "Ajuginucula Smithii Leaves");
        registerItem(ajuginucula_smithii_oil, "Ajuginucula Smithii Oil");

        registerItem(wild_onion, "Wild Onion");
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

        GameRegistry.registerItem(item, formattedName);
    }
}
