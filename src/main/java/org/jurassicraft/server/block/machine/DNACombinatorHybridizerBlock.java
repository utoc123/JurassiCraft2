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
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.tile.DNACombinatorHybridizerTile;

public class DNACombinatorHybridizerBlock extends OrientedBlock
{
    public DNACombinatorHybridizerBlock()
    {
        super(Material.IRON);
        this.setUnlocalizedName("dna_combinator_hybridizer");
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(TabHandler.BLOCKS);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof DNACombinatorHybridizerTile)
            {
                ((DNACombinatorHybridizerTile) tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof DNACombinatorHybridizerTile)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (DNACombinatorHybridizerTile) tileentity);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new DNACombinatorHybridizerTile();
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
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof DNACombinatorHybridizerTile)
            {
                DNACombinatorHybridizerTile embryonicMachine = (DNACombinatorHybridizerTile) tileEntity;

                if (embryonicMachine.isUseableByPlayer(player))
                {
                    player.openGui(JurassiCraft.INSTANCE, 7, world, pos.getX(), pos.getY(), pos.getZ());
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
