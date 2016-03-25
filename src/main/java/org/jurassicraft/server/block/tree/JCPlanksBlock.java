package org.jurassicraft.server.block.tree;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCPlanksBlock extends Block
{
    public JCPlanksBlock(TreeType type)
    {
        super(Material.wood);
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setStepSound(Block.soundTypeWood);
        this.setUnlocalizedName(type.name().toLowerCase() + "_planks");
        this.setCreativeTab(JCCreativeTabs.plants);
    }
}
