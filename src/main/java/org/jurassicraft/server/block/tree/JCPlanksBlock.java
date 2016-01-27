package org.jurassicraft.server.block.tree;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCPlanksBlock extends Block
{
    private WoodType treeType;

    public JCPlanksBlock(WoodType type, String treeName)
    {
        super(Material.wood);
        setHardness(2.0F);
        setResistance(0.5F);
        setStepSound(Block.soundTypeWood);
        setUnlocalizedName(treeName + "_planks");

        this.setCreativeTab(JCCreativeTabs.plants);

        treeType = type;
    }
}
