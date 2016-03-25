package org.jurassicraft.server.creativetab;

import net.minecraft.item.Item;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.item.JCItemRegistry;

public class JCCreativeTabs
{
    public static JurassiCraftTab items;
    public static JurassiCraftFossilTab bones;
    public static JurassiCraftDNATab dna;
    public static JurassiCraftEggsTab eggs;
    public static JurassiCraftSpawnEggsTab spawnEggs;
    public static JurassiCraftFoodTab foods;
    public static JurassiCraftTab blocks;
    public static JurassiCraftTab plants;
    public static JurassiCraftTab fossils;
    public static JurassiCraftMerchandiseTab merchandise;

    public void init()
    {
        items = new JurassiCraftTab("jurassicraft.items")
        {
            public Item getTabIconItem()
            {
                return JCItemRegistry.amber;
            }
        };

        bones = new JurassiCraftFossilTab("jurassicraft.dino_bones");
        dna = new JurassiCraftDNATab("jurassicraft.dna");
        eggs = new JurassiCraftEggsTab("jurassicraft.eggs");
        spawnEggs = new JurassiCraftSpawnEggsTab("jurassicraft.spawnEggs");
        foods = new JurassiCraftFoodTab("jurassicraft.foods");
        blocks = new JurassiCraftTab("jurassicraft.blocks")
        {
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(JCBlockRegistry.gypsum_bricks);
            }
        };
        plants = new JurassiCraftTab("jurassicraft.plants")
        {
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(JCBlockRegistry.small_royal_fern);
            }
        };
        fossils = new JurassiCraftTab("jurassicraft.fossils")
        {
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(JCBlockRegistry.encased_fossils.get(0));
            }
        };
        merchandise = new JurassiCraftMerchandiseTab("jurassicraft.merchandise");
    }
}
