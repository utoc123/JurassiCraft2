package org.jurassicraft.server.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.JCPlanksBlock;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Map;

public enum RecipeHandler
{
    INSTANCE;

    public void init()
    {
        EntityHandler.INSTANCE.getDinosaurs().stream().filter(Dinosaur::shouldRegister).forEach(dinosaur -> {
            int meta = EntityHandler.INSTANCE.getDinosaurId(dinosaur);

            GameRegistry.addSmelting(new ItemStack(ItemHandler.INSTANCE.dino_meat, 1, meta), new ItemStack(ItemHandler.INSTANCE.dino_steak, 1, meta), 5F);
        });

        for (Map.Entry<TreeType, JCPlanksBlock> entry : BlockHandler.INSTANCE.planks.entrySet())
        {
            TreeType type = entry.getKey();
            JCPlanksBlock planks = entry.getValue();

            GameRegistry.addShapelessRecipe(new ItemStack(planks, 4), BlockHandler.INSTANCE.logs.get(type));

            GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.stairs.get(type), 4), "w  ", "ww ", "www", 'w', planks);
            GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.stairs.get(type), 4), "  w", " ww", "www", 'w', planks);
            GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.slabs.get(type), 6), "www", 'w', planks);
        }

        GameRegistry.addSmelting(new ItemStack(BlockHandler.INSTANCE.gypsum_cobblestone), new ItemStack(BlockHandler.INSTANCE.gypsum_stone), 1.5F);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.iron_blades), "I I", " S ", "I I", 'I', Items.iron_ingot, 'S', Items.stick);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.iron_rod), "ISI", "ISI", "ISI", 'I', Items.iron_ingot, 'S', Items.stick);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.fossil_grinder), "lBl", "rRr", "IPI", 'I', Items.iron_ingot, 'R', ItemHandler.INSTANCE.iron_rod, 'B', ItemHandler.INSTANCE.iron_blades, 'r', Items.redstone, 'l', new ItemStack(Items.dye, 1, 4), 'P', Blocks.piston);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.plaster_and_bandage, 9), "PGP", "GWG", "PGP", 'P', Items.paper, 'W', Blocks.wool, 'G', ItemHandler.INSTANCE.gypsum_powder);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.gypsum_stone), "GGG", "GGG", "GGG", 'G', ItemHandler.INSTANCE.gypsum_powder);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.cage_small, 1), "III", "BBB", "III", 'I', Items.iron_ingot, 'B', Blocks.iron_bars);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.cage_small, 1, 1), "III", "GBG", "III", 'I', Items.iron_ingot, 'G', Blocks.glass_pane, 'B', Items.water_bucket);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.gypsum_bricks, 4), "SS", "SS", 'S', BlockHandler.INSTANCE.gypsum_stone);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.cleaning_station), "iii", "RGR", "IBI", 'i', Items.iron_ingot, 'B', Items.bucket, 'G', Blocks.glass_pane, 'R', Items.redstone, 'I', Blocks.iron_block);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.reinforced_stone, 8), "PPP", "PWP", "PPP", 'P', Blocks.stone, 'W', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.reinforced_bricks, 8), "PPP", "PWP", "PPP", 'P', Blocks.stonebrick, 'W', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.reinforced_bricks, 4), "SS", "SS", 'S', BlockHandler.INSTANCE.reinforced_stone);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.empty_test_tube, 8), "G", "G", 'G', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.petri_dish), "G G", "GGG", 'G', Blocks.glass_pane);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.blue_print), "BBB", "BPB", "BBB", 'B', new ItemStack(Items.dye, 1, 4), 'P', Items.paper);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.empty_syringe), "  N", "IG ", "II ", 'G', Blocks.glass_pane, 'I', Items.iron_ingot, 'N', ItemHandler.INSTANCE.needle);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.empty_syringe), "N  ", " GI", " II", 'G', Blocks.glass_pane, 'I', Items.iron_ingot, 'N', ItemHandler.INSTANCE.needle);
        GameRegistry.addSmelting(new ItemStack(Items.potionitem, 1, 0), new ItemStack(ItemHandler.INSTANCE.dna_base), 1.0F);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.needle), "GIG", "GIG", " I ", 'G', Blocks.glass_pane, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.dna_extractor), "III", "INI", "RSR", 'S', BlockHandler.INSTANCE.dna_sequencer, 'I', Items.iron_ingot, 'R', Items.redstone, 'N', ItemHandler.INSTANCE.needle);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.dna_sequencer), "IAI", "SAI", "HAK", 'I', Items.iron_ingot, 'A', ItemHandler.INSTANCE.dna_analyzer, 'S', ItemHandler.INSTANCE.computer_screen, 'H', ItemHandler.INSTANCE.disc_reader, 'K', ItemHandler.INSTANCE.keyboard);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.dna_synthesizer), "IIS", "ICD", "IIC", 'I', Items.iron_ingot, 'S', ItemHandler.INSTANCE.computer_screen, 'C', ItemHandler.INSTANCE.advanced_circuit, 'D', ItemHandler.INSTANCE.disc_reader);

        for (int i = 0; i < 16; i++)
        {
            GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.cultivate_bottom, 1, i), "GGG", "GWG", "III", 'G', new ItemStack(Blocks.stained_glass_pane, 1, i), 'W', Items.water_bucket, 'I', Items.iron_ingot);
        }

        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.embryonic_machine), "GIG", "GIG", "III", 'G', Blocks.glass_pane, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.embryo_calcification_machine), "GIG", "GSG", "III", 'G', Blocks.glass_pane, 'I', Items.iron_ingot, 'S', ItemHandler.INSTANCE.needle);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.incubator), "GGG", "RRR", "III", 'I', Items.iron_ingot, 'R', Items.redstone, 'G', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.dna_combinator_hybridizer), "SCS", "IDI", " K ", 'S', ItemHandler.INSTANCE.computer_screen, 'C', ItemHandler.INSTANCE.basic_circuit, 'I', Items.iron_ingot, 'D', ItemHandler.INSTANCE.disc_reader, 'L', ItemHandler.INSTANCE.keyboard);
        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.INSTANCE.plant_cells_petri_dish), ItemHandler.INSTANCE.plant_cells, ItemHandler.INSTANCE.petri_dish);

        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.iron_nugget, 9), "i", 'i', Items.iron_ingot);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemHandler.INSTANCE.basic_circuit, 2), "nuggetIron", "nuggetIron", "nuggetIron", "nuggetIron", Items.gold_nugget, Items.gold_nugget, Items.gold_nugget, Items.gold_nugget, Items.redstone));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemHandler.INSTANCE.advanced_circuit, 1), ItemHandler.INSTANCE.basic_circuit, ItemHandler.INSTANCE.basic_circuit, "nuggetIron", Items.gold_nugget, Items.redstone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.INSTANCE.storage_disc), " I ", "ICI", " I ", 'I', "nuggetIron", 'C', ItemHandler.INSTANCE.basic_circuit));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.INSTANCE.laser), "IDI", "ICI", "IRI", 'I', "nuggetIron", 'C', ItemHandler.INSTANCE.basic_circuit, 'D', Items.diamond, 'R', Items.redstone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.INSTANCE.disc_reader), "CLI", "i  ", "iib", 'i', "nuggetIron", 'C', ItemHandler.INSTANCE.advanced_circuit, 'L', ItemHandler.INSTANCE.laser, 'I', Items.iron_ingot, 'b', Blocks.stone_button));

        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.INSTANCE.ajuginucula_smithii_oil), ItemHandler.INSTANCE.ajuginucula_smithii_leaves, ItemHandler.INSTANCE.ajuginucula_smithii_leaves, ItemHandler.INSTANCE.ajuginucula_smithii_leaves, ItemHandler.INSTANCE.ajuginucula_smithii_leaves, Items.glass_bottle);

        GameRegistry.addRecipe(new ItemStack(Blocks.mossy_cobblestone, 4), "MCM", "CMC", "MCM", 'M', BlockHandler.INSTANCE.moss, 'C', Blocks.cobblestone);
        GameRegistry.addRecipe(new ItemStack(Blocks.stonebrick, 4, 1), "MCM", "CMC", "MCM", 'M', BlockHandler.INSTANCE.moss, 'C', new ItemStack(Blocks.stonebrick, 1, 0));

        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.computer_screen), "IGI", "RPB", "CQI", 'I', Items.iron_ingot, 'G', new ItemStack(Items.dye, 1, 2), 'R', new ItemStack(Items.dye, 1, 1), 'P', Blocks.glass_pane, 'B', new ItemStack(Items.dye, 1, 4), 'C', ItemHandler.INSTANCE.basic_circuit, 'Q', Items.quartz);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.keyboard), "bbb", "bbb", "psc", 'b', Blocks.stone_button, 'p', Blocks.stone_pressure_plate, 's', Blocks.stone_slab, 'c', ItemHandler.INSTANCE.basic_circuit);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.INSTANCE.dna_analyzer), "iGi", "CLi", "iii", 'i', "nuggetIron", 'G', Blocks.glass, 'C', ItemHandler.INSTANCE.advanced_circuit, 'L', ItemHandler.INSTANCE.laser));

        addGrowthSerumRecipe(Items.cooked_beef);
        addGrowthSerumRecipe(Items.cooked_chicken);
        addGrowthSerumRecipe(Items.cooked_fish);
        addGrowthSerumRecipe(Items.cooked_mutton);
        addGrowthSerumRecipe(Items.cooked_porkchop);
        addGrowthSerumRecipe(Items.cooked_rabbit);

        for (int i = 0; i < EntityHandler.INSTANCE.getDinosaurs().size(); i++)
        {
            addGrowthSerumRecipe(new ItemStack(ItemHandler.INSTANCE.dino_steak, 1, i));
        }
    }

    private void addGrowthSerumRecipe(Item meat)
    {
        addGrowthSerumRecipe(new ItemStack(meat));
    }

    private void addGrowthSerumRecipe(ItemStack meat)
    {
        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.INSTANCE.growth_serum), Items.golden_carrot, ItemHandler.INSTANCE.empty_syringe, Items.water_bucket, meat);
    }
}
