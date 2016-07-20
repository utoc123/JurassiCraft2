package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.tile.ActionFigureTile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ActionFigureItem extends Item
{
    public ActionFigureItem()
    {
        super();

        this.setCreativeTab(TabHandler.DECORATIONS);
        this.setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        pos = pos.offset(side);

        if (player.canPlayerEdit(pos, side, stack))
        {
            Block block = BlockHandler.ACTION_FIGURE;

            if (block.canPlaceBlockAt(world, pos))
            {
                IBlockState state = block.getDefaultState();
                world.setBlockState(pos, block.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, 0, player));
                block.onBlockPlacedBy(world, pos, state, player, stack);

                ActionFigureTile tile = (ActionFigureTile) world.getTileEntity(pos);
                tile.setDinosaur(stack.getItemDamage());

                if (!player.capabilities.isCreativeMode)
                {
                    stack.stackSize--;
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String dinoName = getDinosaur(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.action_figure.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        return EntityHandler.getDinosaurById(stack.getMetadata());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());

        Collections.sort(dinosaurs);

        for (Dinosaur dinosaur : dinosaurs)
        {
            if (dinosaur.shouldRegister())
            {
                subtypes.add(new ItemStack(item, 1, EntityHandler.getDinosaurId(dinosaur)));
            }
        }
    }
}
