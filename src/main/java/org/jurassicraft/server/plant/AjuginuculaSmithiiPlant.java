package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class AjuginuculaSmithiiPlant extends Plant
{
    @Override
    public String getName()
    {
        return "Ajuginucula Smithii";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.AJUGINUCULA_SMITHII;
    }
}
