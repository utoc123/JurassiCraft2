package org.jurassicraft.server.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.lang.AdvLang;

public class CultivateItemBlock extends ItemBlock
{
    public CultivateItemBlock(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("cultivate_bottom");
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        EnumDyeColor color = EnumDyeColor.byMetadata(stack.getItemDamage());
        return new AdvLang("tile.cultivate.name").withProperty("color", "color." + color.getName() + ".name").build();
    }
}
