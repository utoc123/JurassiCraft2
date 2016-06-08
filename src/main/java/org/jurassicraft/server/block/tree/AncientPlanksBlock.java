package org.jurassicraft.server.block.tree;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.jurassicraft.server.tab.TabHandler;

public class AncientPlanksBlock extends Block
{
    public AncientPlanksBlock(TreeType treeType)
    {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(treeType.name().toLowerCase() + "_planks");
        this.setCreativeTab(TabHandler.INSTANCE.PLANTS);
    }
}
