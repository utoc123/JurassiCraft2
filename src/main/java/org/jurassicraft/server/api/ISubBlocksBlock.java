package org.jurassicraft.server.api;

import net.minecraft.item.ItemBlock;

public interface ISubBlocksBlock
{
    Class<? extends ItemBlock> getItemBlockClass();
}
