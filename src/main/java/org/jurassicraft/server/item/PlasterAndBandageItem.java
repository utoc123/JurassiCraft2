package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.achievements.JCAchievements;
import org.jurassicraft.server.block.EncasedFossilBlock;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class PlasterAndBandageItem extends Item
{
    public PlasterAndBandageItem()
    {
        super();

        this.setCreativeTab(JCCreativeTabs.items);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || !player.canPlayerEdit(pos.offset(side), side, stack))
        {
            return EnumActionResult.PASS;
        }
        else
        {
            IBlockState state = world.getBlockState(pos);

            Block block = state.getBlock();

            if (block instanceof FossilBlock)
            {
                JCBlockRegistry blockRegistry = JurassiCraft.blockRegistry;

                int id = blockRegistry.getDinosaurId((FossilBlock) block, block.getMetaFromState(state));

                world.setBlockState(pos, blockRegistry.getEncasedFossil(id).getDefaultState().withProperty(EncasedFossilBlock.VARIANT, blockRegistry.getMetadata(id)));

                if (!player.capabilities.isCreativeMode)
                {
                    stack.stackSize--;
                }

                player.addStat(JCAchievements.fossils, 1);

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}
