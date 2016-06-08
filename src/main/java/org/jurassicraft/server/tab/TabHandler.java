package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.item.ItemHandler;

public enum TabHandler
{
    INSTANCE;

    public CreativeTabs ITEMS;
    public JurassiCraftFossilTab BONES;
    public JurassiCraftDNATab DNA;
    public JurassiCraftEggsTab EGGS;
    public JurassiCraftSpawnEggsTab SPAWN_EGGS;
    public JurassiCraftFoodTab FOODS;
    public CreativeTabs BLOCKS;
    public CreativeTabs PLANTS;
    public CreativeTabs FOSSILS;
    public JurassiCraftMerchandiseTab MERCHANDISE;

    public void init()
    {
        ITEMS = new CreativeTabs("jurassicraft.items")
        {
            @Override
            public Item getTabIconItem()
            {
                return ItemHandler.INSTANCE.AMBER;
            }
        };

        BONES = new JurassiCraftFossilTab("jurassicraft.dino_bones");
        DNA = new JurassiCraftDNATab("jurassicraft.dna");
        EGGS = new JurassiCraftEggsTab("jurassicraft.eggs");
        SPAWN_EGGS = new JurassiCraftSpawnEggsTab("jurassicraft.spawnEggs");
        FOODS = new JurassiCraftFoodTab("jurassicraft.foods");

        BLOCKS = new CreativeTabs("jurassicraft.blocks")
        {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(BlockHandler.INSTANCE.GYPSUM_BRICKS);
            }
        };

        PLANTS = new CreativeTabs("jurassicraft.plants")
        {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(BlockHandler.INSTANCE.SMALL_ROYAL_FERN);
            }
        };

        FOSSILS = new CreativeTabs("jurassicraft.fossils")
        {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(BlockHandler.INSTANCE.ENCASED_FOSSILS.get(0));
            }
        };

        MERCHANDISE = new JurassiCraftMerchandiseTab("jurassicraft.merchandise");
    }
}
