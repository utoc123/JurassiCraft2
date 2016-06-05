package org.jurassicraft.server.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.AncientPlanksBlock;
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
        for (Dinosaur dinosaur : EntityHandler.INSTANCE.getDinosaurs())
        {
            if (dinosaur.shouldRegister())
            {
                int meta = EntityHandler.INSTANCE.getDinosaurId(dinosaur);

                GameRegistry.addSmelting(new ItemStack(ItemHandler.INSTANCE.DINOSAUR_MEAT, 1, meta), new ItemStack(ItemHandler.INSTANCE.DINOSAUR_STEAK, 1, meta), 5F);
            }
        }

        for (Map.Entry<TreeType, AncientPlanksBlock> entry : BlockHandler.INSTANCE.ANCIENT_PLANKS.entrySet())
        {
            TreeType type = entry.getKey();
            AncientPlanksBlock planks = entry.getValue();

            GameRegistry.addShapelessRecipe(new ItemStack(planks, 4), BlockHandler.INSTANCE.ANCIENT_LOGS.get(type));

            GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.ANCIENT_STAIRS.get(type), 4), "w  ", "ww ", "www", 'w', planks);
            GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.ANCIENT_STAIRS.get(type), 4), "  w", " ww", "www", 'w', planks);
            GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.ANCIENT_SLABS.get(type), 6), "www", 'w', planks);
        }

        GameRegistry.addSmelting(new ItemStack(BlockHandler.INSTANCE.GYPSUM_COBBLESTONE), new ItemStack(BlockHandler.INSTANCE.GYPSUM_STONE), 1.5F);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.IRON_BLADES), "I I", " S ", "I I", 'I', Items.IRON_INGOT, 'S', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.IRON_RODE), "ISI", "ISI", "ISI", 'I', Items.IRON_INGOT, 'S', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.FOSSIL_GRINDER), "lBl", "rRr", "IPI", 'I', Items.IRON_INGOT, 'R', ItemHandler.INSTANCE.IRON_RODE, 'B', ItemHandler.INSTANCE.IRON_BLADES, 'r', Items.REDSTONE, 'l', new ItemStack(Items.DYE, 1, 4), 'P', Blocks.PISTON);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.PLASTER_AND_BANDAGE, 9), "PGP", "GWG", "PGP", 'P', Items.PAPER, 'W', Blocks.WOOL, 'G', ItemHandler.INSTANCE.GYPSUM_POWDER);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.GYPSUM_STONE), "GGG", "GGG", "GGG", 'G', ItemHandler.INSTANCE.GYPSUM_POWDER);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.CAGE_SMALL, 1), "III", "BBB", "III", 'I', Items.IRON_INGOT, 'B', Blocks.IRON_BARS);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.CAGE_SMALL, 1, 1), "III", "GBG", "III", 'I', Items.IRON_INGOT, 'G', Blocks.GLASS_PANE, 'B', Items.WATER_BUCKET);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.GYPSUM_BRICKS, 4), "SS", "SS", 'S', BlockHandler.INSTANCE.GYPSUM_STONE);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.CLEANING_STATION), "iii", "RGR", "IBI", 'i', Items.IRON_INGOT, 'B', Items.BUCKET, 'G', Blocks.GLASS_PANE, 'R', Items.REDSTONE, 'I', Blocks.IRON_BLOCK);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.REINFORCED_STONE, 8), "PPP", "PWP", "PPP", 'P', Blocks.STONE, 'W', Items.IRON_INGOT);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.REINFORCED_BRICKS, 8), "PPP", "PWP", "PPP", 'P', Blocks.STONEBRICK, 'W', Items.IRON_INGOT);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.REINFORCED_BRICKS, 4), "SS", "SS", 'S', BlockHandler.INSTANCE.REINFORCED_STONE);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.EMPTY_TEST_TUBE, 8), "G", "G", 'G', Blocks.GLASS);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.PETRI_DISH), "G G", "GGG", 'G', Blocks.GLASS_PANE);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.BLUEPRINT), "BBB", "BPB", "BBB", 'B', new ItemStack(Items.DYE, 1, 4), 'P', Items.PAPER);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.EMPTY_SYRINGE), "  N", "IG ", "II ", 'G', Blocks.GLASS_PANE, 'I', Items.IRON_INGOT, 'N', ItemHandler.INSTANCE.NEEDLE);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.EMPTY_SYRINGE), "N  ", " GI", " II", 'G', Blocks.GLASS_PANE, 'I', Items.IRON_INGOT, 'N', ItemHandler.INSTANCE.NEEDLE);
        GameRegistry.addSmelting(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(ItemHandler.INSTANCE.DNA_NUCLEOTIDES), 1.0F);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.NEEDLE), "GIG", "GIG", " I ", 'G', Blocks.GLASS_PANE, 'I', Items.IRON_INGOT);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.DNA_EXTRACTOR), "III", "INI", "RSR", 'S', BlockHandler.INSTANCE.DNA_SEQUENCER, 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'N', ItemHandler.INSTANCE.NEEDLE);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.DNA_SEQUENCER), "IAI", "SAI", "HAK", 'I', Items.IRON_INGOT, 'A', ItemHandler.INSTANCE.DNA_ANALYZER, 'S', ItemHandler.INSTANCE.COMPUTER_SCREEN, 'H', ItemHandler.INSTANCE.DISC_DRIVE, 'K', ItemHandler.INSTANCE.KEYBOARD);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.DNA_SYNTHESIZER), "IIS", "ICD", "IIC", 'I', Items.IRON_INGOT, 'S', ItemHandler.INSTANCE.COMPUTER_SCREEN, 'C', ItemHandler.INSTANCE.ADVANCED_CIRCUIT, 'D', ItemHandler.INSTANCE.DISC_DRIVE);

        for (int i = 0; i < 16; i++)
        {
            GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.CULTIVATOR_BOTTOM, 1, i), "GGG", "GWG", "III", 'G', new ItemStack(Blocks.STAINED_GLASS_PANE, 1, i), 'W', Items.WATER_BUCKET, 'I', Items.IRON_INGOT);
        }

        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.EMBRYONIC_MACHINE), "GIG", "GIG", "III", 'G', Blocks.GLASS_PANE, 'I', Items.IRON_INGOT);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.EMBRYO_CALCIFICATION_MACHINE), "GIG", "GSG", "III", 'G', Blocks.GLASS_PANE, 'I', Items.IRON_INGOT, 'S', ItemHandler.INSTANCE.NEEDLE);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.INCUBATOR), "GGG", "RRR", "III", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'G', Blocks.GLASS);
        GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.DNA_COMBINATOR_HYBRIDIZER), "SCS", "IDI", " K ", 'S', ItemHandler.INSTANCE.COMPUTER_SCREEN, 'C', ItemHandler.INSTANCE.BASIC_CIRCUIT, 'I', Items.IRON_INGOT, 'D', ItemHandler.INSTANCE.DISC_DRIVE, 'L', ItemHandler.INSTANCE.KEYBOARD);
        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.INSTANCE.PLANT_CELLS_PETRI_DISH), ItemHandler.INSTANCE.PLANT_CELLS, ItemHandler.INSTANCE.PETRI_DISH);

        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.IRON_NUGGET, 9), "i", 'i', Items.IRON_INGOT);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemHandler.INSTANCE.BASIC_CIRCUIT, 2), "nuggetIron", "nuggetIron", "nuggetIron", "nuggetIron", Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.REDSTONE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemHandler.INSTANCE.ADVANCED_CIRCUIT, 1), ItemHandler.INSTANCE.BASIC_CIRCUIT, ItemHandler.INSTANCE.BASIC_CIRCUIT, "nuggetIron", Items.GOLD_NUGGET, Items.REDSTONE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.INSTANCE.STORAGE_DISC), " I ", "ICI", " I ", 'I', "nuggetIron", 'C', ItemHandler.INSTANCE.BASIC_CIRCUIT));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.INSTANCE.LASER), "IDI", "ICI", "IRI", 'I', "nuggetIron", 'C', ItemHandler.INSTANCE.BASIC_CIRCUIT, 'D', Items.DIAMOND, 'R', Items.REDSTONE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.INSTANCE.DISC_DRIVE), "CLI", "i  ", "iib", 'i', "nuggetIron", 'C', ItemHandler.INSTANCE.ADVANCED_CIRCUIT, 'L', ItemHandler.INSTANCE.LASER, 'I', Items.IRON_INGOT, 'b', Blocks.STONE_BUTTON));

        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.INSTANCE.AJUGINUCULA_SMITHII_OIL), ItemHandler.INSTANCE.AJUGINUCULA_SMITHII_LEAVES, ItemHandler.INSTANCE.AJUGINUCULA_SMITHII_LEAVES, ItemHandler.INSTANCE.AJUGINUCULA_SMITHII_LEAVES, ItemHandler.INSTANCE.AJUGINUCULA_SMITHII_LEAVES, Items.GLASS_BOTTLE);

        GameRegistry.addRecipe(new ItemStack(Blocks.MOSSY_COBBLESTONE, 4), "MCM", "CMC", "MCM", 'M', BlockHandler.INSTANCE.MOSS, 'C', Blocks.COBBLESTONE);
        GameRegistry.addRecipe(new ItemStack(Blocks.STONEBRICK, 4, 1), "MCM", "CMC", "MCM", 'M', BlockHandler.INSTANCE.MOSS, 'C', new ItemStack(Blocks.STONEBRICK, 1, 0));

        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.COMPUTER_SCREEN), "IGI", "RPB", "CQI", 'I', Items.IRON_INGOT, 'G', new ItemStack(Items.DYE, 1, 2), 'R', new ItemStack(Items.DYE, 1, 1), 'P', Blocks.GLASS_PANE, 'B', new ItemStack(Items.DYE, 1, 4), 'C', ItemHandler.INSTANCE.BASIC_CIRCUIT, 'Q', Items.QUARTZ);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.INSTANCE.KEYBOARD), "bbb", "bbb", "psc", 'b', Blocks.STONE_BUTTON, 'p', Blocks.STONE_PRESSURE_PLATE, 's', Blocks.STONE_SLAB, 'c', ItemHandler.INSTANCE.BASIC_CIRCUIT);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.INSTANCE.DNA_ANALYZER), "iGi", "CLi", "iii", 'i', "nuggetIron", 'G', Blocks.GLASS, 'C', ItemHandler.INSTANCE.ADVANCED_CIRCUIT, 'L', ItemHandler.INSTANCE.LASER));

        GameRegistry.addSmelting(ItemHandler.INSTANCE.GRACILARIA, new ItemStack(ItemHandler.INSTANCE.LIQUID_AGAR), 0);

        addGrowthSerumRecipe(Items.COOKED_BEEF);
        addGrowthSerumRecipe(Items.COOKED_CHICKEN);
        addGrowthSerumRecipe(Items.COOKED_FISH);
        addGrowthSerumRecipe(Items.COOKED_MUTTON);
        addGrowthSerumRecipe(Items.COOKED_PORKCHOP);
        addGrowthSerumRecipe(Items.COOKED_RABBIT);

        for (int i = 0; i < EntityHandler.INSTANCE.getDinosaurs().size(); i++)
        {
            addGrowthSerumRecipe(new ItemStack(ItemHandler.INSTANCE.DINOSAUR_STEAK, 1, i));
        }
    }

    private void addGrowthSerumRecipe(Item meat)
    {
        addGrowthSerumRecipe(new ItemStack(meat));
    }

    private void addGrowthSerumRecipe(ItemStack meat)
    {
        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.INSTANCE.GROWTH_SERUM), Items.GOLDEN_CARROT, ItemHandler.INSTANCE.EMPTY_SYRINGE, Items.WATER_BUCKET, meat);
    }
}
