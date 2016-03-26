package org.jurassicraft.server.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.lang.AdvLang;

public class JCLogItemBlock extends ItemBlock
{
    public JCLogItemBlock(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) // In here we extend ItemBlock and return metadata (the block's metadata) for the item damage
    {
        return metadata;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        // So there I specify the lang
        return new AdvLang("tile.jc_wood.name").withProperty("wood", "wood." + stack.getItemDamage() + ".name").build(); // For now will use wood lang in this format: wood.0.name
    }
}
