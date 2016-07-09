package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.tab.TabHandler;

public class PaleoBaleBlock extends BlockRotatedPillar
{
    private PaleoBaleBlock.Variant variant;

    public PaleoBaleBlock(PaleoBaleBlock.Variant variant)
    {
        super(Material.GRASS, MapColor.FOLIAGE);
        this.variant = variant;
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
        this.setCreativeTab(TabHandler.PLANTS);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance)
    {
        entity.fall(fallDistance, 0.2F);
    }

    public PaleoBaleBlock.Variant getVariant()
    {
        return variant;
    }

    public enum Variant implements IStringSerializable
    {
        CYCADEOIDEA(BlockHandler.CYCADEOIDEA),
        CYCAD(BlockHandler.SMALL_CYCAD),
        FERN(BlockHandler.SCALY_TREE_FERN, BlockHandler.SMALL_CHAIN_FERN, BlockHandler.SMALL_ROYAL_FERN),
        LEAVES(BlockHandler.ANCIENT_LEAVES.values().toArray(new Block[BlockHandler.ANCIENT_LEAVES.size()])),
        OTHER(BlockHandler.AJUGINUCULA_SMITHII, BlockHandler.CRY_PANSY, BlockHandler.DICKSONIA, BlockHandler.DICROIDIUM_ZUBERI, BlockHandler.WILD_ONION, BlockHandler.ZAMITES);

        private Item[] ingredients;

        Variant(Block... ingredients)
        {
            this.ingredients = new Item[ingredients.length];

            for (int i = 0; i < ingredients.length; i++)
            {
                this.ingredients[i] = Item.getItemFromBlock(ingredients[i]);
            }
        }

        Variant(Item... ingredients)
        {
            this.ingredients = ingredients;
        }

        @Override
        public String getName()
        {
            return name().toLowerCase();
        }

        public Item[] getIngredients()
        {
            return ingredients;
        }
    }
}
