package org.jurassicraft.server.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.period.TimePeriod;

public class FossilItemBlock extends ItemBlock
{
    public FossilItemBlock(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        Dinosaur dinosaur = ((FossilBlock) block).getDinosaur(stack.getMetadata());

        if (dinosaur == null)
        {
            dinosaur = EntityHandler.INSTANCE.getDinosaurById(0);
        }

        return new LangHelper("tile.fossil_block.name").withProperty("period", "period." + dinosaur.getPeriod().getName() + ".name").build();
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        TimePeriod timePeriod = EntityHandler.INSTANCE.getDinosaurById(stack.getMetadata()).getPeriod();
        return super.getUnlocalizedName() + "." + timePeriod.getName();
    }
}
