package org.jurassicraft.server.block.tree;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCPlanksBlock extends Block
{
    public JCPlanksBlock(TreeType treeType)
    {
        super(Material.wood);
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setStepSound(SoundType.WOOD);
        this.setUnlocalizedName(treeType.name().toLowerCase() + "_planks");
        this.setCreativeTab(JCCreativeTabs.plants);
    }
}
