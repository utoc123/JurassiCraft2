package org.jurassicraft.server.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.item.ItemHandler;

public enum TabHandler
{
    INSTANCE;

    public CreativeTabs items;
    public JurassiCraftFossilTab bones;
    public JurassiCraftDNATab dna;
    public JurassiCraftEggsTab eggs;
    public JurassiCraftSpawnEggsTab spawnEggs;
    public JurassiCraftFoodTab foods;
    public CreativeTabs blocks;
    public CreativeTabs plants;
    public CreativeTabs fossils;
    public JurassiCraftMerchandiseTab merchandise;

    public void init()
    {
        items = new CreativeTabs("jurassicraft.items")
        {
            @Override
            public Item getTabIconItem()
            {
                return ItemHandler.INSTANCE.amber;
            }
        };

        bones = new JurassiCraftFossilTab("jurassicraft.dino_bones");
        dna = new JurassiCraftDNATab("jurassicraft.dna");
        eggs = new JurassiCraftEggsTab("jurassicraft.eggs");
        spawnEggs = new JurassiCraftSpawnEggsTab("jurassicraft.spawnEggs");
        foods = new JurassiCraftFoodTab("jurassicraft.foods");
        blocks = new CreativeTabs("jurassicraft.blocks")
        {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(BlockHandler.INSTANCE.gypsum_bricks);
            }
        };
        plants = new CreativeTabs("jurassicraft.plants")
        {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(BlockHandler.INSTANCE.small_royal_fern);
            }
        };
        fossils = new CreativeTabs("jurassicraft.fossils")
        {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(BlockHandler.INSTANCE.encased_fossils.get(0));
            }
        };
        merchandise = new JurassiCraftMerchandiseTab("jurassicraft.merchandise");
    }
}
