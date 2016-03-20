package org.jurassicraft.server.block.machine;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.ISubBlocksBlock;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.item.itemblock.CultivateItemBlock;
import org.jurassicraft.server.tileentity.CultivatorTile;

import java.util.List;

public class CultivatorBlock extends BlockContainer implements ISubBlocksBlock
{
    public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public CultivatorBlock(String position)
    {
        super(Material.iron);
        this.setUnlocalizedName("cultivator_" + position);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setHardness(2.0F);
        this.setResistance(5.0F);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return ((EnumDyeColor) state.getValue(COLOR)).getMetadata();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        if (world.getBlockState(pos).getBlock() == JCBlockRegistry.cultivate_top)
        {
            pos.add(0, -1, 0);
        }

        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof CultivatorTile)
        {
            InventoryHelper.dropInventoryItems(world, pos, (CultivatorTile) tile);
        }
    }

    /**
     * returns a subtypes of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        EnumDyeColor[] colors = EnumDyeColor.values();

        for (EnumDyeColor color : colors)
        {
            subtypes.add(new ItemStack(item, 1, color.getMetadata()));
        }
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass()
    {
        return CultivateItemBlock.class;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new CultivatorTile();
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state)
    {
        return ((EnumDyeColor) state.getValue(COLOR)).getMapColor();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumDyeColor) state.getValue(COLOR)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, COLOR);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
