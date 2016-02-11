package org.jurassicraft.server.creativetab;

import net.minecraft.item.Item;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.item.JCItemRegistry;

public class JCCreativeTabs
{
    public static JurassiCraftTab items = new JurassiCraftTab("jurassicraft.items")
    {
        public Item getTabIconItem()
        {
            return JCItemRegistry.amber;
        }
    };

    public static JurassiCraftFossilTab bones = new JurassiCraftFossilTab("jurassicraft.dino_bones");
    public static JurassiCraftDNATab dna = new JurassiCraftDNATab("jurassicraft.dna");
    public static JurassiCraftEggsTab eggs = new JurassiCraftEggsTab("jurassicraft.eggs");
    public static JurassiCraftSpawnEggsTab spawnEggs = new JurassiCraftSpawnEggsTab("jurassicraft.spawnEggs");
    public static JurassiCraftFoodTab foods = new JurassiCraftFoodTab("jurassicraft.foods");

    public static JurassiCraftTab blocks = new JurassiCraftTab("jurassicraft.blocks")
    {
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(JCBlockRegistry.gypsum_bricks);
        }
    };

    public static JurassiCraftTab plants = new JurassiCraftTab("jurassicraft.plants")
    {
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(JCBlockRegistry.small_royal_fern);
        }
    };

    public static JurassiCraftTab fossils = new JurassiCraftTab("jurassicraft.fossils")
    {
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(JCBlockRegistry.encased_fossils.get(0));
        }
    };

    public static JurassiCraftMerchandiseTab merchandise = new JurassiCraftMerchandiseTab("jurassicraft.merchandise");
}
