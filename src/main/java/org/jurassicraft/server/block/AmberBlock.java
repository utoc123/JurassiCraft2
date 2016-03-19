package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.item.JCItemRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmberBlock extends Block
{
    public AmberBlock()
    {
        super(Material.rock);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setCreativeTab(JCCreativeTabs.blocks);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> ret = new ArrayList<ItemStack>();

        Random rand = world instanceof World ? ((World) world).rand : RANDOM;

        int count = rand.nextInt(fortune + 2) - 1;

        if (count < 0)
        {
            count = 0;
        }

        for (int i = 0; i < count + 1; i++)
        {
            Item item = JCItemRegistry.amber;

            if (item != null)
            {
                ret.add(new ItemStack(item, 1, rand.nextBoolean() ? 1 : 0));
            }
        }

        return ret;
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion)
    {
        return false;
    }
}
