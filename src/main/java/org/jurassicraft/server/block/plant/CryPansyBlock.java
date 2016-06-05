package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.tab.TabHandler;

public class CryPansyBlock extends BlockBush
{
    public CryPansyBlock()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.plants);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType()
    {
        return EnumOffsetType.XZ;
    }
}
