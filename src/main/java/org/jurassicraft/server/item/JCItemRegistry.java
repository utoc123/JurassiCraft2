package org.jurassicraft.server.item;

import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.server.api.IHybrid;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.item.bones.FossilItem;
import org.jurassicraft.server.item.vehicles.HeliModuleItem;
import org.jurassicraft.server.item.vehicles.HelicopterItem;

import java.util.HashMap;
import java.util.Map;

public class JCItemRegistry implements IContentHandler
{
    public static ItemPlasterAndBandage plaster_and_bandage;
    public static DinosaurSpawnEggItem spawn_egg;

    public static ItemDNA dna;
    public static ItemDinosaurEgg egg;
    public static PaleoPadItem paleo_pad;
    public static SoftTissueItem soft_tissue;

    public static DinsaurMeatItem dino_meat;
    public static DinosaurSteakItem dino_steak;

    public static BluePrintItem blue_print;
    public static PaddockSignItem paddock_sign;
    public static JurassiCraftSignItem jc_sign;

    public static AmberItem amber;
    public static BasicItem petri_dish;
    public static BasicItem empty_test_tube;

    public static SyringeItem syringe;
    public static EmptySyringeItem empty_syringe;

    public static StorageDiscItem storage_disc;
    public static BasicItem dna_base;

    public static CageItem cage_small;

    public static PlantDNAItem plant_dna;

    public static BasicItem sea_lamprey;

    public static BasicItem iron_blades;
    public static BasicItem iron_rod;
    public static BasicItem disc_reader;
    public static BasicItem laser;

    public static BasicItem needle;

    public static GrowthSerumItem growth_serum;

    public static BasicItem plant_cells;
    public static PlantCallusItem plant_callus;
    public static BasicItem plant_cells_petri_dish;
    public static HelicopterItem helicopter_spawner;
    public static HeliModuleItem minigun_module_adder;

    public static BasicItem tracker;

    public static JCMusicDiscItem disc_jurassicraft_theme;
    public static JCMusicDiscItem disc_troodons_and_raptors;
    public static JCMusicDiscItem disc_dont_move_a_muscle;

    public static ItemActionFigure action_figure;

    public static BasicItem amber_keychain;
    public static BasicItem amber_cane;
    public static BasicItem mr_dna_keychain;

    public static BasicItem basic_circuit;
    public static BasicItem advanced_circuit;

    public static BasicItem iron_nugget;

    // Debug items
    public static Item dino_scanner;

    public static Map<String, FossilItem> fossils = new HashMap<String, FossilItem>();
    public static Map<String, FossilItem> fresh_fossils = new HashMap<String, FossilItem>();

    public static BasicItem gypsum_powder;

    // TODO more complex crafting components, eg circuit boards

    @Override
    public void init()
    {
        plaster_and_bandage = new ItemPlasterAndBandage();
        spawn_egg = new DinosaurSpawnEggItem();
        dna = new ItemDNA();
        egg = new ItemDinosaurEgg();
        paleo_pad = new PaleoPadItem();
        dino_meat = new DinsaurMeatItem();
        dino_steak = new DinosaurSteakItem();
        blue_print = new BluePrintItem();
        paddock_sign = new PaddockSignItem();
        jc_sign = new JurassiCraftSignItem();
        soft_tissue = new SoftTissueItem();
        amber = new AmberItem();
        petri_dish = new BasicItem(JCCreativeTabs.items);
        empty_test_tube = new BasicItem(JCCreativeTabs.items);
        syringe = new SyringeItem();
        empty_syringe = new EmptySyringeItem();
        storage_disc = new StorageDiscItem();
        disc_reader = new BasicItem(JCCreativeTabs.items);
        laser = new BasicItem(JCCreativeTabs.items);
        dna_base = new BasicItem(JCCreativeTabs.items);
        cage_small = new CageItem();
        plant_dna = new PlantDNAItem();
        sea_lamprey = new BasicItem(JCCreativeTabs.items);
        iron_blades = new BasicItem(JCCreativeTabs.items);
        iron_rod = new BasicItem(JCCreativeTabs.items);
        growth_serum = new GrowthSerumItem();
        needle = new BasicItem(JCCreativeTabs.items);
        plant_cells = new BasicItem(JCCreativeTabs.items);
        plant_callus = new PlantCallusItem();
        plant_cells_petri_dish = new BasicItem(JCCreativeTabs.items);
        tracker = new BasicItem(JCCreativeTabs.items);
        action_figure = new ItemActionFigure();
        dino_scanner = new DinoScannerItem();

        amber_cane = new BasicItem(JCCreativeTabs.merchandise);
        amber_cane.setFull3D();
        amber_cane.setMaxStackSize(1);
        amber_keychain = new BasicItem(JCCreativeTabs.merchandise);
        mr_dna_keychain = new BasicItem(JCCreativeTabs.merchandise);

        helicopter_spawner = new HelicopterItem();
        minigun_module_adder = new HeliModuleItem("minigun");

        disc_jurassicraft_theme = new JCMusicDiscItem("jurassicraft_theme");
        disc_troodons_and_raptors = new JCMusicDiscItem("troodons_and_raptors");
        disc_dont_move_a_muscle = new JCMusicDiscItem("dont_move_a_muscle");

        basic_circuit = new BasicItem(JCCreativeTabs.items);
        advanced_circuit = new BasicItem(JCCreativeTabs.items);

        iron_nugget = new BasicItem(JCCreativeTabs.items);

        gypsum_powder = new BasicItem(JCCreativeTabs.items);

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
    }

    @Override
    public void gameRegistry() throws Exception
    {
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

        // registerItem(entityRemover, "Entity Remover");

        // for (int i = 0; i < JCEntityRegistry.getDinosaurs().size(); i++)
        // {
        // EcoAPI.registerEcologicalRoleFoodItem(EcoAPI.carnivore, new ItemStack(JCItemRegistry.dino_meat, 1, i));
        // EcoAPI.registerEcologicalRoleFoodItem(EcoAPI.carnivore, new ItemStack(JCItemRegistry.dino_steak, 1, i));
        // EcoAPI.registerEntityClassDropItems(JCEntityRegistry.getDinosaurs().get(i).getDinosaurClass(), new ItemStack[]{new ItemStack(JCItemRegistry.dino_meat, 1, i), new ItemStack(JCItemRegistry.dino_steak, 1, i)});
        // }
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
