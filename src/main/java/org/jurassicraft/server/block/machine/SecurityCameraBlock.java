package org.jurassicraft.server.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class SecurityCameraBlock extends Block
{
    public SecurityCameraBlock()
    {
        super(Material.iron);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setCreativeTab(JCCreativeTabs.blocks);
    }
}
