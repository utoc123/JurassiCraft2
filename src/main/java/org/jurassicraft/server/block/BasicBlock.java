package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.jurassicraft.server.creativetab.TabHandler;

public class BasicBlock extends Block
{
    public BasicBlock(Material material)
    {
        super(material);
        this.setCreativeTab(TabHandler.INSTANCE.blocks);
    }
}
