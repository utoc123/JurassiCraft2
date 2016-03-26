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
        ItemHandler itemHandler = ItemHandler.INSTANCE;
        BlockHandler blockHandler = BlockHandler.INSTANCE;

        for (Dinosaur dinosaur : EntityHandler.INSTANCE.getDinosaurs())
        {
            if (dinosaur.shouldRegister())
            {
                int meta = EntityHandler.INSTANCE.getDinosaurId(dinosaur);

                GameRegistry.addSmelting(new ItemStack(itemHandler.dino_meat, 1, meta), new ItemStack(itemHandler.dino_steak, 1, meta), 5F);
            }
        }

        for (Map.Entry<TreeType, JCPlanksBlock> entry : blockHandler.planks.entrySet())
        {
            TreeType type = entry.getKey();
            JCPlanksBlock planks = entry.getValue();

            GameRegistry.addShapelessRecipe(new ItemStack(planks, 4), blockHandler.logs.get(type));

            GameRegistry.addRecipe(new ItemStack(blockHandler.stairs.get(type), 4), "w  ", "ww ", "www", 'w', planks);
            GameRegistry.addRecipe(new ItemStack(blockHandler.stairs.get(type), 4), "  w", " ww", "www", 'w', planks);
            GameRegistry.addRecipe(new ItemStack(blockHandler.slabs.get(type), 6), "www", 'w', planks);
        }

        GameRegistry.addSmelting(new ItemStack(blockHandler.gypsum_cobblestone), new ItemStack(blockHandler.gypsum_stone), 1.5F);
        GameRegistry.addRecipe(new ItemStack(itemHandler.iron_blades), "I I", " S ", "I I", 'I', Items.iron_ingot, 'S', Items.stick);
        GameRegistry.addRecipe(new ItemStack(itemHandler.iron_rod), "ISI", "ISI", "ISI", 'I', Items.iron_ingot, 'S', Items.stick);
        GameRegistry.addRecipe(new ItemStack(blockHandler.fossil_grinder), "lBl", "rRr", "IPI", 'I', Items.iron_ingot, 'R', itemHandler.iron_rod, 'B', itemHandler.iron_blades, 'r', Items.redstone, 'l', new ItemStack(Items.dye, 1, 4), 'P', Blocks.piston);
        GameRegistry.addRecipe(new ItemStack(itemHandler.plaster_and_bandage, 9), "PGP", "GWG", "PGP", 'P', Items.paper, 'W', Blocks.wool, 'G', itemHandler.gypsum_powder);
        GameRegistry.addRecipe(new ItemStack(blockHandler.gypsum_stone), "GGG", "GGG", "GGG", 'G', itemHandler.gypsum_powder);
        GameRegistry.addRecipe(new ItemStack(itemHandler.cage_small, 1), "III", "BBB", "III", 'I', Items.iron_ingot, 'B', Blocks.iron_bars);
        GameRegistry.addRecipe(new ItemStack(itemHandler.cage_small, 1, 1), "III", "GBG", "III", 'I', Items.iron_ingot, 'G', Blocks.glass_pane, 'B', Items.water_bucket);
        GameRegistry.addRecipe(new ItemStack(blockHandler.gypsum_bricks, 4), "SS", "SS", 'S', blockHandler.gypsum_stone);
        GameRegistry.addRecipe(new ItemStack(blockHandler.cleaning_station), "iii", "RGR", "IBI", 'i', Items.iron_ingot, 'B', Items.bucket, 'G', Blocks.glass_pane, 'R', Items.redstone, 'I', Blocks.iron_block);
        GameRegistry.addRecipe(new ItemStack(blockHandler.reinforced_stone, 8), "PPP", "PWP", "PPP", 'P', Blocks.stone, 'W', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(blockHandler.reinforced_bricks, 8), "PPP", "PWP", "PPP", 'P', Blocks.stonebrick, 'W', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(blockHandler.reinforced_bricks, 4), "SS", "SS", 'S', blockHandler.reinforced_stone);
        GameRegistry.addRecipe(new ItemStack(itemHandler.empty_test_tube), "I", "G", "G", 'G', Blocks.glass_pane, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(itemHandler.petri_dish), "G G", "GGG", 'G', Blocks.glass_pane);
        GameRegistry.addRecipe(new ItemStack(itemHandler.blue_print), "BBB", "BPB", "BBB", 'B', new ItemStack(Items.dye, 1, 4), 'P', Items.paper);
        GameRegistry.addRecipe(new ItemStack(itemHandler.empty_syringe), "  N", "IG ", "II ", 'G', Blocks.glass_pane, 'I', Items.iron_ingot, 'N', itemHandler.needle);
        GameRegistry.addRecipe(new ItemStack(itemHandler.empty_syringe), "N  ", " GI", " II", 'G', Blocks.glass_pane, 'I', Items.iron_ingot, 'N', itemHandler.needle);
        GameRegistry.addSmelting(new ItemStack(Items.potionitem, 1, 0), new ItemStack(itemHandler.dna_base), 1.0F);
        GameRegistry.addRecipe(new ItemStack(itemHandler.needle), "GIG", "GIG", " I ", 'G', Blocks.glass_pane, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(blockHandler.dna_extractor), "III", "INI", "RSR", 'S', blockHandler.dna_sequencer, 'I', Items.iron_ingot, 'R', Items.redstone, 'N', itemHandler.needle);
        GameRegistry.addRecipe(new ItemStack(blockHandler.dna_sequencer), "RDR", "GNG", "III", 'G', Items.gold_ingot, 'I', Items.iron_ingot, 'N', itemHandler.needle, 'D', itemHandler.disc_reader, 'R', Items.redstone);
        GameRegistry.addRecipe(new ItemStack(blockHandler.dna_synthesizer), "GDG", "RBR", "ITI", 'G', Items.gold_ingot, 'I', Items.iron_ingot, 'B', Items.bucket, 'R', Items.redstone, 'D', itemHandler.disc_reader);

        for (int i = 0; i < 16; i++)
        {
            GameRegistry.addRecipe(new ItemStack(blockHandler.cultivate_bottom, 1, i), "GGG", "GWG", "III", 'G', new ItemStack(Blocks.stained_glass_pane, 1, i), 'W', Items.water_bucket, 'I', Items.iron_ingot);
        }

        GameRegistry.addRecipe(new ItemStack(blockHandler.embryonic_machine), "GIG", "GIG", "III", 'G', Blocks.glass_pane, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(blockHandler.embryo_calcification_machine), "GIG", "GSG", "III", 'G', Blocks.glass_pane, 'I', Items.iron_ingot, 'S', itemHandler.needle);
        GameRegistry.addRecipe(new ItemStack(blockHandler.incubator), "GGG", "RRR", "III", 'I', Items.iron_ingot, 'R', Items.redstone, 'G', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(blockHandler.dna_combinator_hybridizer), "IRI", "GIG", "IRI", 'G', Blocks.glass, 'I', Items.iron_ingot, 'R', Items.redstone);
        GameRegistry.addShapelessRecipe(new ItemStack(itemHandler.plant_cells_petri_dish), itemHandler.plant_cells, itemHandler.petri_dish);

        GameRegistry.addRecipe(new ItemStack(itemHandler.iron_nugget, 9), "i", 'i', Items.iron_ingot);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemHandler.basic_circuit, 2), "nuggetIron", "nuggetIron", "nuggetIron", "nuggetIron", Items.gold_nugget, Items.gold_nugget, Items.gold_nugget, Items.gold_nugget, Items.redstone));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemHandler.advanced_circuit, 1), itemHandler.basic_circuit, itemHandler.basic_circuit, "nuggetIron", Items.gold_nugget, Items.redstone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemHandler.storage_disc), " I ", "ICI", " I ", 'I', "nuggetIron", 'C', itemHandler.basic_circuit));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemHandler.laser), "IDI", "ICI", "IRI", 'I', "nuggetIron", 'C', itemHandler.basic_circuit, 'D', Items.diamond, 'R', Items.redstone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemHandler.disc_reader), "CLI", "i  ", "iib", 'i', "nuggetIron", 'C', itemHandler.advanced_circuit, 'L', itemHandler.laser, 'I', Items.iron_ingot, 'b', Blocks.stone_button));

        GameRegistry.addShapelessRecipe(new ItemStack(itemHandler.ajuginucula_smithii_oil), itemHandler.ajuginucula_smithii_leaves, itemHandler.ajuginucula_smithii_leaves, itemHandler.ajuginucula_smithii_leaves, itemHandler.ajuginucula_smithii_leaves, Items.glass_bottle);

        GameRegistry.addRecipe(new ItemStack(Blocks.mossy_cobblestone, 4), "MCM", "CMC", "MCM", 'M', blockHandler.moss, 'C', Blocks.cobblestone);
        GameRegistry.addRecipe(new ItemStack(Blocks.stonebrick, 4, 1), "MCM", "CMC", "MCM", 'M', blockHandler.moss, 'C', new ItemStack(Blocks.stonebrick, 1, 0));

        addGrowthSerumRecipe(Items.cooked_beef);
        addGrowthSerumRecipe(Items.cooked_chicken);
        addGrowthSerumRecipe(Items.cooked_fish);
        addGrowthSerumRecipe(Items.cooked_mutton);
        addGrowthSerumRecipe(Items.cooked_porkchop);
        addGrowthSerumRecipe(Items.cooked_rabbit);

        for (int i = 0; i < EntityHandler.INSTANCE.getDinosaurs().size(); i++)
        {
            addGrowthSerumRecipe(new ItemStack(itemHandler.dino_steak, 1, i));
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
