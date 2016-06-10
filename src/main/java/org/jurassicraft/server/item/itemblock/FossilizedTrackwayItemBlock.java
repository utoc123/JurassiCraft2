package org.jurassicraft.server.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.block.FossilizedTrackwayBlock;
import org.jurassicraft.server.lang.LangHelper;

public class FossilizedTrackwayItemBlock extends ItemBlock
{
    public FossilizedTrackwayItemBlock(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        FossilizedTrackwayBlock.TrackwayType type = getType(stack);
        return new LangHelper("tile.fossilized_trackway.name").withProperty("variant", "trackway." + type.getName() + ".name").build();
    }

    private FossilizedTrackwayBlock.TrackwayType getType(ItemStack stack)
    {
        FossilizedTrackwayBlock.TrackwayType[] values = FossilizedTrackwayBlock.TrackwayType.values();
        return values[stack.getItemDamage() % values.length];
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + getType(stack).getName();
    }
}
