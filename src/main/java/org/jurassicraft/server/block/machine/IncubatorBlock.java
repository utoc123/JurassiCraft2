package org.jurassicraft.server.block.machine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.tileentity.IncubatorTile;

public class IncubatorBlock extends OrientedBlock
{
    public IncubatorBlock()
    {
        super(Material.iron);
        this.setUnlocalizedName("incubator");
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(TabHandler.INSTANCE.blocks);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (stack.hasDisplayName())
        {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IncubatorTile)
            {
                ((IncubatorTile) tile).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof IncubatorTile)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IncubatorTile) tile);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new IncubatorTile();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (!player.isSneaking())
        {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IncubatorTile)
            {
                IncubatorTile incubator = (IncubatorTile) tile;

                if (incubator.isUseableByPlayer(player))
                {
                    player.openGui(JurassiCraft.instance, 6, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }
}
